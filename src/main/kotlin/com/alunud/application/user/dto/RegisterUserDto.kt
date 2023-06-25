package com.alunud.application.user.dto

import com.alunud.annotation.validator.NotBlankOrNull
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class RegisterUserDto(
    @field:NotBlank(message = "Username is required")
    @field:Length(min = 3, message = "Username must be at least 3 characters")
    val username: String,

    @field:NotBlankOrNull(message = "Email cant be empty")
    @field:Email(message = "Please provide a valid email address")
    val email: String? = null,

    @field:NotBlank(message = "Password is required")
    @field:Length(min = 8, message = "Password must be at least 6 characters")
    val password: String,

    @field:NotBlank(message = "Confirm password is required")
    val confirmPassword: String,

    val roles: MutableSet<String> = mutableSetOf("ROLE_USER")
) {

    @AssertTrue(message = "Passwords do not match")
    fun isPasswordMatching(): Boolean {
        return password == confirmPassword
    }

}