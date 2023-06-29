package com.alunud.application.auth.dto

import jakarta.validation.constraints.NotBlank

data class LoginDto(
    @field:NotBlank(message = "Username is required")
    var username: String = "",

    @field:NotBlank(message = "Password is required")
    var password: String = ""
)