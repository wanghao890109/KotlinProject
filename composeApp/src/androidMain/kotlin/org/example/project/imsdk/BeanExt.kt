package org.example.project.imsdk

import com.tencent.imsdk.relationship.FriendInfo
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.imsdk.v2.V2TIMFriendInfo
import com.tencent.imsdk.v2.V2TIMMessage
import com.tencent.imsdk.v2.V2TIMUserFullInfo

fun V2TIMUserFullInfo?.toIMUser(): IMUser? {
    if (this == null) return null
    return IMUser(userID, nickName, faceUrl)
}

fun V2TIMMessage?.toIMMessage(): IMMessage? {
    if (this == null) return null
    if (elemType == V2TIMMessage.V2TIM_ELEM_TYPE_TEXT) {
        return IMMessage(
            msgID, textElem?.text ?: "", timestamp, userID, nickName, faceUrl, sender
        )
    }
    return null
}

fun List<V2TIMMessage>?.toIMMessageList(): List<IMMessage> {
    val list = mutableListOf<IMMessage>()
    if (this != null) {
        for (message in this) {
            val imMessage = message.toIMMessage()
            if (imMessage != null) {
                list.add(imMessage)
            }
        }
    }
    return list
}

fun V2TIMConversation?.toIMConversation(): IMConversation? {
    if (this == null) return null
    if (type != V2TIMConversation.V2TIM_C2C) {
        return null
    }
    if (userID == null) {
        return null
    }
    val lastMessage = lastMessage?.toIMMessage() ?: return null
    return IMConversation(conversationID, lastMessage, userID, showName, faceUrl ?: "", unreadCount)
}

fun List<V2TIMConversation>?.toIMConversationList(): List<IMConversation> {
    val list = mutableListOf<IMConversation>()
    if (this != null) {
        for (conversation in this) {
            val imConversation = conversation.toIMConversation()
            if (imConversation != null) {
                list.add(imConversation)
            }
        }
    }
    return list
}


fun V2TIMFriendInfo?.toIMUser():IMUser?{
    if (this == null) {
        return null
    }
    return IMUser(userID, userProfile.nickName, userProfile.nickName)
}

fun List<V2TIMFriendInfo>?.toIMUserList():List<IMUser>{
    val list = mutableListOf<IMUser>()
    if (this != null) {
        for (friendInfo in this) {
            val imUser = friendInfo.toIMUser()
            if (imUser != null) {
                list.add(imUser)
            }
        }
    }
    return list
}