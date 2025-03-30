package org.example.project.uitls

import android.util.Log

actual object ALog {
    actual fun log(tag: String, message: String, level: LogLevel) {
        when (level) {
            LogLevel.DEBUG -> Log.d(tag, message)
            LogLevel.INFO -> Log.i(tag, message)
            LogLevel.WARN -> Log.w(tag, message)
            LogLevel.ERROR -> Log.e(tag, message)
        }
    }
}