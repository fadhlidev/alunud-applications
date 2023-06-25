package com.alunud.application.user.service.impl

import com.alunud.annotation.aspect.Validate
import com.alunud.application.user.dto.ChangeEmailDto
import com.alunud.application.user.dto.ChangePasswordDto
import com.alunud.application.user.dto.RegisterUserDto
import com.alunud.application.user.entity.User
import com.alunud.application.user.repository.UserRepository
import com.alunud.application.user.response.UserResponse
import com.alunud.application.user.response.response
import com.alunud.application.user.service.UserService
import com.alunud.exception.EntityExistsException
import com.alunud.exception.NotFoundException
import com.alunud.util.Validators
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Slf4j
@Transactional
class UserServiceImpl(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordEncoder: BCryptPasswordEncoder,
    @Autowired private val validators: Validators
) : UserService {

    @Validate
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

    override fun findAll(): List<UserResponse> {
        return userRepository.findAll(Sort.by("username")).map { it.response() }
    }

    override fun findOne(username: String): UserResponse {
        val user = userRepository.findByUsername(username)
            ?: throw NotFoundException("User with username $username not found")

        return user.response()
    }

    @Validate
    override fun changeEmail(username: String, dto: ChangeEmailDto) {
        val user = userRepository.findByUsername(username)
            ?: throw NotFoundException("User with username $username not found")

        user.apply {
            email = dto.email
        }

        userRepository.save(user)
    }

    @Validate
    override fun changePassword(username: String, dto: ChangePasswordDto) {
        val user = userRepository.findByUsername(username)
            ?: throw NotFoundException("User with username $username not found")

        dto.oldPassword?.run {
            validators.invalid("Wrong password") {
                !passwordEncoder.matches(this, user.password)
            }
        }

        user.apply {
            password = dto.newPassword
        }

        userRepository.save(user)
    }

    override fun delete(username: String) {
        val user = userRepository.findByUsername(username)
            ?: throw NotFoundException("User with username $username not found")

        userRepository.delete(user)
    }

}