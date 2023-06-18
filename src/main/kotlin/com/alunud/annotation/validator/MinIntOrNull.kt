package com.alunud.annotation.validator

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MinIntOrNullValidator::class])
annotation class MinIntOrNull(
    val value: Int = 0,
    val message: String = "Value must be greater than or equal to {value}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class MinIntOrNullValidator : ConstraintValidator<MinIntOrNull, Int?> {
    private var minValue: Int = 0

    override fun initialize(constraintAnnotation: MinIntOrNull) {
        minValue = constraintAnnotation.value
    }

    override fun isValid(value: Int?, context: ConstraintValidatorContext): Boolean {
        return value == null || value >= minValue
    }
}
