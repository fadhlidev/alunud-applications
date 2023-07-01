package com.alunud.application.zakatfitrah.controller.edition

import com.alunud.common.BaseControllerAdvice
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackageClasses = [ZakatEditionController::class])
class ZakatEditionControllerAdvice : BaseControllerAdvice(
    createError = "FAILED_CREATE_ZAKAT_FITRAH_EDITION",
    readError = "FAILED_GET_ZAKAT_FITRAH_EDITION",
    updateError = "FAILED_UPDATE_ZAKAT_FITRAH_EDITION",
    deleteError = "FAILED_DELETE_ZAKAT_FITRAH_EDITION"
)