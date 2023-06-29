package com.alunud.application.zakatfitrah.dto

import com.alunud.annotation.validator.NotBlankOrNull
import jakarta.annotation.Nullable
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateZakatPayerDto(
    @field:NotBlank(message = "Name is required")
    var name: String = "",

    @field:Nullable
    @field:NotBlankOrNull(message = "Address cant be empty")
    var address: String? = null,

    @field:NotNull(message = "Total people is required")
    @field:Min(value = 1, message = "Minimum number of people is 1")
    var totalPeople: Int = 1,

    @field:NotNull(message = "Total amount is required")
    @field:Min(value = 1, message = "Minimum amount is 1")
    var totalAmount: Double = 1.0,

    var excessAmountReturned: Boolean = false
)