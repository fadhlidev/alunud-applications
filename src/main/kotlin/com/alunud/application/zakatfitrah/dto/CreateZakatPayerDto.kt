package com.alunud.application.zakatfitrah.dto

import jakarta.annotation.Nullable
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateZakatPayerDto(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:Nullable
    @field:NotBlank(message = "Address cant be empty")
    val address: String? = null,

    @field:NotNull(message = "Total people is required")
    @field:Min(value = 1, message = "Minimum number of people is 1")
    val totalPeople: Int,

    @field:NotNull(message = "Total amount is required")
    @field:Min(value = 1, message = "Minimum amount is 1")
    val totalAmount: Double,

    val excessAmountReturned: Boolean = false
)