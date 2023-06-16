package com.alunud.application.zakatfitrah.service

import com.alunud.application.zakatfitrah.dto.CreateZakatApplicantDto
import com.alunud.application.zakatfitrah.response.ZakatApplicantResponse

interface ZakatApplicantService {
    fun create(year: Int, dto: CreateZakatApplicantDto): ZakatApplicantResponse
}