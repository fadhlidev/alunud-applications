package com.alunud.application.zakatfitrah.entity

import jakarta.persistence.*
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.FetchType.LAZY
import java.util.*

@Entity
@Table(name = "zakat_fitrah_editions")
data class ZakatEdition(
    @field:Id
    val id: UUID,

    @field:Column(unique = true, name = "\"year\"")
    var year: Int,

    @field:Column(unique = true)
    var startDate: Long,

    @field:Column(unique = true, nullable = true)
    var endDate: Long?,

    @field:Column(updatable = false)
    val amountPerPerson: Double,

    @field:OneToMany(mappedBy = "zakatEdition", cascade = [ALL], fetch = LAZY)
    val payers: MutableList<ZakatPayer> = mutableListOf(),

    @field:OneToMany(mappedBy = "zakatEdition", cascade = [ALL], fetch = LAZY)
    val applicants: MutableList<ZakatApplicant> = mutableListOf(),

    @field:OneToMany(mappedBy = "zakatEdition", cascade = [ALL], fetch = LAZY)
    val recipients: MutableList<ZakatRecipient> = mutableListOf()
)
