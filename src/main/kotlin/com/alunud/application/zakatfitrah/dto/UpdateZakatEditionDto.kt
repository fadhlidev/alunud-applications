package com.alunud.application.zakatfitrah.dto

import jakarta.annotation.Nullable
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Min

data class UpdateZakatEditionDto(
    @field:Nullable
    @field:Min(value = 0, message = "Start date cannot be less than January 1, 1970")
    val startDate: Long?,

    @field:Nullable
    @field:Min(value = 0, message = "End date cannot be less than January 1, 1970")
    val endDate: Long?,
) {

    @AssertTrue(message = "End date cannot be less than or equal to start date")
    fun isEndDateGreaterThanStartDate(): Boolean {
        if (startDate != null && endDate != null) {
            return endDate > startDate
        }

        return true
    }

}