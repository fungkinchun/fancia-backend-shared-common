package com.fancia.backend.shared.common.redis

import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.core.StringRedisTemplate
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.json.JsonMapper
import java.security.MessageDigest
import java.time.Duration

class RedisQueryCache(
    private val redis: StringRedisTemplate,
    private val jsonMapper: JsonMapper,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun <T> getOrLoad(
        key: String,
        ttl: Duration,
        type: TypeReference<T>,
        loader: () -> T,
    ): T {
        try {
            val cached = redis.opsForValue().get(key)
            if (cached != null) {
                return jsonMapper.readValue(cached, type)
            }
        } catch (ex: Exception) {
            log.warn("Redis cache read failed key={}", key, ex)
        }

        val value = loader()
        try {
            redis.opsForValue().set(key, jsonMapper.writeValueAsString(value), ttl)
        } catch (ex: Exception) {
            log.warn("Redis cache write failed key={}", key, ex)
        }
        return value
    }

    fun evict(key: String) {
        try {
            redis.delete(key)
        } catch (ex: Exception) {
            log.warn("Redis cache evict failed key={}", key, ex)
        }
    }

    fun evictByPrefix(prefix: String) {
        try {
            val keys = mutableListOf<String>()
            redis.execute { connection ->
                val options = ScanOptions.scanOptions().match("$prefix*").count(200).build()
                connection.scan(options).use { cursor ->
                    while (cursor.hasNext()) {
                        keys.add(String(cursor.next()))
                    }
                }
                null
            }
            if (keys.isNotEmpty()) {
                redis.delete(keys)
            }
        } catch (ex: Exception) {
            log.warn("Redis cache prefix evict failed prefix={}", prefix, ex)
        }
    }
}

object CacheKeys {
    fun hash(vararg parts: Any?): String {
        val raw = parts.joinToString("|") { part ->
            when (part) {
                null -> "-"
                is Collection<*> -> part.map { it?.toString() ?: "-" }.sorted().joinToString(",")
                else -> part.toString()
            }
        }
        val digest = MessageDigest.getInstance("SHA-256").digest(raw.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }.take(32)
    }
}
