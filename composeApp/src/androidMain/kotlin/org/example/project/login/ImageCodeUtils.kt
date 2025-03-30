package org.example.project.login;

import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

object ImageCodeUtils {


    private var mLoadImageCodeSuccess = false

    fun showImageCode(webView: WebView, callback: IJsBridgeCallback) {

        webView.visibility = View.VISIBLE
        webView.setBackgroundColor(0) // 设置背景色
        webView.background?.alpha = 0 // 设置填充透明度 范围：0-255
        webView.addJavascriptInterface(JsBridge(object : IJsBridgeCallback {
            override fun setData(errorCode: Int, ticket: String?, randstr: String?) {
                webView.post(Runnable {
                    webView.visibility = View.GONE
                    if (errorCode == 0) {
                        callback.setData(errorCode, ticket, randstr)
                    } else {
//                        val error = webView.context.getString(R.string.load_image_code_error)
//                        ToastUtils.showLong(String.format(error, errorCode))
                    }
                })
            }

            override fun close() {
                webView.post {
                    webView.visibility = View.GONE
                }
            }
        }), "jsBridge")
        val webSettings: WebSettings = webView.settings
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        // 禁用缓存
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        // 开启js支持
        webSettings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                mLoadImageCodeSuccess = true
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        webView.loadUrl("file:///android_asset/login_image_code.html")
    }
}