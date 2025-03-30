package org.example.project

import android.app.Application
import org.example.project.app.AppLifecycle

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppContextHolder.init(this)
        AppLifecycle.onCreate()
    }

}