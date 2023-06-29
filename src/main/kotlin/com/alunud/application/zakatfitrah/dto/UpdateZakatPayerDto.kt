package com.alunud.application.zakatfitrah.dto

import com.alunud.annotation.validator.*
import jakarta.annotation.Nullable

data class UpdateZakatPayerDto(
    @field:Nullable
    @field:NotBlankOrNull(message = "Name cant be empty")
    var name: String? = null,

    @field:Nullable
    @field:NotBlankOrNull(message = "Address cant be empty")
    var address: String? = null,

    @field:Nullable
    @field:MinIntOrNull(value = 1, message = "Minimum number of people is 1")
    var totalPeople: Int? = null,

    @field:Nullable
    @field:MinDoubleOrNull(value = 1.0, message = "Minimum amount is 1.0")
    var totalAmount: Double? = null,

    @field:Nullable
    @field:BooleanOrNull(message = "Excess amount returned must be a boolean")
    var excessAmountReturned: Boolean? = null
)