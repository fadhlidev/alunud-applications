package com.alunud.application.zakatfitrah.entity

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import java.util.*

@Entity
@Table(name = "zakat_fitrah_payers")
data class ZakatPayer(
    @field:Id
    val id: UUID,

    @field:Column(updatable = false)
    val submittedTime: Long,
    var name: String,

    @field:Column(nullable = true)
    var address: String?,
    var totalPeople: Int,
    var totalAmount: Double,
    var excessAmount: Double,
    var lessAmount: Double,
    var excessAmountReturned: Boolean,

    @field:ManyToOne(fetch = LAZY)
    @field:JoinColumn(name = "zakat_id")
    val zakatEdition: ZakatEdition
)