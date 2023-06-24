package com.alunud.util.function

import kotlin.random.Random

fun generateRandomString(length: Int): String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val random = Random(System.currentTimeMillis())
    val randomString = StringBuilder(length)

    repeat(length) {
        val randomIndex = random.nextInt(charset.length)
        randomString.append(charset[randomIndex])
    }

    return randomString.toString()
}