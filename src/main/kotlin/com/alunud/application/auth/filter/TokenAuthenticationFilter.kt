package com.alunud.application.auth.filter

import com.alunud.application.persistence.memory.service.RedisService
import com.alunud.application.user.response.UserResponse
import com.alunud.exception.AuthenticationException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TokenAuthenticationFilter(
    @Autowired private val userDetailsService: UserDetailsService,
    @Autowired private val redisService: RedisService,
    @Autowired private val mapper: ObjectMapper
) :
    OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (isAuthenticationURI(request.requestURI)) {
            return filterChain.doFilter(request, response)
        }

        if (SecurityContextHolder.getContext().authentication != null) {
            return filterChain.doFilter(request, response)
        }

        runBlocking {
            val token = extractTokenFromCookie(request)
                ?: throw AuthenticationException("Could not authenticate user")

            redisService.getValue(token).run {
                if (this == null) {
                    throw AuthenticationException("Could not authenticate user")
                }

                val user = mapper.readValue<UserResponse>(this)
                val userDetails = userDetailsService.loadUserByUsername(user.username)
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                SecurityContextHolder.getContext().authentication = authentication
                filterChain.doFilter(request, response)
            }
        }
    }

    private fun isAuthenticationURI(uri: String): Boolean {
        return uri.startsWith("/api/auth/")
    }

    private fun extractTokenFromCookie(request: HttpServletRequest): String? {
        val cookies = request.cookies
        if (cookies != null) {
            for (cookie in cookies) {
                if (cookie.name == "token") {
                    return cookie.value
                }
            }
        }
        return null
    }

}