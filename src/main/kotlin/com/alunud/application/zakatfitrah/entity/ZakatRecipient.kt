package com.alunud.application.zakatfitrah.entity

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import java.util.*

@Entity
@Table(name = "zakat_fitrah_recipients")
data class ZakatRecipient(
    @field:Id
    val id: UUID,
    var name: String,

    @field:Column(nullable = true)
    var address: String?,

    @field:Column(nullable = true)
    var givenTime: Long?,

    @field:Column(nullable = true)
    var givenAmount: Double?,

    @field:ManyToOne(fetch = LAZY)
    @field:JoinColumn(name = "zakat_id")
    val zakatEdition: ZakatEdition
)