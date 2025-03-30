package org.example.project

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

@SuppressLint("StaticFieldLeak")
object AppContextHolder {
    lateinit var context: Context
        private set

    fun init(app: Application) {
        context = app.applicationContext
    }
}