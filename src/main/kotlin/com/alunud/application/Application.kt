package com.alunud.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class AlUnudApplication

fun main(args: Array<String>) {
    runApplication<AlUnudApplication>(*args)
}
