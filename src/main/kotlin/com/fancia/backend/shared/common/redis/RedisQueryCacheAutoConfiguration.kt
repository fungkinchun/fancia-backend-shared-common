package com.fancia.backend.shared.common.redis

import com.fasterxml.jackson.databind.ObjectMapper
import io.lettuce.core.ClientOptions
import io.lettuce.core.SocketOptions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import java.net.URI
import java.time.Duration

@Configuration
@ConditionalOnExpression("T(org.springframework.util.StringUtils).hasText('\${spring.data.redis.url:}')")
class RedisQueryCacheAutoConfiguration(
    @Value("\${spring.data.redis.url}") private val redisUrl: String,
    @Value("\${spring.data.redis.timeout:3s}") private val commandTimeout: Duration,
    @Value("\${spring.data.redis.connect-timeout:5s}") private val connectTimeout: Duration,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    @Primary
    @ConditionalOnMissingBean(RedisConnectionFactory::class)
    fun lettuceConnectionFactory(): LettuceConnectionFactory {
        require(redisUrl.isNotBlank()) { "spring.data.redis.url must not be blank" }
        val uri = URI(normalizeRedisUrl(redisUrl))
        val host = uri.host ?: error("REDIS_URL is missing a host")
        val port = if (uri.port > 0) uri.port else 6379
        val useSsl = uri.scheme.equals("rediss", ignoreCase = true)

        val standalone = RedisStandaloneConfiguration(host, port).apply {
            uri.userInfo?.let { userInfo ->
                val parts = userInfo.split(":", limit = 2)
                when (parts.size) {
                    1 -> password = RedisPassword.of(parts[0])
                    2 -> {
                        if (parts[0].isNotBlank()) {
                            username = parts[0]
                        }
                        if (parts[1].isNotBlank()) {
                            password = RedisPassword.of(parts[1])
                        }
                    }
                }
            }
        }

        val clientOptions =
            ClientOptions.builder()
                .socketOptions(
                    SocketOptions.builder()
                        .connectTimeout(connectTimeout)
                        .keepAlive(true)
                        .build(),
                )
                .build()

        val clientConfigBuilder =
            LettuceClientConfiguration.builder()
                .commandTimeout(commandTimeout)
                .clientOptions(clientOptions)

        val clientConfig =
            if (useSsl) {
                clientConfigBuilder.useSsl().build()
            } else {
                clientConfigBuilder.build()
            }

        log.info("Configuring Redis query cache host={} port={} ssl={}", host, port, useSsl)
        return LettuceConnectionFactory(standalone, clientConfig).apply {
            afterPropertiesSet()
        }
    }

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate::class)
    fun stringRedisTemplate(connectionFactory: RedisConnectionFactory): StringRedisTemplate =
        StringRedisTemplate(connectionFactory)

    @Bean
    @ConditionalOnMissingBean(RedisQueryCache::class)
    fun redisQueryCache(
        stringRedisTemplate: StringRedisTemplate,
        objectMapper: ObjectMapper,
    ): RedisQueryCache = RedisQueryCache(stringRedisTemplate, objectMapper)

    private fun normalizeRedisUrl(raw: String): String {
        val trimmed = raw.trim()
        if (trimmed.startsWith("redis://", ignoreCase = true) &&
            trimmed.contains("upstash.io", ignoreCase = true)
        ) {
            return "rediss://" + trimmed.substringAfter("://")
        }
        return trimmed
    }
}
