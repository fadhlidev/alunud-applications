package com.alunud.application.zakatfitrah.repository

import com.alunud.application.zakatfitrah.entity.ZakatEdition
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ZakatEditionRepository : JpaRepository<ZakatEdition, UUID> {
    fun findByYear(year: Int): ZakatEdition?
}