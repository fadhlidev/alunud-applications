package com.alunud.application.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @field:Id
    val id: UUID,

    @field:Column(unique = true)
    var username: String,
    var email: String?,
    var password: String
)