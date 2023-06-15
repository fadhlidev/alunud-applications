package com.alunud.application.zakatfitrah.response

import com.alunud.application.zakatfitrah.entity.ZakatPayer
import java.util.*

data class ZakatPayerResponse(
    val id: UUID,
    val name: String,
    val address: String?,
    val submittedTime: Long,
    val zakat: ZakatPayerDescription
)

data class ZakatPayerDescription(
    val totalPeople: Int,
    val totalAmount: Double,
    val excessAmount: Double,
    val lessAmount: Double,
    val excessAmountReturned: Boolean
)

fun ZakatPayer.response() = ZakatPayerResponse(
    id = this.id,
    name = this.name,
    address = this.address,
    submittedTime = this.submittedTime,
    zakat = ZakatPayerDescription(
        totalPeople = this.totalPeople,
        totalAmount = this.totalAmount,
        excessAmount = this.excessAmount,
        lessAmount = this.lessAmount,
        excessAmountReturned = this.excessAmountReturned
    )
)