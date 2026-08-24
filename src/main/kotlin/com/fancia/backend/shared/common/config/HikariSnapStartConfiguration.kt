package com.fancia.backend.shared.common.config

import com.zaxxer.hikari.HikariDataSource
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
                if (bean is HikariIdleResetDataSource) return bean
                if (bean !is HikariDataSource) return bean

                bean.isAllowPoolSuspension = true
                if (bean.connectionTestQuery.isNullOrBlank()) {
                    bean.connectionTestQuery = "SELECT 1"
                }
                // Fail fast on sockets closed during a Lambda freeze.
                if (bean.validationTimeout > 3_000L) {
                    bean.validationTimeout = 3_000L
                }
                log.info("Wrapping HikariDataSource '{}' for idle soft-evict on borrow", beanName)
                return HikariIdleResetDataSource(bean)
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
        HikariDataSources.warmPool(dataSource)
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
}
