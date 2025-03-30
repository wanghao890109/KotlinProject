package org.example.project.imsdk

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.example.project.login.LoginManager
import org.example.project.login.LoginUser
import org.example.project.uitls.ALog

object IMSDK {

    val v2TIMManager: V2TIMManager by lazy {
        V2TIMManager()
    }

    val v2TIMConversationManager: V2TIMConversationManager by lazy {
        V2TIMConversationManager()
    }

    val v2TIMMessageManager: V2TIMMessageManager by lazy {
        V2TIMMessageManager()
    }

    val relationshipManager: RelationshipManager by lazy {
        RelationshipManager()
    }

    fun init() {
        LoginManager.addLoginListener(object : LoginManager.LoginListener {
            override fun onLogin(loginUser: LoginUser) {
                login(loginUser.userID, loginUser.userSig)
            }

            override fun onLogout() {
                logout()
            }
        })
        GlobalScope.launch {
            delay(100)
            if (LoginManager.isLogin()) {
                login(LoginManager.getUserId(), LoginManager.getUserSigg())
            }
        }
    }

    private fun login(userId: String?, userSig: String?) {
        v2TIMManager.initSDK(1600003950)
        v2TIMManager.login(userId ?: return, userSig ?: return, object : IMCallback {
            override fun onSuccess() {
                ALog.log("IMSDK", "login succeed")
            }

            override fun onError(code: Int, desc: String?) {
                ALog.log("IMSDK", "login error $code $desc")
            }
        })
    }

    fun logout() {
        v2TIMManager.logout(object : IMCallback {
            override fun onSuccess() {

            }

            override fun onError(code: Int, desc: String?) {

            }
        })
    }
}