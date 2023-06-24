package com.alunud.application.config

import com.alunud.application.persistence.memory.service.RedisService
import com.alunud.application.persistence.memory.service.impl.InMemoryRedisService
import com.alunud.application.persistence.memory.service.impl.KredsRedisService
import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.newClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedisConfig {

    @Bean
    fun redisService(@Value("\${redis.uri}") redisURI: String): RedisService {
        return if (redisURI.isBlank()) {
            InMemoryRedisService()
        } else {
            val client = newClient(Endpoint.from(redisURI))
            KredsRedisService(client)
        }
    }

}