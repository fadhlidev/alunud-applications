package com.alunud.application.user.dto

import com.alunud.annotation.validator.NotBlankOrNull
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotBlank

data class ChangePasswordDto(
    @field:NotBlankOrNull(message = "Old password cant be empty")
    val oldPassword: String? = null,

    @field:NotBlank(message = "New password is required")
    val newPassword: String,

    @field:NotBlank(message = "Confirm new password is required")
    val confirmNewPassword: String
) {

    @AssertTrue(message = "New passwords do not match")
    fun isNewPasswordMatching(): Boolean {
        return newPassword == confirmNewPassword
    }

}