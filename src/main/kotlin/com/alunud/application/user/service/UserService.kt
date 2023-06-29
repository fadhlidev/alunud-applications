package com.alunud.application.user.service

import com.alunud.application.user.dto.ChangeEmailDto
import com.alunud.application.user.dto.ChangePasswordDto
import com.alunud.application.user.dto.ChangeRolesDto
import com.alunud.application.user.dto.RegisterUserDto
import com.alunud.application.user.response.UserResponse

interface UserService {
    fun register(dto: RegisterUserDto): UserResponse
    fun register(block: RegisterUserDto.() -> Unit): UserResponse
    fun findAll(): List<UserResponse>
    fun findOne(username: String): UserResponse
    fun changeEmail(username: String, dto: ChangeEmailDto)
    fun changeRoles(username: String, dto: ChangeRolesDto)
    fun changePassword(username: String, dto: ChangePasswordDto)
    fun delete(username: String)
}