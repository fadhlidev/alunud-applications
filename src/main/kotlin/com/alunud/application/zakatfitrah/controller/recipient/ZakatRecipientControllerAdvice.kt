package com.alunud.application.zakatfitrah.controller.recipient

import com.alunud.common.BaseControllerAdvice
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackageClasses = [ZakatRecipientController::class])
class ZakatRecipientControllerAdvice : BaseControllerAdvice(
    createError = "FAILED_ADD_ZAKAT_FITRAH_EDITION_RECIPIENT",
    readError = "FAILED_GET_ZAKAT_FITRAH_EDITION_RECIPIENT",
    updateError = "FAILED_UPDATE_ZAKAT_FITRAH_EDITION_RECIPIENT",
    deleteError = "FAILED_DELETE_ZAKAT_FITRAH_EDITION_RECIPIENT"
)