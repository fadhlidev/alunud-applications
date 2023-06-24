package com.alunud.application.user.service.impl

import com.alunud.application.AlUnudApplication
import com.alunud.application.user.dto.ChangeEmailDto
import com.alunud.application.user.dto.ChangePasswordDto
import com.alunud.application.user.dto.RegisterUserDto
import com.alunud.application.user.entity.User
import com.alunud.application.user.repository.UserRepository
import com.alunud.application.user.response.maskEmail
import com.alunud.application.user.service.UserService
import com.alunud.exception.EntityExistsException
import com.alunud.exception.NotFoundException
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

    @Test
    fun `should returns list of user`() {
        assertEquals(0, userService.findAll().size)

        userRepository.save(User(
            id = UUID.randomUUID(),
            username = "wahid",
            email = null,
            password = "password"
        ))

        assertEquals(1, userService.findAll().size)

        userRepository.save(User(
            id = UUID.randomUUID(),
            username = "isnaini",
            email = null,
            password = "password"
        ))

        userRepository.save(User(
            id = UUID.randomUUID(),
            username = "salasa",
            email = null,
            password = "password"
        ))

        val  result = userService.findAll()
        assertEquals(3, result.size)
        assertEquals("isnaini", result[0].username)
        assertEquals("salasa", result[1].username)
        assertEquals("wahid", result[2].username)
    }

    @Test
    fun `should find user`() {
        val user = User(
            id = UUID.randomUUID(),
            username = "wahid",
            email = "wahid@email.com",
            password = "password"
        )

        userRepository.save(user)

        assertNotNull(userRepository.findByUsername(user.username))

        val result = userService.findOne(user.username)

        assertNotNull(result)
        assertNotNull(result.id)
        assertEquals(user.username, result.username)
        assertEquals(maskEmail(user.email), result.email)
    }

    @Test
    fun `should not find user`() {
        assertThrows<NotFoundException> {
            userService.findOne("fulan")
        }
    }

    @Test
    fun `should change user email`() {
        val user = User(
            id = UUID.randomUUID(),
            username = "wahid",
            email = "wahid@email.com",
            password = "password"
        )

        userRepository.save(user)
        assertNotNull(userRepository.findByUsername(user.username))

        val payload = ChangeEmailDto("wahiddun@email.com")
        userService.changeEmail(user.username, payload)

        val result = userRepository.findByUsername(user.username)
        assertNotNull(result)
        assertEquals(payload.email, result?.email)
    }

    @Test
    fun `should not change user email because invalid payload`() {
        val user = User(
            id = UUID.randomUUID(),
            username = "wahid",
            email = "wahid@email.com",
            password = "password"
        )

        userRepository.save(user)
        assertNotNull(userRepository.findByUsername(user.username))

        assertThrows<ConstraintViolationException> {
            val payload = ChangeEmailDto("")
            userService.changeEmail(user.username, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = ChangeEmailDto("wahiddun")
            userService.changeEmail(user.username, payload)
        }
    }

    @Test
    fun `should not change user email because user not found`() {
        assertThrows<NotFoundException> {
            val payload = ChangeEmailDto("fulan@email.com")
            userService.changeEmail("fulan", payload)
        }
    }

    @Test
    fun `should change user password`() {
        val user = User(
            id = UUID.randomUUID(),
            username = "wahid",
            email = "wahid@email.com",
            password = "password"
        )

        userRepository.save(user)
        assertNotNull(userRepository.findByUsername(user.username))

        val payload = ChangePasswordDto(
            oldPassword = "password",
            newPassword = "new_password",
            confirmNewPassword = "new_password"
        )

        userService.changePassword(user.username, payload)

        val result = userRepository.findByUsername(user.username)
        assertNotNull(result)
        assertEquals(payload.newPassword, result?.password)
    }

    @Test
    fun `should not change user password because invalid payload`() {
        val user = User(
            id = UUID.randomUUID(),
            username = "wahid",
            email = "wahid@email.com",
            password = "password"
        )

        userRepository.save(user)
        assertNotNull(userRepository.findByUsername(user.username))

        assertThrows<ConstraintViolationException> {
            val payload = ChangePasswordDto(
                oldPassword = "password",
                newPassword = "",
                confirmNewPassword = "new_password"
            )

            userService.changePassword(user.username, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = ChangePasswordDto(
                oldPassword = "password",
                newPassword = "new_password",
                confirmNewPassword = ""
            )

            userService.changePassword(user.username, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = ChangePasswordDto(
                oldPassword = "password",
                newPassword = "new_password",
                confirmNewPassword = "wrong_password"
            )

            userService.changePassword(user.username, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = ChangePasswordDto(
                oldPassword = "wrong_password",
                newPassword = "new_password",
                confirmNewPassword = "new_password"
            )

            userService.changePassword(user.username, payload)
        }
    }

    @Test
    fun `should not change user password because user not found`() {
        assertThrows<NotFoundException> {
            val payload = ChangePasswordDto(
                oldPassword = "password",
                newPassword = "new_password",
                confirmNewPassword = "new_password"
            )

            userService.changePassword("fulan", payload)
        }
    }

}