package com.alunud.application.user.response

import com.alunud.application.user.entity.User
import java.util.*

data class UserResponse(
    val id: UUID,
    val username: String,
    val email: String?,
    val roles: List<String>
)

/**
 * Return response object of User
 */
fun User.response(mask: Boolean = true) = UserResponse(
    id = this.id,
    username = this.username,
    email = if (mask) maskEmail(this.email) else this.email,
    roles = this.roles.map { it.name }
)

fun maskEmail(email: String?): String? {
    if (email == null) return null
    val parts = email.split("@")
    val username = parts[0]
    val domain = parts[1]

    val maskedUsername = if (username.length > 1) {
        "${username[0]}${"*".repeat(username.length - 2)}${username[username.length - 1]}"
    } else {
        username
    }

    return "$maskedUsername@$domain"
}
