package com.alunud.application.persistence.memory.service

interface RedisService {
    suspend fun setValue(key: String, value: String)
    suspend fun setValue(key: String, value: String, exp: ULong)
    suspend fun getValue(key: String): String?
    suspend fun deleteValue(key: String)
    suspend fun deleteAllValues()
    suspend fun isExists(key: String): Boolean
}