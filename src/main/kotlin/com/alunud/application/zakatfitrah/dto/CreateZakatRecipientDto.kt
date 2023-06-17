package com.alunud.application.zakatfitrah.dto

import jakarta.annotation.Nullable
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class CreateZakatRecipientDto(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:Nullable
    @field:NotBlank(message = "Address cant be empty")
    val address: String?,

    @field:Nullable
    @field:Min(value = 0, message = "Given time cannot be less than January 1, 1970")
    val givenTime: Long?,

    @field:Nullable
    @field:Min(value = 1, message = "Minimum amount is 1")
    val givenAmount: Double?
)