package com.alunud.application.zakatfitrah.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class CreateZakatEditionDto(
    @field:NotNull(message = "Year is required")
    @field:Min(value = 1970, message = "Year cannot be less than 1970")
    val year: Int,

    @field:NotNull(message = "Start date is required")
    @field:Min(value = 0, message = "Start date cannot be less than January 1, 1970")
    val startDate: Long,

    @field:NotNull(message = "Amount is required")
    @field:DecimalMin(value = "2.5", message = "The minimum amount allowed is 2.5")
    val amountPerPerson: Double
)