package com.alunud.application.user.service

import com.alunud.application.user.dto.ChangeEmailDto
import com.alunud.application.user.dto.RegisterUserDto
import com.alunud.application.user.response.UserResponse

interface UserService {
    fun register(dto: RegisterUserDto): UserResponse
    fun findAll(): List<UserResponse>
    fun findOne(username: String): UserResponse
    fun changeEmail(username: String, dto: ChangeEmailDto)
}