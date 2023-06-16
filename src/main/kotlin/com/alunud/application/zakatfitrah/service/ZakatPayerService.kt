package com.alunud.application.zakatfitrah.service

import com.alunud.application.zakatfitrah.dto.CreateZakatPayerDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatPayerDto
import com.alunud.application.zakatfitrah.response.ZakatPayerResponse
import com.alunud.application.zakatfitrah.response.ZakatPayerSimpleResponse
import java.util.*

interface ZakatPayerService {
    fun create(year: Int, dto: CreateZakatPayerDto): ZakatPayerResponse
    fun update(year: Int, id: UUID, dto: UpdateZakatPayerDto): ZakatPayerResponse
    fun delete(year: Int, id: UUID)
    fun findAll(year: Int): List<ZakatPayerSimpleResponse>
    fun findOne(year: Int, id: UUID): ZakatPayerResponse
}