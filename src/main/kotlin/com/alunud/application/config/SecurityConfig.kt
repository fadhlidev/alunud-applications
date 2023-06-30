package com.alunud.application.config

import com.alunud.application.auth.filter.TokenAuthenticationFilter
import com.alunud.web.JsonResponse
import com.google.gson.Gson
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class SecurityConfig {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        tokenAuthenticationFilter: TokenAuthenticationFilter
    ): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .cors { it.disable() }
            .exceptionHandling {
                it.accessDeniedHandler { _, response, _ ->
                    val jsonResponse = JsonResponse(
                        status = HttpStatus.FORBIDDEN.value(),
                        message = "FORBIDDEN_REQUEST",
                        error = "Could not access resource"
                    )

                    response.contentType = MediaType.APPLICATION_JSON_VALUE
                    response.status = HttpStatus.FORBIDDEN.value()
                    response.writer.write(Gson().toJson(jsonResponse))
                }
            }
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

}