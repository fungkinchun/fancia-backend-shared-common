package com.fancia.backend.shared.common.config

import org.crac.Context
import org.crac.Core
import org.crac.Resource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.jdbc.HikariCheckpointRestoreLifecycle
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
@ConditionalOnClass(
    name = [
        "org.crac.Core",
        "com.zaxxer.hikari.HikariDataSource",
        "org.springframework.boot.jdbc.HikariCheckpointRestoreLifecycle",
    ],
)
class HikariSnapStartConfiguration {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    @ConditionalOnMissingBean
    fun hikariCheckpointRestoreLifecycle(
        dataSource: DataSource,
        applicationContext: ConfigurableApplicationContext,
    ): HikariCheckpointRestoreLifecycle {
        log.info("Configuring HikariCheckpointRestoreLifecycle for SnapStart")
        return HikariCheckpointRestoreLifecycle(dataSource, applicationContext)
    }

    @Bean
    fun hikariPoolCracResourceRegistrar(
        dataSource: DataSource,
        lifecycle: HikariCheckpointRestoreLifecycle,
    ): HikariPoolCracResourceRegistrar =
        HikariPoolCracResourceRegistrar(lifecycle, dataSource)
}

class HikariPoolCracResourceRegistrar(
    private val lifecycle: HikariCheckpointRestoreLifecycle,
    private val dataSource: DataSource,
) : SmartInitializingSingleton {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun afterSingletonsInstantiated() {
        Core.getGlobalContext().register(HikariPoolCracResource(lifecycle, dataSource))
        log.info("Registered Hikari CRaC resource for SnapStart with post-restore warm-up")
    }
}

private class HikariPoolCracResource(
    private val lifecycle: HikariCheckpointRestoreLifecycle,
    private val dataSource: DataSource,
) : Resource {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun beforeCheckpoint(context: Context<out Resource>) {
        log.info("SnapStart beforeCheckpoint: suspending Hikari pool")
        lifecycle.stop()
    }

    override fun afterRestore(context: Context<out Resource>) {
        log.info("SnapStart afterRestore: resuming Hikari pool")
        lifecycle.start()
        warmPool()
    }

    private fun warmPool() {
        try {
            dataSource.connection.use { connection ->
                connection.prepareStatement("SELECT 1").use { statement ->
                    statement.execute()
                }
            }
            log.info("SnapStart afterRestore: Hikari pool warm-up succeeded")
        } catch (ex: Exception) {
            log.warn(
                "SnapStart afterRestore: Hikari pool warm-up failed (will retry on first request): {}",
                ex.message,
            )
        }
    }
}
