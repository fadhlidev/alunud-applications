package com.alunud.application.zakatfitrah.controller.recipient

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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice(basePackageClasses = [ZakatRecipientController::class])
class ZakatRecipientControllerAdvice {

    @ExceptionHandler(value = [MethodArgumentTypeMismatchException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun methodArgumentTypeMismatchExceptionExceptionHandler(
        exception: MethodArgumentTypeMismatchException,
        request: HttpServletRequest
    ): JsonResponse {
        return JsonResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = getMessageFromMethod(request.requestURI),
            error = "Invalid type argument parameter"
        )
    }

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
            message = "FAILED_ADD_ZAKAT_FITRAH_EDITION_RECIPIENT",
            error = exception.message
        )
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFoundExceptionHandler(exception: NotFoundException): JsonResponse {
        return JsonResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = "FAILED_GET_ZAKAT_FITRAH_EDITION_RECIPIENT",
            error = exception.message
        )
    }

    private fun getMessageFromMethod(method: String): String {
        return when (method) {
            HttpMethod.POST.name() -> "SUCCESS_ADD_ZAKAT_FITRAH_EDITION_RECIPIENT"
            HttpMethod.PUT.name() -> "FAILED_UPDATE_ZAKAT_FITRAH_EDITION_RECIPIENT"
            else -> "FAILED_DELETE_ZAKAT_FITRAH_EDITION_RECIPIENT"
        }
    }

}