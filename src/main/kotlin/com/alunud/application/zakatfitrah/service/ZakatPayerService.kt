package com.alunud.application.zakatfitrah.service

import com.alunud.application.zakatfitrah.dto.CreateZakatPayerDto
import com.alunud.application.zakatfitrah.response.ZakatPayerResponse

interface ZakatPayerService {
    fun create(year: Int, dto: CreateZakatPayerDto): ZakatPayerResponse
}