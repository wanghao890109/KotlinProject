package org.example.project.login

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.example.project.store.DataStoreManager

object LoginManager {

    private var token: String? = null
    private var loginUser: LoginUser? = null
    private val loginListener = mutableListOf<LoginListener>()

    init {
        GlobalScope.launch {
            token = DataStoreManager.getString("token").firstOrNull()
            loginUser = DataStoreManager.getSerializable<LoginUser>("user").firstOrNull()
        }
    }

    fun isLogin(): Boolean {
        if (token?.isNotEmpty() == true
            && loginUser?.userID?.isNotEmpty() == true
        ) {
            return true
        }
        return false
    }

    fun getToken(): String? {
        return token
    }

    fun getUserId(): String? {
        return loginUser?.userID
    }

    fun getUserSigg(): String? {
        return loginUser?.userSig
    }

    fun getUser(): LoginUser? {
        return loginUser
    }

    fun login(loginUser: LoginUser, token: String) {
        this.loginUser = loginUser
        this.token = token
        GlobalScope.launch {
            DataStoreManager.putSerializable("user", loginUser)
            DataStoreManager.putString("token", token)
        }

        loginListener.forEach { it.onLogin(loginUser) }
    }

    fun logout() {
        loginUser = null
        token = null
        GlobalScope.launch {
            DataStoreManager.putString("token", "")
            DataStoreManager.putSerializable("user", "")
        }
        loginListener.forEach { it.onLogout() }
    }

    fun addLoginListener(listener: LoginListener) {
        loginListener.add(listener)
    }

    fun removeLoginListener(listener: LoginListener) {
        loginListener.remove(listener)
    }

    interface LoginListener {
        fun onLogin(loginUser: LoginUser)
        fun onLogout()
    }
}