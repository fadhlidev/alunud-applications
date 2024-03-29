package com.alunud.application.zakatfitrah.service

import com.alunud.application.zakatfitrah.dto.CreateZakatEditionDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatEditionDto
import com.alunud.application.zakatfitrah.response.ZakatEditionDetailResponse
import com.alunud.application.zakatfitrah.response.ZakatEditionResponse

interface ZakatEditionService {
    fun create(dto: CreateZakatEditionDto): ZakatEditionResponse
    fun create(block: CreateZakatEditionDto.() -> Unit): ZakatEditionResponse
    fun update(year: Int, dto: UpdateZakatEditionDto): ZakatEditionResponse
    fun update(year: Int, block: UpdateZakatEditionDto.() -> Unit): ZakatEditionResponse
    fun delete(year: Int)
    fun findAll(): List<ZakatEditionResponse>
    fun findOne(year: Int): ZakatEditionDetailResponse
}