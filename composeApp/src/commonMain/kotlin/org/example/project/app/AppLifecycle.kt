package org.example.project.app

import org.example.project.imsdk.IMSDK

object AppLifecycle {
    fun onCreate() {
        IMSDK.init()
    }
}