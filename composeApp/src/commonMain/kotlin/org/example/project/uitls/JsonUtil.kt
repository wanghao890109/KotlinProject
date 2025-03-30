package org.example.project.uitls

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object JsonUtil {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    inline fun <reified T : Any> toJson(obj: T): String = json.encodeToString(obj)
    inline fun <reified T> fromJson(jsonStr: String): T = json.decodeFromString(jsonStr)
}