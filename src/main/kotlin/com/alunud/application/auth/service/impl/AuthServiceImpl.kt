package com.alunud.application.auth.service.impl

import com.alunud.application.auth.dto.LoginDto
import com.alunud.application.auth.dto.SignupDto
import com.alunud.application.auth.response.AuthResponse
import com.alunud.application.auth.response.authenticate
import com.alunud.application.auth.service.AuthService
import com.alunud.application.persistence.memory.service.RedisService
import com.alunud.application.user.entity.User
import com.alunud.application.user.repository.UserRepository
import com.alunud.application.user.response.response
import com.alunud.exception.AuthenticationException
import com.alunud.exception.EntityExistsException
import com.alunud.util.Validators
import com.alunud.util.function.generateRandomString
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AuthServiceImpl(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordEncoder: BCryptPasswordEncoder,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val redisService: RedisService,
    @Autowired private val validators: Validators
) : AuthService {

    companion object {
        const val TOKEN_LENGTH = 32
        const val MAX_AUTHENTICATED_TIME: ULong = 604800U
    }

    @Transactional
    override suspend fun login(dto: LoginDto): AuthResponse {
        validators.validate(dto)

        val user = userRepository.findByUsername(dto.username)
            ?: throw AuthenticationException("Wrong username or password")

        if (!passwordEncoder.matches(dto.password, user.password)) {
            throw AuthenticationException("Wrong username or password")
        }

        val accessToken = generateAuthenticationToken()
        val credential = user.response()
        redisService.setValue(accessToken, objectMapper.writeValueAsString(credential), MAX_AUTHENTICATED_TIME)

        return user.authenticate(accessToken)
    }

    override suspend fun logout(token: String) {
        if (redisService.isExists(token)) {
            redisService.deleteValue(token)
        }
    }

    @Transactional
    override suspend fun signup(dto: SignupDto): AuthResponse {
        validators.validate(dto)

        userRepository.findByUsername(dto.username)?.run {
            throw EntityExistsException("Username ${dto.username} already taken")
        }

        val user = User(
            id = UUID.randomUUID(),
            username = dto.username,
            email = dto.email,
            password = passwordEncoder.encode(dto.password)
        )

        userRepository.save(user)

        val accessToken = generateAuthenticationToken()
        val credential = user.response()
        redisService.setValue(accessToken, objectMapper.writeValueAsString(credential), MAX_AUTHENTICATED_TIME)

        return user.authenticate(accessToken)
    }

    private suspend fun generateAuthenticationToken(): String {
        val token = generateRandomString(TOKEN_LENGTH)

        if (redisService.isExists(token)) {
            return generateAuthenticationToken()
        }

        return token
    }

}