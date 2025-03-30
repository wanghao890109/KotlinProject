package org.example.project.imsdk

interface IMConversationListener {
    fun onNewConversation(conversations: List<IMConversation>)
    fun onConversationChanged(conversations: List<IMConversation>)
    fun onConversationDeleted(conversations: List<String>)
    fun onTotalUnreadMessageCountChanged(totalUnreadCount: Int)
}