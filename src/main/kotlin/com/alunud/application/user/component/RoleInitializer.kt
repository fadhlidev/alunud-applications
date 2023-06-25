package com.alunud.application.user.component

import com.alunud.application.user.entity.Role
import com.alunud.application.user.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class RoleInitializer(@Autowired private val roleRepository: RoleRepository) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        initializeRoles()
    }

    private fun initializeRoles() {
        val roles = listOf(
            Role(id = UUID.randomUUID(), name = "ROLE_ADMIN"),
            Role(id = UUID.randomUUID(), name = "ROLE_USER")
        )

        roleRepository.saveAll(roles)
    }

}