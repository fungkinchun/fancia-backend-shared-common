package com.fancia.backend.shared.common.config

import org.crac.Context
import org.crac.Core
import org.crac.Resource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.jdbc.HikariCheckpointRestoreLifecycle
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.zaxxer.hikari.HikariDataSource
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
    fun snapStartHikariDataSourcePostProcessor(): BeanPostProcessor =
        object : BeanPostProcessor {
            override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
                if (bean is HikariDataSource) {
                    bean.isAllowPoolSuspension = true
                    if (bean.connectionTestQuery.isNullOrBlank()) {
                        bean.connectionTestQuery = "SELECT 1"
                    }
                }
                return bean
            }
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
        log.info("Registered Hikari CRaC resource for SnapStart with post-restore pool reset")
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
        HikariDataSources.softEvictConnections(dataSource)
    }

    override fun afterRestore(context: Context<out Resource>) {
        log.info("SnapStart afterRestore: resetting Hikari pool")
        resetPool()
        warmPoolWithRetry()
    }

    private fun resetPool() {
        runCatching { lifecycle.stop() }
            .onFailure { ex ->
                log.warn("SnapStart afterRestore: pool stop failed: {}", ex.message)
            }
        HikariDataSources.softEvictConnections(dataSource)
        runCatching { lifecycle.start() }
            .onFailure { ex ->
                log.warn("SnapStart afterRestore: pool start failed: {}", ex.message)
            }
    }

    private fun warmPoolWithRetry(maxAttempts: Int = 4) {
        repeat(maxAttempts) { attempt ->
            try {
                dataSource.connection.use { connection ->
                    connection.prepareStatement("SELECT 1").use { statement ->
                        statement.execute()
                    }
                }
                log.info(
                    "SnapStart afterRestore: Hikari pool warm-up succeeded on attempt {}",
                    attempt + 1,
                )
                return
            } catch (ex: Exception) {
                log.warn(
                    "SnapStart afterRestore: warm-up attempt {} failed: {}",
                    attempt + 1,
                    ex.message,
                )
                HikariDataSources.softEvictConnections(dataSource)
                if (attempt < maxAttempts - 1) {
                    Thread.sleep(750L * (attempt + 1))
                }
            }
        }
        log.error(
            "SnapStart afterRestore: Hikari pool warm-up failed after {} attempts",
            maxAttempts,
        )
    }
}
