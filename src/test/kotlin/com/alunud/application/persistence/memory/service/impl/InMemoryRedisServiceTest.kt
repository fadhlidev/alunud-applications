package com.alunud.application.persistence.memory.service.impl

import com.alunud.exception.NotFoundException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InMemoryRedisServiceTest {

    private val inMemoryRedisService = InMemoryRedisService()

    @AfterEach
    fun `clear persisted values`() = runBlocking {
        inMemoryRedisService.deleteAllValues()
    }

    @Test
    fun `should set and get value`() = runBlocking {
        val pair = Pair("data#1", "value#1")
        inMemoryRedisService.setValue(pair.first, pair.second)
        assertEquals(pair.second, inMemoryRedisService.getValue(pair.first))
    }

    @Test
    fun `should not set value`() {
        runBlocking {
            assertThrows<IllegalArgumentException> {
                inMemoryRedisService.setValue("", "value")
            }

            assertThrows<IllegalArgumentException> {
                inMemoryRedisService.setValue("key", "")
            }
        }
    }

    @Test
    fun `should set and get value with exp`() = runBlocking {
        val pair = Pair("data#2", "value#2")
        inMemoryRedisService.setValue(pair.first, pair.second, 5U)
        assertEquals(pair.second, inMemoryRedisService.getValue(pair.first))
        delay(6_000)
        assertNull(inMemoryRedisService.getValue(pair.first))
    }

    @Test
    fun `should not set value with exp`() {
        runBlocking {
            assertThrows<IllegalArgumentException> {
                inMemoryRedisService.setValue("", "value", 5U)
            }

            assertThrows<IllegalArgumentException> {
                inMemoryRedisService.setValue("key", "", 5U)
            }
        }
    }

    @Test
    fun `should not get value`() {
        runBlocking {
            assertThrows<IllegalArgumentException> {
                inMemoryRedisService.getValue("")
            }
        }
    }

    @Test
    fun `should delete value`() = runBlocking {
        val pair = Pair("data#3", "value#3")
        inMemoryRedisService.setValue(pair.first, pair.second)
        assertEquals(pair.second, inMemoryRedisService.getValue(pair.first))
        inMemoryRedisService.deleteValue(pair.first)
        assertNull(inMemoryRedisService.getValue(pair.first))
    }

    @Test
    fun `should not delete value`() {
        runBlocking {
            assertThrows<IllegalArgumentException> {
                inMemoryRedisService.deleteValue("")
            }

            assertThrows<NotFoundException> {
                inMemoryRedisService.deleteValue("nodata")
            }
        }
    }

}