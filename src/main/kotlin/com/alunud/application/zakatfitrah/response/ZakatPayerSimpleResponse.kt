package com.alunud.application.zakatfitrah.response

import com.alunud.application.zakatfitrah.entity.ZakatPayer
import java.util.*

data class ZakatPayerSimpleResponse (
    val id: UUID,
    val name: String,
    val submittedTime: Long,
    val totalPeople: Int,
    val totalAmount: Double
)

/**
 * Return simple response object of Zakat Payer
 */
fun ZakatPayer.simpleResponse() = ZakatPayerSimpleResponse(
    id = this.id,
    name = this.name,
    submittedTime = this.submittedTime,
    totalPeople = this.totalPeople,
    totalAmount = this.totalAmount
)