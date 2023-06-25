package com.alunud.annotation.validator

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NotEmptyListOrNullValidator::class])
annotation class NotEmptyListOrNull(
    val message: String = "List must not be empty",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class NotEmptyListOrNullValidator : ConstraintValidator<NotEmptyListOrNull, List<Any>?> {
    override fun isValid(value: List<Any>?, context: ConstraintValidatorContext): Boolean {
        return value == null || value.isNotEmpty()
    }
}