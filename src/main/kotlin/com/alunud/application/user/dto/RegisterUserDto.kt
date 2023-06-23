package com.alunud.application.user.dto

import com.alunud.annotation.validator.NotBlankOrNull
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class RegisterUserDto(
    @field:NotBlank(message = "Username is required")
    val username: String,

    @field:NotBlankOrNull(message = "Email cant be empty")
    @field:Email(message = "Please provide a valid email address")
    val email: String? = null,

    @field:NotBlank(message = "Password is required")
    val password: String,

    @field:NotBlank(message = "Confirm password is required")
    val confirmPassword: String
) {

    @AssertTrue(message = "Passwords do not match")
    fun isPasswordMatching(): Boolean {
        return password == confirmPassword
    }

}