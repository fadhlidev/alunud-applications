package com.alunud.application.zakatfitrah.dto

import com.alunud.annotation.validator.MinLongOrNull
import jakarta.annotation.Nullable
import jakarta.validation.constraints.AssertTrue

data class UpdateZakatEditionDto(
    @field:Nullable
    @field:MinLongOrNull(value = 0, message = "Start date cannot be less than January 1, 1970")
    var startDate: Long? = null,

    @field:Nullable
    @field:MinLongOrNull(value = 0, message = "End date cannot be less than January 1, 1970")
    var endDate: Long? = null,
) {

    @AssertTrue(message = "End date cannot be less than or equal to start date")
    fun isEndDateGreaterThanStartDate(): Boolean {
        if (startDate != null && endDate != null) {
            return endDate!! > startDate!!
        }

        return true
    }

}