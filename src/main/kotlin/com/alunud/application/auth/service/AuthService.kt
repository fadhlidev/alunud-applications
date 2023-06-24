package com.alunud.application.auth.service

import com.alunud.application.auth.dto.LoginDto
import com.alunud.application.auth.dto.SignupDto
import com.alunud.application.auth.response.AuthResponse

interface AuthService {
    suspend fun login(dto: LoginDto): AuthResponse
    suspend fun logout(token: String)
    suspend fun signup(dto: SignupDto): AuthResponse
}