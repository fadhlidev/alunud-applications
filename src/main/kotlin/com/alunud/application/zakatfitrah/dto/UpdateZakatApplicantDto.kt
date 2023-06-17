package com.alunud.application.zakatfitrah.dto

import jakarta.annotation.Nullable
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class UpdateZakatApplicantDto(
    @field:Nullable
    @field:NotBlank(message = "Institution cant be empty")
    val institutionName: String?,

    @field:Nullable
    @field:NotBlank(message = "Institution address cant be empty")
    val institutionAddress: String?,

    @field:Nullable
    @field:Min(value = 0, message = "Received time cannot be less than January 1, 1970")
    val receivedTime: Long?,

    @field:Nullable
    @field:Min(value = 0, message = "Given time cannot be less than January 1, 1970")
    val givenTime: Long?,

    @field:Nullable
    @field:Min(value = 1, message = "Minimum amount is 1")
    val givenAmount: Double?
) {

    @AssertTrue(message = "Given time cannot be less than or equal to received time")
    fun isGivenTimeGreaterThanReceivedTime(): Boolean {
        if (receivedTime != null && givenTime != null) {
            return givenTime > receivedTime
        }

        return true
    }

}