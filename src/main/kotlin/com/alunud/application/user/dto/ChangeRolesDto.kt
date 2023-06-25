package com.alunud.application.user.dto

import com.alunud.annotation.validator.NotEmptyListOrNull

data class ChangeRolesDto(
    @field:NotEmptyListOrNull
    val roles: List<String>
)