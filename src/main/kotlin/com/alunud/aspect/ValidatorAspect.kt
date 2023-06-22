package com.alunud.aspect

import com.alunud.util.Validators
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

@Aspect
class ValidatorAspect(private val validators: Validators) {

    @Around("@annotation(com.alunud.annotation.aspect.Validate)")
    fun validateDto(joinPoint: ProceedingJoinPoint): Any? {
        val args = joinPoint.args
        val dto = args[args.size - 1]
        validators.validate(dto)
        return joinPoint.proceed()
    }

}