package com.alunud.application.zakatfitrah.service

import com.alunud.application.zakatfitrah.dto.CreateZakatEditionDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatEditionDto
import com.alunud.application.zakatfitrah.response.ZakatEditionResponse

interface ZakatEditionService {
    fun create(dto: CreateZakatEditionDto): ZakatEditionResponse
    fun update(year: Int, dto: UpdateZakatEditionDto): ZakatEditionResponse
    fun delete(year: Int)
}