package org.example.project.imsdk

open class SampleConversationListener: IMConversationListener {
    override fun onNewConversation(conversations: List<IMConversation>) {

    }

    override fun onConversationChanged(conversations: List<IMConversation>) {

    }

    override fun onConversationDeleted(conversations: List<String>) {

    }

    override fun onTotalUnreadMessageCountChanged(totalUnreadCount: Int) {}
}