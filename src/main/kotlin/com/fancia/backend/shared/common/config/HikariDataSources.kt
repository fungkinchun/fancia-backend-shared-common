package com.fancia.backend.shared.common.config

import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import org.springframework.jdbc.datasource.DelegatingDataSource
import javax.sql.DataSource

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
}
