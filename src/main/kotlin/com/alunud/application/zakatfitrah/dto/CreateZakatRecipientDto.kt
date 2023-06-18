package com.alunud.application.zakatfitrah.dto

import com.alunud.annotation.validator.MinDoubleOrNull
import com.alunud.annotation.validator.MinLongOrNull
import com.alunud.annotation.validator.NotBlankOrNull
import jakarta.annotation.Nullable
import jakarta.validation.constraints.NotBlank

data class CreateZakatRecipientDto(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:Nullable
    @field:NotBlankOrNull(message = "Address cant be empty")
    val address: String? = null,

    @field:Nullable
    @field:MinLongOrNull(value = 0, message = "Given time cannot be less than January 1, 1970")
    val givenTime: Long? = null,

    @field:Nullable
    @field:MinDoubleOrNull(value = 1.0, message = "Minimum amount is 1.0")
    val givenAmount: Double? = null
)