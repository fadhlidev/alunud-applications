package com.alunud.application.zakatfitrah.controller.edition

import com.alunud.exception.EntityExistsException
import com.alunud.exception.NotFoundException
import com.alunud.web.JsonResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackageClasses = [ZakatEditionController::class])
class ZakatEditionControllerAdvice {

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun httpMessageNotReadableExceptionHandler(
        exception: HttpMessageNotReadableException,
        request: HttpServletRequest
    ): JsonResponse {
        return JsonResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = getMessageFromMethod(request.requestURI),
            error = "Invalid request body format"
        )
    }

    @ExceptionHandler(value = [ConstraintViolationException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun constraintViolationExceptionHandler(
        exception: ConstraintViolationException,
        request: HttpServletRequest
    ): JsonResponse {
        return JsonResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = getMessageFromMethod(request.requestURI),
            error = exception.constraintViolations.first().message
        )
    }

    @ExceptionHandler(EntityExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun entityExistsExceptionHandler(exception: EntityExistsException): JsonResponse {
        return JsonResponse(
            status = HttpStatus.CONFLICT.value(),
            message = "FAILED_CREATE_ZAKAT_FITRAH_EDITION",
            error = exception.message
        )
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFoundExceptionHandler(exception: NotFoundException): JsonResponse {
        return JsonResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = "FAILED_GET_ZAKAT_FITRAH_EDITION",
            error = exception.message
        )
    }

    private fun getMessageFromMethod(method: String): String {
        return when (method) {
            HttpMethod.POST.name() -> "FAILED_CREATE_ZAKAT_FITRAH_EDITION"
            HttpMethod.DELETE.name() -> "FAILED_UPDATE_ZAKAT_FITRAH_EDITION"
            else -> "FAILED_DELETE_ZAKAT_FITRAH_EDITION"
        }
    }

}