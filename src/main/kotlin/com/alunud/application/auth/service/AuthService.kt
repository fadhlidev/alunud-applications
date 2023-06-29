package com.alunud.application.auth.service

import com.alunud.application.auth.dto.LoginDto
import com.alunud.application.auth.dto.SignupDto
import com.alunud.application.auth.response.AuthResponse

interface AuthService {
    suspend fun login(dto: LoginDto): AuthResponse
    suspend fun login(block: LoginDto.() -> Unit): AuthResponse
    suspend fun logout(token: String): Boolean
    suspend fun signup(dto: SignupDto): AuthResponse
    suspend fun signup(block: SignupDto.() -> Unit): AuthResponse
}