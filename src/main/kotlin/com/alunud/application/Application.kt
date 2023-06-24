package com.alunud.application

import com.alunud.application.persistence.memory.service.RedisService
import com.alunud.application.persistence.memory.service.impl.InMemoryRedisService
import com.alunud.application.persistence.memory.service.impl.KredsRedisService
import com.alunud.aspect.ValidatorAspect
import com.alunud.util.Validators
import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.newClient
import jakarta.validation.Validator
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
@EnableAspectJAutoProxy
class AlUnudApplication {

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

    @Bean
    fun redisService(@Value("\${redis.endpoint}") redisEndpoint: String): RedisService {
        return if (redisEndpoint.isBlank()) {
            InMemoryRedisService()
        } else {
            val client = newClient(Endpoint.from(redisEndpoint))
            KredsRedisService(client)
        }
    }

}

fun main(args: Array<String>) {
    runApplication<AlUnudApplication>(*args)
}
