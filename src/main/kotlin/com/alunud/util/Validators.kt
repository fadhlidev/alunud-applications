package com.alunud.util

import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator

class Validators(private val validator: Validator) {

    fun validate(any: Any) {
        val result = validator.validate(any)

        if (result.size != 0) {
            throw ConstraintViolationException(result)
        }
    }

    fun invalid(message: String, block: () -> Boolean) {
        if (block()) {
            throw ConstraintViolationException(message, null)
        }
    }

}