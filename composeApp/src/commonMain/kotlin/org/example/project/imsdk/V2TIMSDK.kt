package org.example.project.imsdk

expect class V2TIMManager() {
    fun initSDK(appID: Int)
    fun login(userID: String, userSig: String, callback: IMCallback)
    fun logout(callback: IMCallback)
    fun getUsersInfo(userID: String, callback: TCallback<IMUser>)
}

expect class V2TIMMessageManager() {
    fun getMessageList(conversationID: String, count: Int, lastMessageId: String?, callback: TCallback<List<IMMessage>>)
    fun sendTextMessage(text: String, userID: String, callback: TCallback<IMMessage>)
    fun addAdvancedMsgListener(listener: IMMessageListener)
    fun removeAdvancedMsgListener(listener: IMMessageListener)
}

expect class V2TIMConversationManager() {

    fun getConversationList(callback: TCallback<List<IMConversation>>)
    fun addConversationListener(listener: IMConversationListener)
    fun removeConversationListener(listener: IMConversationListener)
    fun cleanConversationUnreadMessageCount(conversationID: String)
    fun getUnreadMessageCount(callback: TCallback<Int>)
}

expect class RelationshipManager() {
    fun getFriendList(callback: TCallback<List<IMUser>>)
}