package org.example.project.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.imsdk.IMConversation
import org.example.project.ui.view.UserAvatar
import org.example.project.ui.AppColors
import org.example.project.ui.view.UnreadCountComposable

@Composable
fun ConversationItem(conversation: IMConversation, click: (IMConversation) -> Unit) {

    val message = conversation.lastMessage

    Column(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().background(Color.White).clickable {
            click.invoke(conversation)
        },
    ) {
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(conversation.userAvatar)
            Spacer(Modifier.width(8.dp))

            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
                Text(
                    text = conversation.userName, fontSize = 18.sp, color = Color.Black, maxLines = 1
                )
                Text(
                    text = message.message,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (conversation.unreadCount > 0) {
                UnreadCountComposable(conversation.unreadCount, Modifier)
            }
        }
        Spacer(Modifier.height(8.dp))
        Row {
            Spacer(Modifier.width(58.dp).height(0.5.dp))
            Spacer(Modifier.fillMaxWidth().background(AppColors.FFEDEDED).height(1.dp))
        }

    }
}
