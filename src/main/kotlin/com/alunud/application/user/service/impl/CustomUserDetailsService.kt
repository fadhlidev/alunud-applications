package com.alunud.application.user.service.impl

import com.alunud.application.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service("userDetailsService")
class CustomUserDetailsService(@Autowired private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        userRepository.findByUsername(username)?.run {
            val authorities = roles.map { SimpleGrantedAuthority(it.name) }
            return User(username, password, authorities)
        }

        return User("", "", mutableListOf())
    }

}