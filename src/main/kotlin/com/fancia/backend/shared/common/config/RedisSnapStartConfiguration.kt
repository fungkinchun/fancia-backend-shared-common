package com.fancia.backend.shared.common.config

import org.crac.Context
import org.crac.Core
import org.crac.Resource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
@ConditionalOnClass(
    name = [
        "org.crac.Core",
        "org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory",
    ],
)
class RedisSnapStartConfiguration {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun snapStartLettuceConnectionFactoryPostProcessor(): BeanPostProcessor =
        object : BeanPostProcessor {
            override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
                if (bean is IdleResetRedisConnectionFactory) return bean
                if (bean !is LettuceConnectionFactory) return bean

                log.info("Wrapping LettuceConnectionFactory '{}' for idle reset on borrow", beanName)
                return IdleResetRedisConnectionFactory(bean)
            }
        }

    @Bean
    fun lettuceCracResourceRegistrar(
        connectionFactories: ObjectProvider<RedisConnectionFactory>,
    ): LettuceCracResourceRegistrar =
        LettuceCracResourceRegistrar(connectionFactories)
}

class LettuceCracResourceRegistrar(
    private val connectionFactories: ObjectProvider<RedisConnectionFactory>,
) : SmartInitializingSingleton {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun afterSingletonsInstantiated() {
        val factories =
            connectionFactories
                .orderedStream()
                .filter { LettuceConnectionFactories.unwrap(it) != null }
                .toList()
        if (factories.isEmpty()) return

        Core.getGlobalContext().register(LettuceCracResource(factories))
        log.info(
            "Registered Lettuce CRaC resource for SnapStart ({} connection factor{})",
            factories.size,
            if (factories.size == 1) "y" else "ies",
        )
    }
}

private class LettuceCracResource(
    private val factories: List<RedisConnectionFactory>,
) : Resource {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun beforeCheckpoint(context: Context<out Resource>) {
        log.info("SnapStart beforeCheckpoint: resetting Lettuce shared connections")
        factories.forEach { LettuceConnectionFactories.resetSharedConnection(it) }
    }

    override fun afterRestore(context: Context<out Resource>) {
        log.info("SnapStart afterRestore: resetting and warming Lettuce connections")
        factories.forEach { factory ->
            LettuceConnectionFactories.resetSharedConnection(factory)
            LettuceConnectionFactories.warmConnection(factory)
        }
    }
}
