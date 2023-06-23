package com.alunud.application.user.service.impl

import com.alunud.annotation.aspect.Validate
import com.alunud.application.user.dto.RegisterUserDto
import com.alunud.application.user.entity.User
import com.alunud.application.user.repository.UserRepository
import com.alunud.application.user.response.UserResponse
import com.alunud.application.user.response.response
import com.alunud.application.user.service.UserService
import com.alunud.exception.EntityExistsException
import jakarta.transaction.Transactional
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
@Slf4j
class UserServiceImpl(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordEncoder: BCryptPasswordEncoder
) : UserService {

    @Validate
    @Transactional
    override fun register(dto: RegisterUserDto): UserResponse {
        userRepository.findByUsername(dto.username)?.run {
            throw EntityExistsException("User with username ${dto.username} is already exists")
        }

        val user = User(
            id = UUID.randomUUID(),
            username = dto.username,
            email = dto.email,
            password = passwordEncoder.encode(dto.password)
        )

        userRepository.save(user)
        return user.response()
    }

    @Transactional
    override fun findAll(): List<UserResponse> {
        return userRepository.findAll(Sort.by("username")).map { it.response() }
    }

}