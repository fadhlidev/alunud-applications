package com.alunud.application.zakatfitrah.controller.applicant

import com.alunud.common.BaseControllerAdvice
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackageClasses = [ZakatApplicantController::class])
class ZakatApplicantControllerAdvice : BaseControllerAdvice(
    createError = "FAILED_ADD_ZAKAT_FITRAH_EDITION_APPLICANT",
    readError = "FAILED_GET_ZAKAT_FITRAH_EDITION_APPLICANT",
    updateError = "FAILED_UPDATE_ZAKAT_FITRAH_EDITION_APPLICANT",
    deleteError = "FAILED_DELETE_ZAKAT_FITRAH_EDITION_APPLICANT"
)