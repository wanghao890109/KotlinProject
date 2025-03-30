package org.example.project.imsdk

actual class V2TIMManager {
    actual fun initSDK(appID: Int) {
    }

    actual fun login(userID: String, userSig: String, callback: IMCallback) {
    }

    actual fun logout(callback: IMCallback) {
    }

    actual fun getUsersInfo(
        userID: String,
        callback: TCallback<IMUser>
    ) {
    }
}

actual class V2TIMMessageManager {
    actual fun getMessageList(
        conversationID: String,
        count: Int,
        lastMessageId: String?,
        callback: TCallback<List<IMMessage>>
    ) {
    }

    actual fun sendTextMessage(
        text: String,
        userID: String,
        callback: TCallback<IMMessage>
    ) {
    }

    actual fun addAdvancedMsgListener(listener: IMMessageListener) {
    }

    actual fun removeAdvancedMsgListener(listener: IMMessageListener) {
    }
}

actual class V2TIMConversationManager {
    actual fun getConversationList(callback: TCallback<List<IMConversation>>) {
    }

    actual fun addConversationListener(listener: IMConversationListener) {
    }

    actual fun removeConversationListener(listener: IMConversationListener) {
    }

}

actual class RelationshipManager actual constructor() {
    actual fun getFriendList(callback: TCallback<List<IMUser>>) {
    }
}