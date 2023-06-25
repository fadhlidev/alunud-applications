package com.alunud.application.user.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "roles")
data class Role(
    @field:Id
    val id: UUID,

    @field:Column(nullable = false, unique = true)
    val name: String,

    @field:ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    val users: MutableList<User> = mutableListOf()
)