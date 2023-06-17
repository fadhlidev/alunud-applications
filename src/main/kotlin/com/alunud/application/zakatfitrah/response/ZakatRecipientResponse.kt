package com.alunud.application.zakatfitrah.response

import com.alunud.application.zakatfitrah.entity.ZakatRecipient
import java.util.UUID

data class ZakatRecipientResponse (
    val id: UUID,
    val name: String,
    val address: String?,
    val givenTime: Long?,
    val givenAmount: Double?
)

/**
 * Return response object of Zakat Recipient
 */
fun ZakatRecipient.response() = ZakatRecipientResponse(
    id = this.id,
    name = this.name,
    address = this.address,
    givenTime = this.givenTime,
    givenAmount = this.givenAmount
)