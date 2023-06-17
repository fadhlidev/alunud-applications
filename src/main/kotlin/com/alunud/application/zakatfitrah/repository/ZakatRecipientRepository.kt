package com.alunud.application.zakatfitrah.repository

import com.alunud.application.zakatfitrah.entity.ZakatEdition
import com.alunud.application.zakatfitrah.entity.ZakatRecipient
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ZakatRecipientRepository : JpaRepository<ZakatRecipient, UUID> {
    fun findByZakatEditionAndId(edition: ZakatEdition, id: UUID): ZakatRecipient?
    fun findAllByZakatEdition(edition: ZakatEdition, sort: Sort): List<ZakatRecipient>
}