package com.alunud.application.user.entity

import jakarta.persistence.*
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

    @field:ManyToMany(fetch = FetchType.EAGER)
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