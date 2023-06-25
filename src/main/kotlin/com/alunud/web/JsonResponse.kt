package com.alunud.web

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import org.springframework.http.HttpStatus

@JsonInclude(Include.NON_NULL)
data class JsonResponse(
    val status: Int = HttpStatus.OK.value(),
    val message: String,
    var data: Any? = null,
    val error: String? = null
)