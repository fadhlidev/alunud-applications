package com.alunud.application.user.service.impl

import com.alunud.application.AlUnudApplication
import com.alunud.application.user.dto.RegisterUserDto
import com.alunud.application.user.entity.User
import com.alunud.application.user.repository.UserRepository
import com.alunud.application.user.response.maskEmail
import com.alunud.application.user.service.UserService
import com.alunud.exception.EntityExistsException
import jakarta.validation.ConstraintViolationException
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
class UserServiceImplTest(
    @Autowired val userService: UserService,
    @Autowired val userRepository: UserRepository,
    @Autowired val passwordEncoder: BCryptPasswordEncoder
) {

    @AfterEach
    fun `clean up user repository`() {
        userRepository.deleteAll()
    }

    @Test
    fun `should register user with email`() {
        val payload = RegisterUserDto(
            username = "fulan",
            email = "fulan@email.com",
            password = "password",
            confirmPassword = "password"
        )

        val result = userService.register(payload)
        assertNotNull(result)
        assertEquals(payload.username, result.username)
        assertEquals(maskEmail(payload.email), result.email)

        val user = userRepository.findByUsername(result.username)
        assertNotNull(user)
        assertEquals(payload.username, user?.username)
        assertEquals(payload.email, user?.email)
        assertTrue { passwordEncoder.matches(payload.password, user?.password) }
    }

    @Test
    fun `should register user without email`() {
        val payload = RegisterUserDto(
            username = "fulan",
            password = "password",
            confirmPassword = "password"
        )

        val result = userService.register(payload)
        assertNotNull(result)
        assertEquals(payload.username, result.username)
        assertNull(result.email)

        val user = userRepository.findByUsername(result.username)
        assertNotNull(user)
        assertEquals(payload.username, user?.username)
        assertNull(user?.email)
        assertTrue { passwordEncoder.matches(payload.password, user?.password) }
    }

    @Test
    fun `should not register user because username already exists`() {
        val user = User(
            id = UUID.randomUUID(),
            username = "fulan",
            email = null,
            password = passwordEncoder.encode("password")
        )

        userRepository.save(user)

        assertNotNull(userRepository.findByUsername(user.username))

        assertThrows<EntityExistsException> {
            val payload = RegisterUserDto(
                username = "fulan",
                email = "fulan@email.com",
                password = "password",
                confirmPassword = "password"
            )

            userService.register(payload)
        }
    }

    @Test
    fun `should not register user because invalid payload`() {
        assertThrows<ConstraintViolationException> {
            val payload = RegisterUserDto(
                username = "",
                email = "fulan@email.com",
                password = "password",
                confirmPassword = "password"
            )

            userService.register(payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = RegisterUserDto(
                username = "fulan",
                email = "",
                password = "password",
                confirmPassword = "password"
            )

            userService.register(payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = RegisterUserDto(
                username = "fulan",
                email = "fulan@email.com",
                password = "",
                confirmPassword = "password"
            )

            userService.register(payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = RegisterUserDto(
                username = "fulan",
                email = "fulan@email.com",
                password = "password",
                confirmPassword = ""
            )

            userService.register(payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = RegisterUserDto(
                username = "fulan",
                email = "fulan@email.com",
                password = "password",
                confirmPassword = "wrong_password"
            )

            userService.register(payload)
        }
    }

}