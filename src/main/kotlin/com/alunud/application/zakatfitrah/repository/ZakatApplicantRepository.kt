package com.alunud.application.zakatfitrah.repository

import com.alunud.application.zakatfitrah.entity.ZakatApplicant
import com.alunud.application.zakatfitrah.entity.ZakatEdition
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ZakatApplicantRepository : JpaRepository<ZakatApplicant, UUID> {
    fun findByZakatEditionAndId(edition: ZakatEdition, id: UUID): ZakatApplicant?
}