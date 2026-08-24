package com.fancia.backend.shared.common.config

import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import org.springframework.jdbc.datasource.DelegatingDataSource
import java.sql.Connection
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import javax.sql.DataSource

/**
 * After a long gap between borrows (e.g. Lambda freeze/thaw), soft-evicts stale
 * sockets so the next [getConnection] opens a fresh one. SnapStart CRaC handles
 * checkpoint/restore separately; this covers ordinary freezes between invokes.
 */
internal class HikariIdleResetDataSource(
    target: HikariDataSource,
    private val idleGapNanos: Long = DEFAULT_IDLE_GAP_NANOS,
) : DelegatingDataSource(target) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val lastBorrowNanos = AtomicLong(System.nanoTime())

    override fun getConnection(): Connection {
        resetIfIdle()
        return super.getConnection().also { markBorrowed() }
    }

    override fun getConnection(username: String, password: String): Connection {
        resetIfIdle()
        return super.getConnection(username, password).also { markBorrowed() }
    }

    private fun resetIfIdle() {
        val now = System.nanoTime()
        val previous = lastBorrowNanos.get()
        val gap = now - previous
        if (gap <= idleGapNanos) return

        // Only one borrower should soft-evict per idle gap.
        if (!lastBorrowNanos.compareAndSet(previous, now)) return

        log.info(
            "Hikari idle gap of {}ms; soft-evicting connections before borrow",
            TimeUnit.NANOSECONDS.toMillis(gap),
        )
        targetDataSource?.let { HikariDataSources.softEvictConnections(it) }
    }

    private fun markBorrowed() {
        lastBorrowNanos.set(System.nanoTime())
    }

    companion object {
        val DEFAULT_IDLE_GAP_NANOS: Long = TimeUnit.SECONDS.toNanos(30)
    }
}

internal object HikariDataSources {
    private val log = LoggerFactory.getLogger(HikariDataSources::class.java)

    fun unwrap(dataSource: DataSource): HikariDataSource? {
        var current: DataSource = dataSource
        val visited = mutableSetOf<DataSource>()

        while (current !in visited) {
            visited += current
            if (current is HikariDataSource) return current

            if (current is DelegatingDataSource) {
                current.targetDataSource?.let {
                    current = it
                    continue
                }
            }

            try {
                if (current.isWrapperFor(HikariDataSource::class.java)) {
                    return current.unwrap(HikariDataSource::class.java)
                }
            } catch (ex: Exception) {
                log.debug("Could not unwrap DataSource to HikariDataSource: {}", ex.message)
            }

            break
        }

        return null
    }

    fun softEvictConnections(dataSource: DataSource) {
        val hikari = unwrap(dataSource) ?: return
        hikari.hikariPoolMXBean?.softEvictConnections()
    }

    fun warmPool(dataSource: DataSource, maxAttempts: Int = 4) {
        repeat(maxAttempts) { attempt ->
            try {
                dataSource.connection.use { connection ->
                    connection.prepareStatement("SELECT 1").use { statement ->
                        statement.execute()
                    }
                }
                log.info("Hikari pool warm-up succeeded on attempt {}", attempt + 1)
                return
            } catch (ex: Exception) {
                log.warn(
                    "Hikari pool warm-up attempt {}/{} failed: {}",
                    attempt + 1,
                    maxAttempts,
                    ex.message,
                )
                softEvictConnections(dataSource)
                if (attempt < maxAttempts - 1) {
                    Thread.sleep(750L * (attempt + 1))
                }
            }
        }
        log.error("Hikari pool warm-up failed after {} attempts", maxAttempts)
    }
}
