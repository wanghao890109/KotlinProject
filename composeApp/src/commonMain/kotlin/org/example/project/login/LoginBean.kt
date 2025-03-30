package org.example.project.login

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.Serializable
import org.example.project.store.DataStoreManager

@Serializable
data class LoginSendSmsRequest(
    val phone: String,
//    val countryCode:String = "+86",
    val inviterPhone: String = "",
    val inviterId: String = "",
    val isHuawei: Boolean = false,
    val ticket: String? = null,
    val randstr: String? = null
)

@Serializable
data class LoginSendSmsResponse(
    val reason: String? = null,
    val reasonDesc: String? = null,
    val userAvatar: String? = null,
    val userNickName: String? = null,
    val userId: String? = null,
    val isForever: Int = 0,
    val isOldUser: Boolean? = null,
    val defaultInviteUserId: String? = null,
    val isNewWxUser: Boolean? = null
)

@Serializable
data class LoginResponse(
    var code: Int? = null,
    var message: String? = null,

    var userID: String? = null,
    var userSig: String? = null,
    var phone: String? = null,
    var originPhone: String? = null,
    var mode: String? = null,
    var inviterId: String? = null,

    var token: String? = null,
    var sdkAppId: Int? = 0,
    val authority: List<String>? = null,
    var route: Int? = null,
    var wxTicket: String? = null,

    var nickName: String? = null,

    var userAvatar: String? = null,
    var userNickName: String? = null,
    var reason: String? = null,
    var reasonDesc: String? = null,
    var isForever: Int? = 0
) {

    val isNewUser: Boolean
        get() = mode == "new"
}

@Serializable
data class LoginUser(
    var userAvatar: String = "",
    var userNickName: String = "",
    var userID: String = "",
    var userSig: String = ""
)

fun LoginResponse.toLoginUser(): LoginUser {
    return LoginUser(
        userAvatar = userAvatar ?: "",
        userNickName = userNickName ?: "",
        userID = userID ?: "",
        userSig = userSig ?: ""
    )
}