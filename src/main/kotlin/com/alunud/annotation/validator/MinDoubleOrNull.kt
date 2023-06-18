package com.alunud.annotation.validator

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MinDoubleOrNullValidator::class])
annotation class MinDoubleOrNull(
    val value: Double = 0.0,
    val message: String = "Value must be greater than or equal to {value}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class MinDoubleOrNullValidator : ConstraintValidator<MinDoubleOrNull, Double?> {
    private var minValue: Double = 0.0

    override fun initialize(constraintAnnotation: MinDoubleOrNull) {
        minValue = constraintAnnotation.value
    }

    override fun isValid(value: Double?, context: ConstraintValidatorContext): Boolean {
        return value == null || value >= minValue
    }
}
