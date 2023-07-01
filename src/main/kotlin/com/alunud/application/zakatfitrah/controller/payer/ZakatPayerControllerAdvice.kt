package com.alunud.application.zakatfitrah.controller.payer

import com.alunud.common.BaseControllerAdvice
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackageClasses = [ZakatPayerController::class])
class ZakatPayerControllerAdvice : BaseControllerAdvice(
    createError = "FAILED_ADD_ZAKAT_FITRAH_EDITION_PAYER",
    readError = "FAILED_GET_ZAKAT_FITRAH_EDITION_PAYER",
    updateError = "FAILED_UPDATE_ZAKAT_FITRAH_EDITION_PAYER",
    deleteError = "FAILED_DELETE_ZAKAT_FITRAH_EDITION_PAYER"
)