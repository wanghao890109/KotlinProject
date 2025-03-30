package org.example.project.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.project.svga.SvgaImageView

expect class LoginUtils() {
    fun initImageCodeWebView(): @Composable () -> Unit
    fun showImageCodeWebView(callBack: ImageCodeCallBack)
}

interface ImageCodeCallBack {
    fun onResult(ticket: String, randstr: String)
}
