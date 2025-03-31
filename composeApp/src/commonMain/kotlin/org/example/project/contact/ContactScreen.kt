package org.example.project.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.imsdk.IMUser
import org.example.project.ui.AppColors
import org.example.project.uitls.JsonUtil
import org.example.project.uitls.StatusBarUtils

@Composable
fun ContactScreen(click: (IMUser) -> Unit) {

    val viewModel = viewModel<ContactViewModel>()

    val groupedContactsSaver = Saver<Map<Char, List<IMUser>>, String>(
        save = { item -> JsonUtil.toJson(item) },  // 转换为 String 存储
        restore = { data -> JsonUtil.fromJson(data) }
    )

    var groupedContacts: Map<Char, List<IMUser>> by rememberSaveable(stateSaver = groupedContactsSaver) {
        mutableStateOf(mutableMapOf())
    }

    LaunchedEffect(Unit) {
        if (groupedContacts.isEmpty()) {
            viewModel.getFriendList()
            viewModel.friendMap.collect {
                groupedContacts = it
            }
        }
    }

    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize().background(AppColors.FFF5F5F5)
        .padding(top = StatusBarUtils.getStatusBarHeight().dp).clickable {

        }) {
        Box(modifier = Modifier.height(56.dp).fillMaxWidth().background(Color(0xFFF5F5F5))) {
            Text(
                "通讯录",
                modifier = Modifier.align(Alignment.Center).padding(horizontal = 16.dp),
                fontSize = 24.sp,
                color = Color.Black,
                maxLines = 1
            )
        }

        Box(modifier = Modifier.weight(1f).fillMaxSize().padding(horizontal = 12.dp)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(), state = listState
            ) {

                item {
                    SearchItem()
                }
                groupedContacts.forEach { (initial, items) ->

                    item {
                        Text(
                            color = AppColors.TextSecondary,
                            text = initial.toString(),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(4.dp).background(Color.Transparent).fillMaxWidth()
                        )
                    }

                    items(items) { item ->
                        Box {

                        }
                        ContactItem(item) {
                            click.invoke(it)
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun SearchItem() {
    var searchText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(bottom = 4.dp).background(Color(0xFFF5F5F5))
    ) {
        TextField(
            value = searchText,
            colors = colors(
                focusedPlaceholderColor = AppColors.FFADADAD,
                unfocusedPlaceholderColor = AppColors.FFADADAD,
                focusedTextColor = AppColors.BLACK,
                unfocusedTextColor = AppColors.BLACK,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
            placeholder = { Text("搜索", fontSize = 12.sp, modifier = Modifier.wrapContentHeight()) },
            modifier = Modifier.wrapContentHeight().fillMaxWidth()
                .background(AppColors.WHITE, RoundedCornerShape(4.dp)),
            onValueChange = {
                searchText = it
            },
            maxLines = 1
        )
    }
}