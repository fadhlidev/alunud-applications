package com.alunud.application

import com.alunud.util.Validators
import jakarta.validation.Validator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class AlUnudApplication {

    @Bean
    fun validators(validator: Validator): Validators {
        return Validators(validator)
    }

}

fun main(args: Array<String>) {
    runApplication<AlUnudApplication>(*args)
}
