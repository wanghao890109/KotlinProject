package org.example.project.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.imsdk.IMConversation
import org.example.project.imsdk.IMConversationListener
import org.example.project.imsdk.IMSDK
import org.example.project.imsdk.TCallback
import org.example.project.main.NavigationItem
import org.example.project.ui.AppColors
import org.example.project.uitls.ALog
import org.example.project.uitls.JsonUtil
import org.example.project.uitls.StatusBarUtils

@Composable
fun ConversationScreen(click: (IMConversation) -> Unit) {

    val conversationsSaver = Saver<MutableList<IMConversation>, String>(
        save = { item -> JsonUtil.toJson(item) },  // 转换为 String 存储
        restore = { data -> JsonUtil.fromJson(data) }
    )
    val conversations: MutableList<IMConversation> by rememberSaveable(stateSaver = conversationsSaver) {
        mutableStateOf(mutableStateListOf())
    }

//    val conversations = remember { mutableStateListOf<IMConversation>() }
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        if (conversations.isEmpty()) {
            IMSDK.v2TIMConversationManager.getConversationList(object : TCallback<List<IMConversation>> {
                override fun onSuccess(t: List<IMConversation>) {
                    conversations.addAll(t)
                }

                override fun onError(code: Int, desc: String?) {
                    ALog.log("获取会话列表失败", "$code, $desc")
                }
            })
        }
    }

    DisposableEffect(true) {
        val callback = object : IMConversationListener {
            override fun onNewConversation(t: List<IMConversation>) {
                conversations.addAll(t)
            }

            override fun onConversationChanged(t: List<IMConversation>) {
                t.forEach { item ->
                    val findIndex = conversations.indexOfLast { it.id == item.id }
                    if (findIndex != -1) {
                        conversations[findIndex] = item
                    }
                }
            }

            override fun onConversationDeleted(t: List<String>) {

            }

            override fun onTotalUnreadMessageCountChanged(totalUnreadCount: Int) {

            }
        }
        IMSDK.v2TIMConversationManager.addConversationListener(callback)
        onDispose {
            IMSDK.v2TIMConversationManager.removeConversationListener(callback)
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().background(AppColors.FFF5F5F5)
            .padding(top = StatusBarUtils.getStatusBarHeight().dp)
    ) {
        Box(modifier = Modifier.height(56.dp).fillMaxWidth().background(Color(0xFFF5F5F5))) {
            Text(
                "消息",
                modifier = Modifier.align(Alignment.Center).padding(horizontal = 16.dp),
                fontSize = 24.sp,
                color = Color.Black,
                maxLines = 1
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(), state = listState
        ) {
            items(conversations) { item ->
                ConversationItem(item) {
                    click(it)
                }
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    }
}
