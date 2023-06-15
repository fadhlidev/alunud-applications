package com.alunud.application.zakatfitrah.entity

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import java.util.*

@Entity
@Table(name = "zakat_fitrah_applicants")
data class ZakatApplicant(
    @field:Id
    val id: UUID,
    val institutionName: String,

    @field:Column(nullable = true)
    val institutionAddress: String?,

    val receivedTime: Long,

    @field:Column(nullable = true)
    val givenTime: Long?,

    @field:Column(nullable = true)
    val givenAmount: Double?,

    @field:ManyToOne(fetch = LAZY)
    @field:JoinColumn(name = "zakat_id")
    val zakatEdition: ZakatEdition
)