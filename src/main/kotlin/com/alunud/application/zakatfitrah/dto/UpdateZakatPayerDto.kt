package com.alunud.application.zakatfitrah.dto

import jakarta.annotation.Nullable
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class UpdateZakatPayerDto(
    @field:Nullable
    @field:NotBlank(message = "Name cant be empty")
    val name: String?,

    @field:Nullable
    @field:NotBlank(message = "Address cant be empty")
    val address: String?,

    @field:Nullable
    @field:Min(value = 1, message = "Minimum number of people is 1")
    val totalPeople: Int?,

    @field:Nullable
    @field:Min(value = 1, message = "Minimum amount is 1")
    val totalAmount: Double?,

    @field:Nullable
    val excessAmountReturned: Boolean?
)