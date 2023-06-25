package com.alunud.application.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "roles")
data class Role(
    @field:Id
    val id: UUID,

    @field:Column(nullable = false, unique = true)
    val name: String,

    @field:ManyToMany(mappedBy = "roles")
    val users: MutableList<User> = mutableListOf()
)