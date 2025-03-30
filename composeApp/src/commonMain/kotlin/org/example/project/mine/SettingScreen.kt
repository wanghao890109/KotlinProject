package org.example.project.mine

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role.Companion.Switch
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.widget_ic_back_titlebar
import org.example.project.imsdk.IMCallback
import org.example.project.imsdk.IMSDK
import org.example.project.ui.AppColors
import org.example.project.uitls.AToast
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun SettingScreen(router: (String) -> Unit, onBackClick: () -> Unit) {
    Scaffold(
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                SettingContent(router, onBackClick)
            }
        },
        contentColor = AppColors.FFF5F5F5,
        containerColor = AppColors.FFF5F5F5,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SettingContent(router: (String) -> Unit, onBackClick: () -> Unit) {
    Box {
        Column(modifier = Modifier.fillMaxSize()) {
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
                    "设置",
                    modifier = Modifier.padding(horizontal = 16.dp).align(Alignment.Center),
                    fontSize = 24.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                SettingItem("账号与安全", {

                })
                SettingItem("消息通知", {

                })
                SettingSwitchItem("听筒模式", {

                })
                SettingItem("问题反馈", {

                })
                SettingItem("关于享脉", {

                })

                Spacer(Modifier.fillMaxWidth().height(10.dp).background(AppColors.FFEDEDED))

                Box(modifier = Modifier.fillMaxWidth().height(56.dp).clickable {
                    IMSDK.v2TIMManager.logout(object : IMCallback {
                        override fun onSuccess() {
                            AToast.show("退出登录")
                            router.invoke("login")
                        }

                        override fun onError(code: Int, desc: String?) {
                            AToast.show("退出登录")
                            router.invoke("login")
                        }
                    })
                }, contentAlignment = Alignment.Center) {
                    Text(
                        "退出登录",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.wrapContentSize(),
                        fontSize = 16.sp,
                        color = Color.Red
                    )
                }

                Spacer(Modifier.fillMaxWidth().weight(1f).background(AppColors.FFEDEDED))
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SettingItem(title: String, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 16.dp).clickable {
        onClick.invoke()
    }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(title, modifier = Modifier.weight(1f), fontSize = 16.sp, color = Color.Black)
            AsyncImage(
                model = Res.getUri("drawable/arrow_right.png"),
                modifier = Modifier.width(16.dp).height(16.dp),
                alignment = Alignment.Center,
                contentDescription = null
            )
        }
        Spacer(Modifier.fillMaxWidth().height(1.dp).background(AppColors.FFEDEDED))
    }
}

@Composable
fun SettingSwitchItem(title: String, onClick: (Boolean) -> Unit) {
    var isChecked by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(title, modifier = Modifier.weight(1f), fontSize = 16.sp, color = Color.Black)
            Switch(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    onClick.invoke(it)
                },
                modifier = Modifier.padding(end = 0.dp),
                colors = SwitchDefaults.colors(
                    checkedTrackColor = AppColors.TextPrimary,
                    uncheckedTrackColor = AppColors.TextPrimary,
                    checkedThumbColor = AppColors.FFEDEDED,
                    uncheckedThumbColor = AppColors.FFEDEDED,
                    checkedBorderColor = Color.Transparent,
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
        Spacer(Modifier.fillMaxWidth().height(1.dp).background(AppColors.FFEDEDED))
    }
}