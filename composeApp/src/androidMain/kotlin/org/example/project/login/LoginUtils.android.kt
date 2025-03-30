package org.example.project.login

import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import org.example.project.AppContextHolder


actual class LoginUtils {
    var webView: WebView? = null
    actual fun initImageCodeWebView(): @Composable () -> Unit {
        val view =  @Composable {
            AndroidWebView()
        }
        return view
    }

    actual fun showImageCodeWebView(callBack: ImageCodeCallBack) {
        webView?.let {
            ImageCodeUtils.showImageCode(it, object : IJsBridgeCallback {
                override fun setData(errorCode: Int, ticket: String?, randstr: String?) {
                    callBack.onResult(ticket?:"", randstr?:"")
                }

                override fun close() {

                }

            })
        }
    }

    @Composable
    fun AndroidWebView() {
        AndroidView(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            factory = {
                WebView(AppContextHolder.context).apply {
                    isVisible = false
                }
            }, update = {
                webView= it
            })
    }
}