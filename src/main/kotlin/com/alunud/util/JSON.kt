package com.alunud.util

import com.google.gson.Gson

class JSON {

    companion object {
        fun stringify(obj: Any): String {
            return Gson().toJson(obj)
        }

        fun <T> parse(json: String, obj: Class<T>): T {
            return Gson().fromJson(json, obj)
        }
    }

}