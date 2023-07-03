package com.alunud.application.user.controller

import com.alunud.application.user.dto.ChangeEmailDto
import com.alunud.application.user.dto.ChangePasswordDto
import com.alunud.application.user.dto.ChangeRolesDto
import com.alunud.application.user.dto.RegisterUserDto
import com.alunud.application.user.service.UserService
import com.alunud.exception.ProhibitionException
import com.alunud.util.Validators
import com.alunud.web.JsonResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(@Autowired private val userService: UserService, @Autowired private val validators: Validators) {

    @PostMapping
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody dto: RegisterUserDto): JsonResponse {
        val result = userService.register(dto)

        return JsonResponse(
            status = HttpStatus.CREATED.value(),
            message = "SUCCESS_REGISTER_NEW_USER",
            data = result
        )
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAll(): JsonResponse {
        val result = userService.findAll()

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "SUCCESS_GET_USER_LIST",
            data = result
        )
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    fun findOne(@PathVariable username: String): JsonResponse {
        val result = userService.findOne(username)

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "SUCCESS_GET_USER",
            data = result
        )
    }

    @PatchMapping("/{username}/email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeEmail(@PathVariable username: String, @RequestBody dto: ChangeEmailDto, currentUser: Authentication) {
        if (currentUser.name != username || currentUser.authorities.none { it == SimpleGrantedAuthority("ROLE_ADMIN") }) {
            throw ProhibitionException("You have no permission to do this operation")
        }

        userService.changeEmail(username, dto)
    }

    @PatchMapping("/{username}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changePassword(
        @PathVariable username: String,
        @RequestBody dto: ChangePasswordDto,
        currentUser: Authentication
    ) {
        if (currentUser.name != username || currentUser.authorities.none { it == SimpleGrantedAuthority("ROLE_ADMIN") }) {
            throw ProhibitionException("You have no permission to do this operation")
        }

        if (currentUser.authorities.none { it == SimpleGrantedAuthority("ROLE_ADMIN") }) {
            validators.invalid("Old password cant be empty") {
                dto.oldPassword?.isBlank() ?: true
            }
        }

        userService.changePassword(username, dto)
    }

    @PatchMapping("/{username}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    fun changeRoles(@PathVariable username: String, @RequestBody dto: ChangeRolesDto, currentUser: Authentication) {
        if (currentUser.name == username) {
            throw ProhibitionException("Not allowed to update roles yourself")
        }

        userService.changeRoles(username, dto)
    }

    @DeleteMapping("/{username}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable username: String, currentUser: Authentication) {
        if (currentUser.name == username) {
            throw ProhibitionException("Not allowed to delete account yourself")
        }

        userService.delete(username)
    }

}