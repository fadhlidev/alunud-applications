package com.alunud.application.zakatfitrah.service

import com.alunud.application.zakatfitrah.dto.CreateZakatPayerDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatPayerDto
import com.alunud.application.zakatfitrah.response.ZakatPayerResponse
import java.util.UUID

interface ZakatPayerService {
    fun create(year: Int, dto: CreateZakatPayerDto): ZakatPayerResponse
    fun update(year: Int, id: UUID, dto: UpdateZakatPayerDto): ZakatPayerResponse
}