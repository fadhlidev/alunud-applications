package com.alunud.application.persistence.memory.service.impl

import com.alunud.application.persistence.memory.service.RedisService
import com.alunud.exception.NotFoundException
import java.util.*
import kotlin.concurrent.schedule

class InMemoryRedisService : RedisService {

    private val storage: MutableMap<String, Pair<String, Long>> = mutableMapOf()

    init {
        startExpirationTask()
    }

    private fun startExpirationTask() {
        val timer = Timer(true)
        val expirationTask = timer.schedule(1000, 1000) {
            removeExpiredValues()
        }
    }

    override suspend fun setValue(key: String, value: String) {
        require(key.isNotBlank()) { "Key is required" }
        require(value.isNotBlank()) { "Value is required" }
        storage[key] = Pair(value, Long.MAX_VALUE)
    }

    override suspend fun setValue(key: String, value: String, exp: ULong) {
        require(key.isNotBlank()) { "Key is required" }
        require(value.isNotBlank()) { "Value is required" }
        val expirationTime = System.currentTimeMillis() + exp.toLong() * 1000L
        storage[key] = Pair(value, expirationTime)
    }

    override suspend fun getValue(key: String): String? {
        require(key.isNotBlank()) { "Key is required" }
        val pair = storage[key]
        return if (pair != null && pair.second >= System.currentTimeMillis()) {
            pair.first
        } else {
            null
        }
    }

    override suspend fun deleteValue(key: String) {
        require(key.isNotBlank()) { "Key is required" }

        if (storage.remove(key) == null) {
            throw NotFoundException("Key $key not found")
        }
    }

    override suspend fun deleteAllValues() {
        storage.clear()
    }

    override suspend fun isExists(key: String): Boolean {
        return storage.containsKey(key)
    }

    private fun removeExpiredValues() {
        val currentTime = System.currentTimeMillis()
        val expiredKeys = storage.filterValues { it.second < currentTime }.keys
        expiredKeys.forEach { storage.remove(it) }
    }

}