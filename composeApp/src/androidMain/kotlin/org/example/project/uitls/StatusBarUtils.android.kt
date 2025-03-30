package org.example.project.uitls

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build

actual object StatusBarUtils {
    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    actual fun getStatusBarHeight(): Int {
        val res = Resources.getSystem()
        var height = 0.0f
        val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            height = res.getDimensionPixelSize(resourceId).toFloat()
        }
        return px2dip(height)
    }

    fun px2dip(pxValue: Float): Int {
        val scale =Resources.getSystem().displayMetrics.density
        return (pxValue / (if (scale <= 0.0f) 1.0f else scale) + 0.5f).toInt()
    }
}