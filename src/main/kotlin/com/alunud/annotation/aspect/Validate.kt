package com.alunud.annotation.aspect

/**
 * Annotation to indicate that the DTO passed as the last parameter of a method needs to be validated using Jakarta Validators.
 * If the DTO is not valid, a ConstraintViolationException will be thrown.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Validate
