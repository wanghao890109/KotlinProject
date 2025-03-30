package org.example.project.message

import androidx.compose.ui.util.fastCbrt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.example.project.imsdk.IMMessage
import org.example.project.imsdk.IMSDK
import org.example.project.imsdk.IMUser
import org.example.project.imsdk.TCallback
import org.example.project.uitls.ALog


class MessageViewModel : ViewModel() {

    private val _getUserInfo = MutableSharedFlow<IMUser>(replay = 0)
    val getUserInfo = _getUserInfo.asSharedFlow()

    private val _getMessageList = MutableSharedFlow<List<IMMessage>>(replay = 0)
    val getMessageList = _getMessageList.asSharedFlow()

    var lastMessageId: String? = null
    var hasMore: Boolean = false

    fun getUserInfo(userId: String) {
        IMSDK.v2TIMManager.getUsersInfo(userId, object : TCallback<IMUser> {
            override fun onSuccess(t: IMUser) {
                viewModelScope.launch {
                    _getUserInfo.emit(t)
                }
            }

            override fun onError(code: Int, desc: String?) {
                ALog.log("getUsersInfo", "code:$code, desc:$desc")
            }
        })
    }


    fun initMessageList(conversationID: String) {
        lastMessageId = null
        loadMoreMessageList(conversationID)
    }

    fun loadMoreMessageList(conversationID: String) {
        IMSDK.v2TIMMessageManager.getMessageList(conversationID,
            20,
            lastMessageId,
            object : TCallback<List<IMMessage>> {
                override fun onSuccess(t: List<IMMessage>) {
                    hasMore = t.size >= 20
                    lastMessageId = t.lastOrNull()?.id
                    viewModelScope.launch {
                        _getMessageList.emit(t)
                    }
                }

                override fun onError(code: Int, desc: String?) {
                    ALog.log("getMessageList", "code:$code, desc:$desc")
                }
            })
    }

    fun hanMoreMessage(): Boolean {
        if (lastMessageId?.isNotEmpty() == true) {
            return false
        }
        return hasMore
    }

}