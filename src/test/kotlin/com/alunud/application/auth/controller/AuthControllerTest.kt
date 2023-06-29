package com.alunud.application.auth.controller

import com.alunud.application.AlUnudApplication
import com.alunud.application.auth.dto.LoginDto
import com.alunud.application.auth.dto.SignupDto
import com.alunud.application.auth.service.AuthService
import com.alunud.application.persistence.memory.service.RedisService
import com.alunud.application.user.repository.UserRepository
import com.alunud.application.user.response.UserResponse
import com.alunud.application.user.response.maskEmail
import com.alunud.util.JSON
import com.alunud.web.JsonResponse
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.ResourceAccessException
import java.util.*

@SpringBootTest(classes = [AlUnudApplication::class], webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var redisService: RedisService

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
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun `should logged user in`() = runBlocking {
        authService.signup {
            username = "wahid"
            email = "wahid@email.com"
            password = "password"
            confirmPassword = "password"
        }

        val response = requestLogin {
            username = "wahid"
            password = "password"
        }
        val jsonResponse = JSON.parse(response.body as String, JsonResponse::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNull(jsonResponse.error)
        assertEquals(HttpStatus.OK.value(), jsonResponse.status)
        assertEquals("AUTHENTICATION_SUCCESS", jsonResponse.message)
        assertNotNull((jsonResponse.data as Map<*, *>)["id"])
        assertEquals("wahid", (jsonResponse.data as Map<*, *>)["username"])
        assertEquals("wahid@email.com", (jsonResponse.data as Map<*, *>)["email"])

        val token = response.getTokenFromCookie()
        assertNotNull(token)
        val authenticatedUser = redisService.getValue(token!!)
        assertNotNull(authenticatedUser)
        val user = JSON.parse(authenticatedUser!!, UserResponse::class.java)
        assertEquals((jsonResponse.data as Map<*, *>)["id"], user.id.toString())
        assertEquals((jsonResponse.data as Map<*, *>)["username"], user.username)
        assertEquals(maskEmail((jsonResponse.data as Map<*, *>)["email"] as String), user.email)
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun `should logged user in because invalid payload`() {
        run {
            val response = requestLogin {
                username = ""
                password = "password"
            }
            assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        }

        run {
            val response = requestLogin {
                username = "fulan"
                password = ""
            }
            assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        }
    }

    @Test
    fun `should logged user in because invalid credentials`() {
        runBlocking {
            authService.signup {
                username = "wahid"
                email = "wahid@email.com"
                password = "password"
                confirmPassword = "password"
            }
        }

        assertThrows<ResourceAccessException> {
            requestLogin {
                username = "fulan"
                password = "password"
            }
        }

        assertThrows<ResourceAccessException> {
            requestLogin {
                username = "wahid"
                password = "wrong_password"
            }
        }
    }

    @Test
    fun `should signed user up without email`() {
        val response = requestSignup {
            username = "isnaini"
            password = "password"
            confirmPassword = "password"
        }
        val jsonResponse = JSON.parse(response.body as String, JsonResponse::class.java)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNull(jsonResponse.error)
        assertEquals(HttpStatus.CREATED.value(), jsonResponse.status)
        assertEquals("REGISTRATION_SUCCESS", jsonResponse.message)
        assertNotNull((jsonResponse.data as Map<*, *>)["id"])
        assertEquals("isnaini", (jsonResponse.data as Map<*, *>)["username"])
        assertNull((jsonResponse.data as Map<*, *>)["email"])
    }

    @Test
    fun `should signed user up with email`() {
        val response = requestSignup {
            username = "fulan"
            email = "fulan@email"
            password = "password"
            confirmPassword = "password"
        }
        val jsonResponse = JSON.parse(response.body as String, JsonResponse::class.java)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNull(jsonResponse.error)
        assertEquals(HttpStatus.CREATED.value(), jsonResponse.status)
        assertEquals("REGISTRATION_SUCCESS", jsonResponse.message)
        assertNotNull((jsonResponse.data as Map<*, *>)["id"])
        assertEquals("fulan", (jsonResponse.data as Map<*, *>)["username"])
        assertEquals("fulan@email", (jsonResponse.data as Map<*, *>)["email"])
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun `should not signed user up because username already taken`() = runBlocking {
        authService.signup {
            username = "fulan"
            email = "fulan@email.com"
            password = "password"
            confirmPassword = "password"
        }

        run {
            val response = requestLogin {
                username = ""
                password = "password"
            }
            assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        }

        run {
            val response = requestLogin {
                username = "fulan"
                password = ""
            }
            assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        }
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun `should logged user out`() = runBlocking {
        authService.signup {
            username = "fulan"
            email = "fulan@email.com"
            password = "password"
            confirmPassword = "password"
        }

        val login = requestLogin {
            username = "fulan"
            password = "password"
        }

        val tokenCookie = login.headers[HttpHeaders.SET_COOKIE]
        assertNotNull(tokenCookie)

        val token = login.getTokenFromCookie()
        assertNotNull(token)
        var authenticatedUser = redisService.getValue(token!!)
        assertNotNull(authenticatedUser)

        val logout = requestLogout(tokenCookie!![0])
        assertEquals(HttpStatus.NO_CONTENT, logout.statusCode)

        authenticatedUser = redisService.getValue(token)
        assertNull(authenticatedUser)
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun `should not signed user up because invalid payload`() {
        run {
            val response = requestSignup {
                email = "fulan@email.com"
                password = "password"
                confirmPassword = "password"
            }
            assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        }

        run {
            val response = requestSignup {
                username = "hi"
                email = "isnaini@email.com"
                password = "password"
                confirmPassword = "password"
            }
            assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        }

        run {
            val response = requestSignup {
                username = "fulan"
                email = ""
                password = "password"
                confirmPassword = "password"
            }
            assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        }

        run {
            val response = requestSignup {
                username = "fulan"
                email = "fulan@email.com"
                confirmPassword = "password"
            }
            assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        }

        run {
            val response = requestSignup {
                username = "fulan"
                email = "fulan@email.com"
                password = "password"
            }
            assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        }

        run {
            val response = requestSignup {
                username = "fulan"
                email = "fulan@email.com"
                password = "pwd"
                confirmPassword = "pwd"
            }
            assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        }

        run {
            val response = requestSignup {
                username = "fulan"
                email = "fulan@email.com"
                password = "password"
                confirmPassword = "wrong_password"
            }
            assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        }
    }

    private fun requestLogin(block: LoginDto.() -> Unit): ResponseEntity<String> {
        val url = "http://localhost:$port/api/auth/login"
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON

        val payload = LoginDto().apply(block)
        val requestEntity = HttpEntity(JSON.stringify(payload), requestHeaders)

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String::class.java)
    }

    private fun requestLogout(tokenCookie: String): ResponseEntity<Void> {
        val url = "http://localhost:$port/api/auth/logout"
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON
        requestHeaders.set("Cookie", tokenCookie)

        val requestEntity = HttpEntity(null, requestHeaders)

        return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void::class.java)
    }

    private fun requestSignup(block: SignupDto.() -> Unit): ResponseEntity<String> {
        val url = "http://localhost:$port/api/auth/signup"
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON

        val payload = SignupDto().apply(block)
        val requestEntity = HttpEntity(JSON.stringify(payload), requestHeaders)

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String::class.java)
    }

    private fun ResponseEntity<String>.getTokenFromCookie(): String? {
        val cookies = this.headers[HttpHeaders.SET_COOKIE] ?: return null
        val tokenCookie = cookies[0]
        val cookieSplit = tokenCookie.split(";")
        val tokenAttr = cookieSplit[0]
        return tokenAttr.subSequence(6..tokenAttr.lastIndex).toString()
    }

}