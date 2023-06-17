package com.alunud.application.zakatfitrah.service

import com.alunud.application.zakatfitrah.dto.CreateZakatApplicantDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatApplicantDto
import com.alunud.application.zakatfitrah.response.ZakatApplicantResponse
import java.util.UUID

interface ZakatApplicantService {
    fun create(year: Int, dto: CreateZakatApplicantDto): ZakatApplicantResponse
    fun update(year: Int, id: UUID, dto: UpdateZakatApplicantDto): ZakatApplicantResponse
    fun delete(year: Int, id: UUID)
    fun findAll(year: Int): List<ZakatApplicantResponse>
    fun findOne(year: Int, id: UUID): ZakatApplicantResponse
}