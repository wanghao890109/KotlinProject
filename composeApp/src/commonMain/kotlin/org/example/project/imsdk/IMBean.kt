package org.example.project.imsdk

import kotlinx.serialization.Serializable
import org.example.project.login.LoginUser

@Serializable
data class IMUser(val userID: String, val name: String, val avatar: String)

@Serializable
data class IMMessage(
    val id: String,
    val message: String,
    val time: Long,
    val userId: String,
    val userName: String,
    val userAvatar: String,
    val sender: String
)

@Serializable
data class IMConversation(
    val id: String,
    val lastMessage: IMMessage,
    val userId: String,
    val userName: String,
    val userAvatar: String,
    val unreadCount: Int = 0
)

fun LoginUser?.toIMUser(): IMUser? {
    return this?.let {
        IMUser(
            userID = it.userID,
            name = it.userNickName,
            avatar = it.userAvatar
        )
    }
}