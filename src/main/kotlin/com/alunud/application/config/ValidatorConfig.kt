package com.alunud.application.config

import com.alunud.aspect.ValidatorAspect
import com.alunud.util.Validators
import jakarta.validation.Validator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableAspectJAutoProxy
class ValidatorConfig {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun validators(validator: Validator): Validators {
        return Validators(validator)
    }

    @Bean
    fun validatorAspect(validators: Validators): ValidatorAspect {
        return ValidatorAspect(validators)
    }

}