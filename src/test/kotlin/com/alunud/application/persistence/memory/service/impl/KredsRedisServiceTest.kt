package com.alunud.application.persistence.memory.service.impl

import com.alunud.exception.NotFoundException
import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.newClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class KredsRedisServiceTest {

    private val kredsRedisService = KredsRedisService(newClient(Endpoint.from("127.0.01:6379")))

    @AfterEach
    fun `clear persisted values`() = runBlocking {
        kredsRedisService.deleteAllValues()
    }

    @Test
    fun `should set and get value`() = runBlocking {
        val pair = Pair("data#1", "value#1")
        kredsRedisService.setValue(pair.first, pair.second)
        assertEquals(pair.second, kredsRedisService.getValue(pair.first))
    }

    @Test
    fun `should not set value`() {
        runBlocking {
            assertThrows<IllegalArgumentException> {
                kredsRedisService.setValue("", "value")
            }

            assertThrows<IllegalArgumentException> {
                kredsRedisService.setValue("key", "")
            }
        }
    }

    @Test
    fun `should set and get value with exp`() = runBlocking {
        val pair = Pair("data#2", "value#2")
        kredsRedisService.setValue(pair.first, pair.second, 5U)
        assertEquals(pair.second, kredsRedisService.getValue(pair.first))
        delay(6_000)
        assertNull(kredsRedisService.getValue(pair.first))
    }

    @Test
    fun `should not set value with exp`() {
        runBlocking {
            assertThrows<IllegalArgumentException> {
                kredsRedisService.setValue("", "value", 5U)
            }

            assertThrows<IllegalArgumentException> {
                kredsRedisService.setValue("key", "", 5U)
            }
        }
    }

    @Test
    fun `should not get value`() {
        runBlocking {
            org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
                kredsRedisService.getValue("")
            }
        }
    }

    @Test
    fun `should delete value`() = runBlocking {
        val pair = Pair("data#3", "value#3")
        kredsRedisService.setValue(pair.first, pair.second)
        assertEquals(pair.second, kredsRedisService.getValue(pair.first))
        kredsRedisService.deleteValue(pair.first)
        assertNull(kredsRedisService.getValue(pair.first))
    }

    @Test
    fun `should not delete value`() {
        runBlocking {
            assertThrows<IllegalArgumentException> {
                kredsRedisService.deleteValue("")
            }

            assertThrows<NotFoundException> {
                kredsRedisService.deleteValue("nodata")
            }
        }
    }

}