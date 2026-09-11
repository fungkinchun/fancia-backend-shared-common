package com.fancia.backend.shared.common.config

import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

internal class IdleResetRedisConnectionFactory(
    val target: LettuceConnectionFactory,
    private val idleGapNanos: Long = DEFAULT_IDLE_GAP_NANOS,
) : RedisConnectionFactory by target {
    private val log = LoggerFactory.getLogger(javaClass)
    private val lastBorrowNanos = AtomicLong(System.nanoTime())

    override fun getConnection(): RedisConnection {
        resetIfIdle()
        return target.connection.also { markBorrowed() }
    }

    fun resetSharedConnection(reason: String) {
        log.info("Resetting Lettuce shared connection ({})", reason)
        LettuceConnectionFactories.resetSharedConnection(target)
        markBorrowed()
    }

    private fun resetIfIdle() {
        val now = System.nanoTime()
        val previous = lastBorrowNanos.get()
        val gap = now - previous
        if (gap <= idleGapNanos) return

        if (!lastBorrowNanos.compareAndSet(previous, now)) return

        log.info(
            "Lettuce idle gap of {}ms; resetting shared connection before borrow",
            TimeUnit.NANOSECONDS.toMillis(gap),
        )
        LettuceConnectionFactories.resetSharedConnection(target)
    }

    private fun markBorrowed() {
        lastBorrowNanos.set(System.nanoTime())
    }

    companion object {
        val DEFAULT_IDLE_GAP_NANOS: Long = TimeUnit.SECONDS.toNanos(30)
    }
}

internal object LettuceConnectionFactories {
    private val log = LoggerFactory.getLogger(javaClass)

    fun unwrap(factory: RedisConnectionFactory): LettuceConnectionFactory? =
        when (factory) {
            is LettuceConnectionFactory -> factory
            is IdleResetRedisConnectionFactory -> factory.target
            else -> null
        }

    fun resetSharedConnection(factory: RedisConnectionFactory) {
        val lettuce = unwrap(factory) ?: return
        runCatching { lettuce.resetConnection() }
            .onFailure { ex ->
                log.warn("Lettuce resetConnection failed: {}", ex.message)
            }
    }

    fun warmConnection(factory: RedisConnectionFactory, maxAttempts: Int = 3) {
        repeat(maxAttempts) { attempt ->
            try {
                factory.connection.use { connection ->
                    connection.ping()
                }
                log.info("Lettuce warm-up ping succeeded on attempt {}", attempt + 1)
                return
            } catch (ex: Exception) {
                log.warn(
                    "Lettuce warm-up attempt {}/{} failed: {}",
                    attempt + 1,
                    maxAttempts,
                    ex.message,
                )
                resetSharedConnection(factory)
                if (attempt < maxAttempts - 1) {
                    Thread.sleep(500L * (attempt + 1))
                }
            }
        }
        log.error("Lettuce warm-up failed after {} attempts", maxAttempts)
    }
}
