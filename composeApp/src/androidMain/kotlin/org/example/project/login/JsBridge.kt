package org.example.project.login

import android.webkit.JavascriptInterface

/**
 * @author ximi
 * @time 2024/7/16 19:00
 */
class JsBridge(private val mJsBridgeCallback: IJsBridgeCallback) {
    @JavascriptInterface
    fun setData(errorCode: Int, ticket: String?, randstr: String?) {
        mJsBridgeCallback.setData(errorCode, ticket, randstr)
    }

    @JavascriptInterface
    fun close() {
        mJsBridgeCallback.close()
    }
}
