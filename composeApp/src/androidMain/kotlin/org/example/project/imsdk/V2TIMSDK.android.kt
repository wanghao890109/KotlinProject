package org.example.project.imsdk

import android.util.Log
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener
import com.tencent.imsdk.v2.V2TIMCallback
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.imsdk.v2.V2TIMConversationListFilter
import com.tencent.imsdk.v2.V2TIMConversationListener
import com.tencent.imsdk.v2.V2TIMConversationResult
import com.tencent.imsdk.v2.V2TIMFriendInfo
import com.tencent.imsdk.v2.V2TIMLogListener
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMMessage
import com.tencent.imsdk.v2.V2TIMMessageListGetOption
import com.tencent.imsdk.v2.V2TIMOfflinePushInfo
import com.tencent.imsdk.v2.V2TIMSDKConfig
import com.tencent.imsdk.v2.V2TIMSendCallback
import com.tencent.imsdk.v2.V2TIMUserFullInfo
import com.tencent.imsdk.v2.V2TIMValueCallback
import org.example.project.AppContextHolder
import java.util.concurrent.CopyOnWriteArrayList


actual class V2TIMManager {
    actual fun initSDK(appID: Int) {
        // 用户操作初始化, 默认已经读过隐私协议
        val config = V2TIMSDKConfig()
        config.logLevel = V2TIMSDKConfig.V2TIM_LOG_DEBUG
        config.logListener = object : V2TIMLogListener() {
            override fun onLog(logLevel: Int, logContent: String) {
                super.onLog(logLevel, logContent)
                Log.i("V2TIMSDKLog", "level: $logLevel; content: $logContent")
            }
        }
        V2TIMManager.getInstance().initSDK(AppContextHolder.context, appID, config)
    }

    actual fun login(userID: String, userSig: String, callback: IMCallback) {
        val loginStatus = V2TIMManager.getInstance().loginStatus
        if (loginStatus != V2TIMManager.V2TIM_STATUS_LOGOUT) {
            return
        }
        V2TIMManager.getInstance().login(userID, userSig, object : V2TIMCallback {
            override fun onError(code: Int, desc: String?) {
                callback.onError(code, desc)
            }

            override fun onSuccess() {
                callback.onSuccess()
            }
        })
    }

    actual fun logout(callback: IMCallback) {
        V2TIMManager.getInstance().logout(object : V2TIMCallback {
            override fun onError(code: Int, desc: String?) {
                callback.onError(code, desc)
            }

            override fun onSuccess() {
                callback.onSuccess()
            }
        })
    }

    actual fun getUsersInfo(userID: String, callback: TCallback<IMUser>) {
        V2TIMManager.getInstance()
            .getUsersInfo(arrayListOf(userID), object : V2TIMValueCallback<List<V2TIMUserFullInfo>> {
                override fun onSuccess(t: List<V2TIMUserFullInfo>?) {
                    t?.firstOrNull()?.toIMUser()?.apply {
                        callback.onSuccess(this)
                    } ?: kotlin.run {
                        callback.onError(-1, "user not found")
                    }
                }

                override fun onError(code: Int, desc: String?) {
                    callback.onError(code, desc)
                }
            })
    }
}

actual class V2TIMMessageManager {

    private var advancedMsgListenerList = CopyOnWriteArrayList<IMMessageListener>()

    init {
        V2TIMManager.getMessageManager().addAdvancedMsgListener(object : V2TIMAdvancedMsgListener() {
            override fun onRecvNewMessage(msg: V2TIMMessage) {
                advancedMsgListenerList.forEach {
                    if (msg.elemType == V2TIMMessage.V2TIM_ELEM_TYPE_TEXT) {
                        it.onRecvNewMessage(msg.toIMMessage())
                    }
                }
            }

            override fun onRecvMessageModified(msg: V2TIMMessage) {
                advancedMsgListenerList.forEach {
                    if (msg.elemType == V2TIMMessage.V2TIM_ELEM_TYPE_TEXT) {
                        it.onRecvMessageModified(msg.toIMMessage())
                    }
                }
            }
        })
    }

    actual fun getMessageList(
        conversationID: String, count: Int, lastMessageId: String?, callback: TCallback<List<IMMessage>>
    ) {
        if (lastMessageId?.isNotEmpty() == true) {
            findMessages(lastMessageId, object : TCallback<V2TIMMessage> {
                override fun onSuccess(t: V2TIMMessage) {
                    getC2CHistoryMessageList(conversationID, count, t, callback)
                }

                override fun onError(code: Int, desc: String?) {
                    callback.onSuccess(emptyList())
                }
            })
        } else {
//            //优先本地拿消息
//            getHistoryMessageList(conversationID, object : TCallback<List<IMMessage>> {
//                override fun onSuccess(t: List<IMMessage>) {
//                    if (t.isEmpty()) {
//                        onError(-1, "lastMessageId not found")
//                    } else {
//                        callback.onSuccess(t)
//                    }
//                }
//
//                override fun onError(code: Int, desc: String?) {
//                    getC2CHistoryMessageList(conversationID, count, null, callback)
//                }
//            })
            getC2CHistoryMessageList(conversationID, count, null, callback)
        }
    }

    private fun findMessages(lastMessageId: String, callback: TCallback<V2TIMMessage>) {
        V2TIMManager.getMessageManager()
            .findMessages(arrayListOf(lastMessageId), object : V2TIMValueCallback<List<V2TIMMessage>> {
                override fun onSuccess(t: List<V2TIMMessage>?) {
                    t?.firstOrNull()?.apply {
                        callback.onSuccess(this)
                    } ?: run {
                        onError(-1, "lastMessageId not found")
                    }
                }

                override fun onError(code: Int, desc: String?) {
                    onError(-1, "lastMessageId not found")
                }
            })
    }

    fun getHistoryMessageList(
        conversationID: String,
        callback: TCallback<List<IMMessage>>
    ) {
        //public abstract void getHistoryMessageList(V2TIMMessageListGetOption option, V2TIMValueCallback<List<V2TIMMessage>> callback);
        val option = V2TIMMessageListGetOption().apply {
            userID = conversationID
            getType = V2TIMMessageListGetOption.V2TIM_GET_LOCAL_NEWER_MSG
            messageTypeList = arrayListOf(V2TIMMessage.V2TIM_ELEM_TYPE_TEXT)
            setCount(20)
        }
        V2TIMManager.getMessageManager()
            .getHistoryMessageList(option,
                object : V2TIMValueCallback<List<V2TIMMessage>> {
                    override fun onError(code: Int, desc: String?) {
                        callback.onError(code, desc)
                    }

                    override fun onSuccess(v2TIMMessages: List<V2TIMMessage>) {
                        val messages = v2TIMMessages.toIMMessageList()
                        callback.onSuccess(messages)
                    }
                }
            )
    }

    fun getC2CHistoryMessageList(
        conversationID: String,
        count: Int,
        msg: V2TIMMessage?,
        callback: TCallback<List<IMMessage>>
    ) {
        V2TIMManager.getMessageManager()
            .getC2CHistoryMessageList(
                conversationID,
                count,
                msg,
                object : V2TIMValueCallback<List<V2TIMMessage>> {
                    override fun onError(code: Int, desc: String?) {
                        callback.onError(code, desc)
                    }

                    override fun onSuccess(v2TIMMessages: List<V2TIMMessage>) {
                        val messages = v2TIMMessages.toIMMessageList()
                        callback.onSuccess(messages)
                    }
                }
            )
    }

    actual fun sendTextMessage(
        text: String, userID: String, callback: TCallback<IMMessage>
    ) {
        V2TIMManager.getMessageManager().sendMessage(V2TIMManager.getMessageManager().createTextMessage(text),
            userID,
            null,
            V2TIMMessage.V2TIM_PRIORITY_NORMAL,
            false,
            V2TIMOfflinePushInfo(),
            object : V2TIMSendCallback<V2TIMMessage> {
                override fun onSuccess(t: V2TIMMessage) {
                    t.toIMMessage()?.apply {
                        callback.onSuccess(this)
                    }
                }

                override fun onError(code: Int, desc: String?) {
                    callback.onError(code, desc)
                }

                override fun onProgress(progress: Int) {

                }

            })
    }

    actual fun addAdvancedMsgListener(listener: IMMessageListener) {
        advancedMsgListenerList.add(listener)
    }

    actual fun removeAdvancedMsgListener(listener: IMMessageListener) {
        advancedMsgListenerList.remove(listener)
    }
}

actual class V2TIMConversationManager {
    private var conversationListenerList = CopyOnWriteArrayList<IMConversationListener>()

    init {
        V2TIMManager.getConversationManager().addConversationListener(object : V2TIMConversationListener() {
            override fun onNewConversation(conversations: List<V2TIMConversation>?) {
                val list = conversations.toIMConversationList()
                conversationListenerList.forEach {
                    it.onNewConversation(list)
                }
            }

            override fun onConversationChanged(conversationList: MutableList<V2TIMConversation>?) {
                val list = conversationList.toIMConversationList()
                conversationListenerList.forEach {
                    it.onConversationChanged(list)
                }

                getUnreadMessageCount(object : TCallback<Int> {
                    override fun onSuccess(t: Int) {
                        onTotalUnreadMessageCountChanged(t.toLong())
                    }

                    override fun onError(code: Int, desc: String?) {

                    }
                })
            }

            override fun onConversationDeleted(conversationIDList: MutableList<String>) {
                conversationListenerList.forEach {
                    it.onConversationDeleted(conversationIDList)
                }
            }

            override fun onTotalUnreadMessageCountChanged(totalUnreadCount: Long) {
                conversationListenerList.forEach {
                    it.onTotalUnreadMessageCountChanged(totalUnreadCount.toInt())
                }
            }
        })

//        // 注册监听指定过滤条件下的未读消息总数变更
//        val filter = V2TIMConversationListFilter()
//        filter.conversationType = V2TIMConversation.V2TIM_C2C
//        V2TIMManager.getConversationManager().subscribeUnreadMessageCountByFilter(filter)
    }

    actual fun getConversationList(callback: TCallback<List<IMConversation>>) {
        V2TIMManager.getConversationManager()
            .getConversationList(0, 1000, object : V2TIMValueCallback<V2TIMConversationResult> {
                override fun onSuccess(t: V2TIMConversationResult) {
                    t.conversationList?.toIMConversationList()?.let { callback.onSuccess(it) }
                }

                override fun onError(code: Int, desc: String?) {
                    callback.onError(code, desc)
                }

            })
    }

    actual fun addConversationListener(listener: IMConversationListener) {
        conversationListenerList.add(listener)
    }

    actual fun removeConversationListener(listener: IMConversationListener) {
        conversationListenerList.remove(listener)
    }

    actual fun cleanConversationUnreadMessageCount(conversationID: String) {
        val finalConversationID = if (conversationID.contains("c2c")) {
            conversationID
        } else {
            "c2c_$conversationID"
        }
        V2TIMManager.getConversationManager().cleanConversationUnreadMessageCount(finalConversationID, 0, 0, object :V2TIMCallback{
            override fun onError(code: Int, desc: String?) {
               Log.i("cleanConversationUnreadMessageCount", "code:$code,desc:$desc")
            }

            override fun onSuccess() {
                Log.i("cleanConversationUnreadMessageCount", "onSuccess")
            }
        })
    }

    actual fun getUnreadMessageCount(callback: TCallback<Int>) {
        val filter = V2TIMConversationListFilter()
        filter.conversationType = V2TIMConversation.V2TIM_C2C
        V2TIMManager.getConversationManager().getUnreadMessageCountByFilter(filter, object : V2TIMValueCallback<Long> {
            override fun onSuccess(t: Long?) {
                callback.onSuccess(t?.toInt() ?: 0)
            }

            override fun onError(code: Int, desc: String?) {
                callback.onSuccess(0)
            }
        })
    }
}

actual class RelationshipManager actual constructor() {
    actual fun getFriendList(callback: TCallback<List<IMUser>>) {
        V2TIMManager.getFriendshipManager().getFriendList(object : V2TIMValueCallback<List<V2TIMFriendInfo>> {
            override fun onSuccess(t: List<V2TIMFriendInfo>?) {
                callback.onSuccess(t.toIMUserList())
            }

            override fun onError(code: Int, desc: String?) {
                callback.onSuccess(emptyList())
            }
        })
    }
}