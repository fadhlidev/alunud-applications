package com.alunud.application.persistence.memory.service.impl

import com.alunud.application.persistence.memory.service.RedisService
import com.alunud.exception.NotFoundException
import io.github.crackthecodeabhi.kreds.connection.KredsClient
import org.springframework.beans.factory.annotation.Value

class KredsRedisService(private val redisClient: KredsClient) : RedisService {

    private val prefix = "alunud-kreds";

    override suspend fun setValue(key: String, value: String) {
        require(key.isNotBlank()) { "Key is required" }
        require(value.isNotBlank()) { "Value is required" }
        redisClient.set("$prefix-$key", value)
    }

    override suspend fun setValue(key: String, value: String, exp: ULong) {
        require(key.isNotBlank()) { "Key is required" }
        require(value.isNotBlank()) { "Value is required" }
        redisClient.set("$prefix-$key", value)
        redisClient.expire("$prefix-$key", exp)
    }

    override suspend fun getValue(key: String): String? {
        require(key.isNotBlank()) { "Key is required" }
        return redisClient.get("$prefix-$key")
    }

    override suspend fun deleteValue(key: String) {
        require(key.isNotBlank()) { "Key is required" }
        val code = redisClient.del("$prefix-$key")
        if (code == 0L) {
            throw NotFoundException("Key $key not found")
        }
    }

    override suspend fun deleteAllValues() {
        val keys = redisClient.keys("$prefix-*")
        keys.forEach {
            redisClient.del(it)
        }
    }

}