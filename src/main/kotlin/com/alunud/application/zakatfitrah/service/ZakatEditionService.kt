package com.alunud.application.zakatfitrah.service

import com.alunud.application.zakatfitrah.dto.CreateZakatEditionDto
import com.alunud.application.zakatfitrah.response.ZakatEditionResponse

interface ZakatEditionService {
    fun create(dto: CreateZakatEditionDto): ZakatEditionResponse
}