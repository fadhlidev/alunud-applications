package com.alunud.application.user.service

import com.alunud.application.user.dto.RegisterUserDto
import com.alunud.application.user.response.UserResponse

interface UserService {
    fun register(dto: RegisterUserDto): UserResponse
    fun findAll(): List<UserResponse>
}