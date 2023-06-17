package com.alunud.application.zakatfitrah.service

import com.alunud.application.zakatfitrah.dto.CreateZakatRecipientDto
import com.alunud.application.zakatfitrah.response.ZakatRecipientResponse

interface ZakatRecipientService {
    fun create(year: Int, dto: CreateZakatRecipientDto): ZakatRecipientResponse
}