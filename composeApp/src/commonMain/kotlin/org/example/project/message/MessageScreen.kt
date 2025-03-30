package org.example.project.message

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.widget_ic_back_titlebar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import org.example.project.imsdk.IMMessage
import org.example.project.imsdk.IMMessageListener
import org.example.project.imsdk.IMSDK
import org.example.project.imsdk.TCallback
import org.example.project.ui.AppColors
import org.example.project.uitls.ALog
import org.jetbrains.compose.resources.painterResource


@Composable
fun MessageScreen(toUser: String, onBackClick: () -> Unit) {

    LaunchedEffect(true) {
        IMSDK.v2TIMConversationManager.cleanConversationUnreadMessageCount(toUser)
    }
    Scaffold(
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                MessageContent(toUser, onBackClick)
            }
        },
        contentColor = AppColors.FFF5F5F5,
        containerColor = AppColors.FFF5F5F5,
    )
}

@Composable
fun MessageContent(toUser: String, onBackClick: () -> Unit) {

    val viewModel = viewModel<MessageViewModel>()
    val keyboard = LocalSoftwareKeyboardController.current

    val user by viewModel.getUserInfo.collectAsState(null)

    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<IMMessage>() }

    var scrollToBottom by remember { mutableStateOf(0) }

    val listState = rememberLazyListState()


    LaunchedEffect(scrollToBottom) {
        listState.animateScrollToItem(0)
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }.distinctUntilChanged()
            .collect { index ->
                if (index == messages.size - 1 && viewModel.hanMoreMessage()) {
                    viewModel.loadMoreMessageList(toUser)
                }
            }
    }

    LaunchedEffect(Unit) {
        viewModel.getUserInfo(toUser)
        viewModel.initMessageList(toUser)
        viewModel.getMessageList.collect {
            messages.addAll(it)
        }
    }

    DisposableEffect(true) {
        val callback = object : IMMessageListener {
            override fun onRecvNewMessage(message: IMMessage?) {
                messages.add(0, message ?: return)
                scrollToBottom++
            }

            override fun onRecvMessageModified(message: IMMessage?) {

            }
        }
        IMSDK.v2TIMMessageManager.addAdvancedMsgListener(callback)
        onDispose {
            IMSDK.v2TIMMessageManager.removeAdvancedMsgListener(callback)
        }
    }

    Box {
        Column(modifier = Modifier.fillMaxSize().imePadding().pointerInput(Unit) {
            detectTapGestures(onPress = {
                keyboard?.hide()
            })
        }) {

            Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
                Row(modifier = Modifier.height(56.dp).wrapContentWidth().background(Color(0xFFF5F5F5))) {
                    Spacer(Modifier.width(8.dp))
                    Image(
                        painter = painterResource(Res.drawable.widget_ic_back_titlebar),
                        modifier = Modifier.width(30.dp).height(30.dp).align(Alignment.CenterVertically).clickable {
                            onBackClick.invoke()
                        },
                        alignment = Alignment.Center,
                        contentDescription = null
                    )
                }
                Text(
                    user?.name ?: "",
                    modifier = Modifier.padding(horizontal = 16.dp).align(Alignment.Center),
                    fontSize = 24.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                // 消息列表
                LazyColumn(
                    reverseLayout = true,
                    modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp),
                    state = listState
                ) {
                    items(messages) { msg ->
                        ChatBubble(message = msg)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }

            // 输入框 + 发送按钮
            Column {
                Spacer(Modifier.fillMaxWidth().background(AppColors.FFEDEDED).height(1.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().background(AppColors.FFF6F6F6).padding(16.dp, 6.dp, 20.dp, 6.dp)
                        .animateContentSize()
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier.weight(1f).background(AppColors.WHITE, RoundedCornerShape(8.dp))
                            .height(50.dp).padding(horizontal = 0.dp, vertical = 0.dp),
                        textStyle = TextStyle(fontSize = 14.sp),
                        maxLines = 4,
                        colors = colors(
                            focusedPlaceholderColor = AppColors.FFADADAD,
                            unfocusedPlaceholderColor = AppColors.FFADADAD,
                            focusedTextColor = AppColors.TextPrimary,
                            unfocusedTextColor = AppColors.TextPrimary,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (messageText.isNotBlank()) {
                                IMSDK.v2TIMMessageManager.sendTextMessage(
                                    messageText,
                                    user?.userID ?: return@Button,
                                    object : TCallback<IMMessage> {
                                        override fun onSuccess(t: IMMessage) {
                                            messages.add(0, t)
                                            scrollToBottom++
                                        }

                                        override fun onError(code: Int, desc: String?) {
                                            ALog.log("sendTextMessage", "code:$code, desc:$desc")
                                        }
                                    })
                                messageText = ""
                            }
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.TextPrimary,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Unspecified,
                            disabledContentColor = Color.Unspecified,
                        )
                    ) {
                        Text("发送")
                    }
                }
            }
        }
    }
}
