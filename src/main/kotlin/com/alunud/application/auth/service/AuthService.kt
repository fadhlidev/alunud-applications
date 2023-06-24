package com.alunud.application.auth.service

import com.alunud.application.auth.dto.LoginDto
import com.alunud.application.auth.response.AuthResponse

interface AuthService {
    suspend fun login(dto: LoginDto): AuthResponse
}