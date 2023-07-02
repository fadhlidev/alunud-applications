package com.alunud.common

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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

open class BaseControllerAdvice(
    protected val createError: String = "FAILED_TO_CREATE_ENTITY",
    protected val readError: String = "FAILED_TO_GET_ENTITY",
    protected val updateError: String = "FAILED_TO_UPDATE_ENTITY",
    protected val deleteError: String = "FAILED_TO_DELETE_ENTITY"
) {

    @ExceptionHandler(value = [MethodArgumentTypeMismatchException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun methodArgumentTypeMismatchExceptionExceptionHandler(
        exception: MethodArgumentTypeMismatchException,
        request: HttpServletRequest
    ): JsonResponse {
        return JsonResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = getMessageFromRequest(request),
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
            message = getMessageFromRequest(request),
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
            message = getMessageFromRequest(request),
            error = exception.constraintViolations.first().message
        )
    }

    @ExceptionHandler(EntityExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun entityExistsExceptionHandler(exception: EntityExistsException): JsonResponse {
        return JsonResponse(
            status = HttpStatus.CONFLICT.value(),
            message = createError,
            error = exception.message
        )
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFoundExceptionHandler(
        exception: NotFoundException,
        request: HttpServletRequest
    ): JsonResponse {
        return JsonResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = getMessageFromRequest(request),
            error = exception.message
        )
    }

    open fun getMessageFromRequest(request: HttpServletRequest): String {
        return when (request.method) {
            HttpMethod.POST.name() -> createError
            HttpMethod.GET.name() -> readError
            HttpMethod.PUT.name() -> updateError
            else -> deleteError
        }
    }

}