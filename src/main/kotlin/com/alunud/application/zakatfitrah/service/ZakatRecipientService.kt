package com.alunud.application.zakatfitrah.service

import com.alunud.application.zakatfitrah.dto.CreateZakatRecipientDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatRecipientDto
import com.alunud.application.zakatfitrah.response.ZakatRecipientResponse
import java.util.UUID

interface ZakatRecipientService {
    fun create(year: Int, dto: CreateZakatRecipientDto): ZakatRecipientResponse
    fun create(year: Int, block: CreateZakatRecipientDto.() -> Unit): ZakatRecipientResponse
    fun update(year: Int, id: UUID, dto: UpdateZakatRecipientDto): ZakatRecipientResponse
    fun update(year: Int, id: UUID, block: UpdateZakatRecipientDto.() -> Unit): ZakatRecipientResponse
    fun delete(year: Int, id: UUID)
    fun findAll(year: Int): List<ZakatRecipientResponse>
    fun findOne(year: Int, id: UUID): ZakatRecipientResponse
}