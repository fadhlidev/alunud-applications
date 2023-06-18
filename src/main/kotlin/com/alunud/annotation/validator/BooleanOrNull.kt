package com.alunud.annotation.validator

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [BooleanOrNullValidator::class])
annotation class BooleanOrNull(
    val message: String = "Value must be true, false, or null",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class BooleanOrNullValidator : ConstraintValidator<BooleanOrNull, Boolean?> {
    override fun isValid(value: Boolean?, context: ConstraintValidatorContext): Boolean {
        return value == null || value is Boolean
    }
}