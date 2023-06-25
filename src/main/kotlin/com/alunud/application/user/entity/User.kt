package com.alunud.application.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
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
    var password: String,

    @field:ManyToMany
    @field:JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(
            name = "user_username",
            referencedColumnName = "username"
        )],
        inverseJoinColumns = [JoinColumn(
            name = "role_name",
            referencedColumnName = "name"
        )]
    )
    val roles: MutableSet<Role> = mutableSetOf()
)