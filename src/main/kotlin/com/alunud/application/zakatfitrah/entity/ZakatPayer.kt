package com.alunud.application.zakatfitrah.entity

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import java.util.*

@Entity
@Table(name = "zakat_fitrah_payers")
data class ZakatPayer(
    @field:Id
    val id: UUID,
    val submittedTime: Long,
    val name: String,

    @field:Column(nullable = true)
    val address: String?,
    val totalPeople: Int,
    val totalAmount: Double,
    val excessAmount: Double,
    val lessAmount: Double,
    val excessAmountReturned: Boolean,

    @field:ManyToOne(fetch = LAZY)
    @field:JoinColumn(name = "zakat_id")
    val zakatEdition: ZakatEdition
)