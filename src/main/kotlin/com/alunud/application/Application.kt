package com.alunud.application

import com.alunud.aspect.ValidatorAspect
import com.alunud.util.Validators
import jakarta.validation.Validator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
class AlUnudApplication {

    @Bean
    fun validators(validator: Validator): Validators {
        return Validators(validator)
    }

    @Bean
    fun validatorAspect(validators: Validators): ValidatorAspect {
        return ValidatorAspect(validators)
    }

}

fun main(args: Array<String>) {
    runApplication<AlUnudApplication>(*args)
}
