package com.alunud.application.auth.controller

import com.alunud.application.auth.dto.LoginDto
import com.alunud.application.auth.dto.SignupDto
import com.alunud.application.auth.service.AuthService
import com.alunud.web.JsonResponse
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(@Autowired private val authService: AuthService) {

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    suspend fun login(@RequestBody dto: LoginDto, response: HttpServletResponse): JsonResponse {
        val (id, username, email, token) = authService.login(dto)
        setAuthenticationToken(response, token)

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "AUTHENTICATION_SUCCESS",
            data = mapOf(
                "id" to id,
                "username" to username,
                "email" to email
            )
        )
    }


    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun signup(@RequestBody dto: SignupDto, response: HttpServletResponse): JsonResponse {
        val (id, username, email, token) = authService.signup(dto)
        setAuthenticationToken(response, token)

        return JsonResponse(
            status = HttpStatus.CREATED.value(),
            message = "REGISTRATION_SUCCESS",
            data = mapOf(
                "id" to id,
                "username" to username,
                "email" to email
            )
        )
    }

    private fun setAuthenticationToken(response: HttpServletResponse, token: String) {
        val cookie = Cookie("token", token)
        cookie.maxAge = 604800
        cookie.isHttpOnly = true
        cookie.path = "/api"
        response.addCookie(cookie)
    }

}