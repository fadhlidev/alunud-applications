package com.alunud.application.zakatfitrah.dto

import com.alunud.annotation.validator.MinDoubleOrNull
import com.alunud.annotation.validator.MinLongOrNull
import com.alunud.annotation.validator.NotBlankOrNull
import jakarta.annotation.Nullable

data class UpdateZakatRecipientDto(
    @field:Nullable
    @field:NotBlankOrNull(message = "Name cant be empty")
    var name: String? = null,

    @field:Nullable
    @field:NotBlankOrNull(message = "Address cant be empty")
    var address: String? = null,

    @field:Nullable
    @field:MinLongOrNull(value = 0, message = "Given time cannot be less than January 1, 1970")
    var givenTime: Long? = null,

    @field:Nullable
    @field:MinDoubleOrNull(value = 1.0, message = "Minimum amount is 1.0")
    var givenAmount: Double? = null
)