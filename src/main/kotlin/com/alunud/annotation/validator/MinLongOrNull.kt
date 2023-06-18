package com.alunud.annotation.validator

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MinLongOrNullValidator::class])
annotation class MinLongOrNull(
    val value: Long = 0,
    val message: String = "Value must be greater than or equal to {value}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class MinLongOrNullValidator : ConstraintValidator<MinLongOrNull, Long?> {
    private var minValue: Long = 0

    override fun initialize(constraintAnnotation: MinLongOrNull) {
        minValue = constraintAnnotation.value
    }

    override fun isValid(value: Long?, context: ConstraintValidatorContext): Boolean {
        return value == null || value >= minValue
    }
}
