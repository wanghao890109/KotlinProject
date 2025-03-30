package org.example.project.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.imsdk.IMUser
import org.example.project.ui.view.UserAvatar
import org.example.project.ui.AppColors

@Composable
fun ContactItem(imUser: IMUser, click: (IMUser) -> Unit) {

    Box(modifier = Modifier.fillMaxWidth().wrapContentHeight().background(Color.White)) {
        Column(
            modifier = Modifier.fillMaxWidth().height(60.dp).clickable {
                click.invoke(imUser)
            }, verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserAvatar(imUser.avatar, size = 30.dp, corner = 4.dp)
                Spacer(Modifier.width(16.dp))

                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = imUser.name, fontSize = 18.sp, color = Color.Black, maxLines = 1
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Row {
                Spacer(Modifier.width(50.dp).height(0.5.dp))
                Spacer(Modifier.fillMaxWidth().background(AppColors.FFEDEDED).height(1.dp))
            }

        }
    }

}
