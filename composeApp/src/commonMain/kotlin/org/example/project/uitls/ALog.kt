package org.example.project.uitls

enum class LogLevel { DEBUG, INFO, WARN, ERROR }

expect object ALog {
    fun log(tag: String, message: String, level: LogLevel = LogLevel.DEBUG)
}