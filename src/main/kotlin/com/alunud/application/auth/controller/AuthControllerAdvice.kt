package com.alunud.application.auth.controller

import com.alunud.exception.AuthenticationException
import com.alunud.exception.EntityExistsException
import com.alunud.exception.NotFoundException
import com.alunud.web.JsonResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.alunud.application.auth.controller"])
class AuthControllerAdvice {

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun httpMessageNotReadableExceptionHandler(
        exception: HttpMessageNotReadableException,
        request: HttpServletRequest
    ): JsonResponse {
        return JsonResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = getMessageFromURI(request.requestURI),
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
            message = getMessageFromURI(request.requestURI),
            error = exception.constraintViolations.first().message
        )
    }

    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun authenticationExceptionHandler(exception: AuthenticationException): JsonResponse {
        return JsonResponse(
            status = HttpStatus.UNAUTHORIZED.value(),
            message = "AUTHENTICATION_FAILED",
            error = exception.message
        )
    }

    @ExceptionHandler(EntityExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun entityExistsExceptionHandler(exception: EntityExistsException): JsonResponse {
        return JsonResponse(
            status = HttpStatus.CONFLICT.value(),
            message = "REGISTRATION_FAILED",
            error = exception.message
        )
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFoundExceptionHandler(exception: NotFoundException): JsonResponse {
        return JsonResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = "REGISTRATION_FAILED",
            error = exception.message
        )
    }

    private fun getMessageFromURI(uri: String): String {
        return if (uri.endsWith("login")) "AUTHENTICATION_FAILED" else "REGISTRATION_FAILED"
    }

}