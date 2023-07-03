package com.alunud.application.user.controller

import com.alunud.common.BaseControllerAdvice
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackageClasses = [UserController::class])
class UserControllerAdvice : BaseControllerAdvice(
    createError = "FAILED_REGISTER_NEW_USER",
    readError = "FAILED_GET_USER",
    deleteError = "FAILED_DELETE_USER"
) {

    override fun getMessageFromRequest(request: HttpServletRequest): String {
        return when (request.method) {
            HttpMethod.POST.name() -> createError
            HttpMethod.GET.name() -> readError
            HttpMethod.PATCH.name() -> getUpdateMessage(request)
            else -> deleteError
        }
    }

    private fun getUpdateMessage(request: HttpServletRequest): String {
        if (request.requestURI.endsWith("email")) {
            return "FAILED_UPDATE_USER_EMAIL"
        } else if (request.requestURI.endsWith("roles")) {
            return "FAILED_UPDATE_USER_ROLES"
        } else if (request.requestURI.endsWith("password")) {
            return "FAILED_UPDATE_USER_PASSWORD"
        }

        return "INVALID_UPDATE_OPERATION"
    }

}