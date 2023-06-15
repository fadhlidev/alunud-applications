package com.alunud.application.zakatfitrah.response

import com.alunud.application.zakatfitrah.entity.ZakatEdition
import java.util.*

data class ZakatEditionResponse(
    val id: UUID,
    val year: Int,
    val startDate: Long,
    val endDate: Long?,
    val amountPerPerson: Double
)

/**
 * Return response object of Zakat Edition
 */
fun ZakatEdition.response() =
    ZakatEditionResponse(
        id = this.id,
        year = this.year,
        startDate = this.startDate,
        endDate = this.endDate,
        amountPerPerson = this.amountPerPerson
    )