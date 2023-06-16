package com.alunud.application.zakatfitrah.response

import com.alunud.application.zakatfitrah.entity.ZakatApplicant
import java.util.UUID

data class ZakatApplicantResponse (
    val id: UUID,
    val institutionName: String,
    val institutionAddress: String?,
    val receivedTime: Long,
    val givenTime: Long?,
    val givenAmount: Double?
)

fun ZakatApplicant.response() = ZakatApplicantResponse(
    id = this.id,
    institutionName = this.institutionName,
    institutionAddress = this.institutionAddress,
    receivedTime = this.receivedTime,
    givenTime = this.givenTime,
    givenAmount = this.givenAmount
)