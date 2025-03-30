package org.example.project.uitls

import platform.Foundation.NSLog

actual object ALog {
    actual fun log(tag: String, message: String, level: LogLevel) {
        val logMessage = "[${level.name}] $tag: $message"
        NSLog(logMessage)
    }
}