package org.example.project.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.imsdk.IMMessage
import org.example.project.login.LoginManager
import org.example.project.ui.view.UserAvatar

@Composable
fun ChatBubble(message: IMMessage) {

    val fromMe = message.sender == LoginManager.getUserId()
    val bubbleColor = if (fromMe) Color(0xFFC5D3FA) else Color.White
    val alignment = if (fromMe) Arrangement.End else Arrangement.Start
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = alignment
    ) {
        if (!fromMe) {
            // 左侧消息：头像 + 昵称 + 气泡
            Row(verticalAlignment = Alignment.Top) {
                UserAvatar(message.userAvatar)
                Spacer(Modifier.width(6.dp))

                Column {
                    Text(
                        text = message.userName,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Box(
                        modifier = Modifier
                            .background(bubbleColor, shape = RoundedCornerShape(12.dp))
                            .padding(12.dp)
                            .widthIn(max = 220.dp)
                    ) {
                        Text(text = message.message, color = Color.Black)
                    }
                }

                Spacer(Modifier.width(44.dp))
            }
        } else {
            // 自己的消息：右对齐气泡
            Row(verticalAlignment = Alignment.Top) {
                Column(horizontalAlignment = Alignment.End) {
                    Box(
                        modifier = Modifier
                            .background(bubbleColor, shape = RoundedCornerShape(12.dp))
                            .padding(12.dp)
                            .widthIn(max = 220.dp)
                    ) {
                        Text(text = message.message, color = Color.Black)
                    }
                }

                Spacer(Modifier.width(6.dp))

                UserAvatar(message.userAvatar)
            }
        }
    }
}
