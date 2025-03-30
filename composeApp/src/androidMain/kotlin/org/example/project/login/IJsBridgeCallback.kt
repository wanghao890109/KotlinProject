package org.example.project.login

/**
 * @author ximi
 * @time 2024/7/16 19:06
 */
interface IJsBridgeCallback {
    fun setData(errorCode: Int, ticket: String?, randstr: String?)

    fun close()
}
