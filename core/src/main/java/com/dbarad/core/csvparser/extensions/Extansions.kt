package com.dbarad.core.csvparser.extensions

import com.google.gson.Gson

fun Any.toJson(): String {
    return Gson().toJson(this)
}