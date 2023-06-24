package com.alunud.application.auth.response

import com.alunud.application.user.entity.User
import java.util.*

data class AuthResponse(
    val id: UUID,
    val username: String,
    val email: String?,
    val token: String
)

/**
 * Return response object of Authenticated User
 */
fun User.authenticate(token: String) = AuthResponse(
    id = this.id,
    username = this.username,
    email = this.email,
    token = token
)