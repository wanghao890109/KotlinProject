package org.example.project.main

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinproject.composeapp.generated.resources.Res
import kotlinx.serialization.Serializable
import org.example.project.imsdk.IMSDK
import org.example.project.imsdk.SampleConversationListener
import org.example.project.imsdk.TCallback
import org.example.project.svga.getSvgaImageView
import org.example.project.ui.AppColors
import org.example.project.ui.view.UnreadCountComposable
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun BottomNavigationBar(currentRoute: NavigationItem, callback: (NavigationItem) -> Unit) {

    var unreadCount by remember { mutableStateOf(0) }

    DisposableEffect(true) {
        IMSDK.v2TIMConversationManager.getUnreadMessageCount(object : TCallback<Int> {
            override fun onSuccess(t: Int) {
                unreadCount = t
            }

            override fun onError(code: Int, desc: String?) {

            }
        })
        val listener = object : SampleConversationListener() {
            override fun onTotalUnreadMessageCountChanged(totalUnreadCount: Int) {
                unreadCount = totalUnreadCount
            }
        }
        IMSDK.v2TIMConversationManager.addConversationListener(listener)
        onDispose {
            IMSDK.v2TIMConversationManager.removeConversationListener(listener)
        }
    }

    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Channel,
        NavigationItem.Discovery,
        NavigationItem.Contact,
        NavigationItem.Mine
    )
    val isDarkStyle = currentRoute == NavigationItem.Discovery
    val contentColor = if (isDarkStyle) {
        AppColors.FF1D1D1D
    } else {
        AppColors.FFF9F9F9
    }
    Column {
        Spacer(
            Modifier.fillMaxWidth().background(if (isDarkStyle) AppColors.BLACK else AppColors.FFEDEDED).height(1.dp)
        )
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().background(contentColor)) {
            items.forEach { item ->
                Column(
                    modifier = Modifier.height(80.dp).weight(1f).clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (currentRoute.route != item.route) { // 只有不在当前页面才触发跳转
                            callback.invoke(item)
                        }
                    },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomNavigationBarItem(item, currentRoute, isDarkStyle, unreadCount)
                }
            }
        }
//        NavigationBar(contentColor = contentColor, containerColor = contentColor) {
//
//            items.forEach { item ->
//                val isSelected = currentRoute.route == item.route
//                val svgaView = getSvgaImageView(item.icon, Modifier.width(30.dp).height(30.dp))
//                val icon: @Composable () -> Unit = {
//                    Box(
//                        modifier = Modifier.wrapContentWidth().height(36.dp), contentAlignment = Alignment.BottomCenter
//                    ) {
//                        Box(modifier = Modifier.wrapContentSize()) {
//                            if (isSelected) {
//                                svgaView.getSVGAView().invoke()
//                            } else {
//
//                                AsyncImage(
//                                    model = if (isDarkStyle) {
//                                        Res.getUri(item.iconResDark)
//                                    } else {
//                                        Res.getUri(item.iconRes)
//                                    },
//                                    modifier = Modifier.width(30.dp).height(30.dp),
//                                    alignment = Alignment.Center,
//                                    contentDescription = null
//                                )
//                            }
//                        }
//
//                        if (unreadCount > 0 && item.route == NavigationItem.Home.route) {
//                            UnreadCountComposable(unreadCount, Modifier.align(Alignment.TopEnd))
//                        }
//                    }
//                }
//
//                    NavigationBarItem(
//                        icon = icon,
//                        label = { Text(item.title) },
//                        selected = isSelected,
//                        colors = BottomNavigationBarItemColors(isDarkStyle),
//                        onClick = {
//                            if (currentRoute.route != item.route) { // 只有不在当前页面才触发跳转
//                                callback.invoke(item)
//                            }
//                        },
//                        interactionSource = remember { MutableInteractionSource() }, // 禁用水波纹
//                    )
//
//            }
//        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CustomNavigationBarItem(
    item: NavigationItem,
    currentRoute: NavigationItem,
    isDarkStyle: Boolean,
    unreadCount: Int
) {
    val isSelected = currentRoute.route == item.route
    val svgaView = getSvgaImageView(item.icon, Modifier.width(30.dp).height(30.dp))
    Column(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.wrapContentWidth().height(36.dp), contentAlignment = Alignment.BottomCenter
        ) {
            Box(modifier = Modifier.wrapContentSize()) {
                if (isSelected) {
                    svgaView.getSVGAView().invoke()
                } else {
                    AsyncImage(
                        model = if (isDarkStyle) {
                            Res.getUri(item.iconResDark)
                        } else {
                            Res.getUri(item.iconRes)
                        },
                        modifier = Modifier.width(30.dp).height(30.dp),
                        alignment = Alignment.Center,
                        contentDescription = null
                    )
                }
            }
            if (unreadCount > 0 && item.route == NavigationItem.Home.route) {
                UnreadCountComposable(unreadCount, Modifier.align(Alignment.TopEnd))
            }
        }
        Text(
            item.title, color = if (isSelected) AppColors.TextPrimary else {
                if (isDarkStyle) {
                    AppColors.FFE5E5E5
                } else {
                    AppColors.TextSecondary
                }
            }, fontSize = 12.sp
        )
    }
}

@Composable
fun BottomNavigationBarItemColors(isDarkStyle: Boolean) = NavigationBarItemColors(
    selectedIconColor = Color.Transparent,
    selectedTextColor = AppColors.TextPrimary,
    selectedIndicatorColor = Color.Transparent,
    unselectedIconColor = Color.Transparent,
    unselectedTextColor = if (isDarkStyle) AppColors.FFE5E5E5 else AppColors.TextSecondary,
    disabledIconColor = Color.Transparent,
    disabledTextColor = Color.Transparent
)

@Serializable
sealed class NavigationItem(
    val route: String, val icon: String, val iconRes: String, val iconResDark: String, val title: String
) {

    @Serializable
    data object Home : NavigationItem(
        "conversation",
        "main_tab_conversation.svga",
        "drawable/main_ic_navigation_bar_conversation_unselected.png",
        "drawable/main_ic_navigation_bar_channel_unselected_dark.png",
        "消息"
    )

    @Serializable
    data object Channel : NavigationItem(
        "channel",
        "main_tab_channel.svga",
        "drawable/main_ic_navigation_bar_channel_unselected.png",
        "drawable/main_ic_navigation_bar_channel_unselected_dark.png",
        "频道"
    )

    @Serializable
    data object Discovery : NavigationItem(
        "discovery",
        "main_tab_discovery.svga",
        "drawable/main_ic_navigation_bar_discovery_unselected.png",
        "drawable/main_ic_navigation_bar_discovery_unselected.png",
        "发现"
    )

    @Serializable
    data object Contact : NavigationItem(
        "contact",
        "main_tab_contact.svga",
        "drawable/main_ic_navigation_bar_contacts_unselected.png",
        "drawable/main_ic_navigation_bar_contacts_unselected_dark.png",
        "通讯录"
    )

    @Serializable
    data object Mine : NavigationItem(
        "mine",
        "main_tab_mine.svga",
        "drawable/main_ic_navigation_bar_mine_unselected.png",
        "drawable/main_ic_navigation_bar_mine_unselected_dark.png",
        "我的"
    )
}
