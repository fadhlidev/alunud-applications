package com.alunud.application.zakatfitrah.dto

import com.alunud.annotation.validator.MinDoubleOrNull
import com.alunud.annotation.validator.MinLongOrNull
import com.alunud.annotation.validator.NotBlankOrNull
import jakarta.annotation.Nullable
import jakarta.validation.constraints.AssertTrue

data class UpdateZakatApplicantDto(
    @field:Nullable
    @field:NotBlankOrNull(message = "Institution name cant be empty")
    var institutionName: String? = null,

    @field:Nullable
    @field:NotBlankOrNull(message = "Institution address cant be empty")
    var institutionAddress: String? = null,

    @field:Nullable
    @field:MinLongOrNull(value = 0, message = "Received time cannot be less than January 1, 1970")
    var receivedTime: Long? = null,

    @field:Nullable
    @field:MinLongOrNull(value = 0, message = "Given time cannot be less than January 1, 1970")
    var givenTime: Long? = null,

    @field:Nullable
    @field:MinDoubleOrNull(value = 1.0, message = "Minimum amount is 1.0")
    var givenAmount: Double? = null
) {

    @AssertTrue(message = "Given time cannot be less than or equal to received time")
    fun isGivenTimeGreaterThanReceivedTime(): Boolean {
        if (receivedTime != null && givenTime != null) {
            return givenTime!! > receivedTime!!
        }

        return true
    }

}