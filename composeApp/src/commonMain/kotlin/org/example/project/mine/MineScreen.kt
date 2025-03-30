package org.example.project.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinproject.composeapp.generated.resources.Res
import org.example.project.login.LoginManager
import org.example.project.login.LoginUser
import org.example.project.ui.AppColors
import org.example.project.ui.view.UserAvatar
import org.example.project.uitls.StatusBarUtils
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MineScreen(router: (String) -> Unit) {

    val user = LoginManager.getUser() ?: return

    val paddingTop = StatusBarUtils.getStatusBarHeight().dp
    Column(modifier = Modifier.fillMaxSize().background(AppColors.WHITE).padding(top = paddingTop)) {
        Spacer(Modifier.height(34.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(user.userAvatar, size = 62.dp, corner = 10.dp)
            Spacer(Modifier.width(12.dp))

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = user.userNickName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1
                )
                Text(
                    text = "IP属地: 北京", fontSize = 12.sp, color = AppColors.FF999999, maxLines = 1
                )
            }
        }

        Spacer(Modifier.fillMaxWidth().height(10.dp).background(AppColors.FFEDEDED))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            mineFuncList.forEach { funcItem ->
                Column(Modifier.weight(1f).padding(vertical = 4.dp).align(Alignment.CenterVertically).clickable {
                    router.invoke(funcItem.router)
                }) {
                    AsyncImage(
                        model = Res.getUri(funcItem.iconPath),
                        modifier = Modifier.width(38.dp).height(38.dp).align(Alignment.CenterHorizontally),
                        alignment = Alignment.Center,
                        contentDescription = null
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = funcItem.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.FF1A1A1A,
                        maxLines = 1,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
        Spacer(Modifier.fillMaxWidth().height(10.dp).background(AppColors.FFEDEDED))
    }
}