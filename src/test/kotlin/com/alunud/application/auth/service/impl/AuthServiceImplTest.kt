package com.alunud.application.auth.service.impl

import com.alunud.application.AlUnudApplication
import com.alunud.application.auth.dto.LoginDto
import com.alunud.application.auth.service.AuthService
import com.alunud.application.persistence.memory.service.RedisService
import com.alunud.application.user.entity.User
import com.alunud.application.user.repository.UserRepository
import com.alunud.application.user.response.UserResponse
import com.alunud.application.user.response.response
import com.alunud.exception.AuthenticationException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.validation.ConstraintViolationException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest(classes = [AlUnudApplication::class])
@ActiveProfiles("test")
class AuthServiceImplTest(
    @Autowired val authService: AuthService,
    @Autowired val redisService: RedisService,
    @Autowired val userRepository: UserRepository,
    @Autowired val passwordEncoder: BCryptPasswordEncoder,
    @Autowired val objectMapper: ObjectMapper
) {

    @AfterEach
    fun `clean up user repository`() {
        userRepository.deleteAll()
    }

    @AfterEach
    fun `clean up redis service`() {
        runBlocking {
            redisService.deleteAllValues()
        }
    }

    @Test
    fun `should logged user in`() = runBlocking {
        val user = User(
            id = UUID.randomUUID(),
            username = "fulan",
            email = "fulan@email.com",
            password = passwordEncoder.encode("password")
        )
        userRepository.save(user)
        assertNotNull(userRepository.findByUsername(user.username))

        val payload = LoginDto("fulan", "password")
        val result = authService.login(payload)
        assertNotNull(result)
        assertEquals(user.id, result.id)
        assertEquals(user.username, result.username)
        assertEquals(user.email, result.email)

        val authenticatedUser = redisService.getValue(result.token)
        assertNotNull(authenticatedUser)
        assertEquals(user.response(), objectMapper.readValue<UserResponse>(authenticatedUser!!))
    }

    @Test
    fun `should logged user in because invalid payload`() {
        val user = User(
            id = UUID.randomUUID(),
            username = "fulan",
            email = "fulan@email.com",
            password = passwordEncoder.encode("password")
        )
        userRepository.save(user)
        assertNotNull(userRepository.findByUsername(user.username))

        assertThrows<ConstraintViolationException> {
            runBlocking {
                val payload = LoginDto("", "password")
                authService.login(payload)
            }
        }

        assertThrows<ConstraintViolationException> {
            runBlocking {
                val payload = LoginDto("fulan", "")
                authService.login(payload)
            }
        }
    }

    @Test
    fun `should logged user in because invalid credentials`() {
        val user = User(
            id = UUID.randomUUID(),
            username = "fulan",
            email = "fulan@email.com",
            password = passwordEncoder.encode("password")
        )
        userRepository.save(user)
        assertNotNull(userRepository.findByUsername(user.username))

        assertThrows<AuthenticationException> {
            runBlocking {
                val payload = LoginDto("wahid", "password")
                authService.login(payload)
            }
        }

        assertThrows<AuthenticationException> {
            runBlocking {
                val payload = LoginDto("fulan", "wrong_password")
                authService.login(payload)
            }
        }
    }

    @Test
    fun `should logged user out`() = runBlocking {
        val user = User(
            id = UUID.randomUUID(),
            username = "fulan",
            email = "fulan@email.com",
            password = passwordEncoder.encode("password")
        )
        userRepository.save(user)
        assertNotNull(userRepository.findByUsername(user.username))

        val result = authService.login(LoginDto("fulan", "password"))
        val authenticatedUser = redisService.getValue(result.token)
        assertNotNull(authenticatedUser)

        authService.logout(result.token)
        assertNull(redisService.getValue(result.token))
    }

}