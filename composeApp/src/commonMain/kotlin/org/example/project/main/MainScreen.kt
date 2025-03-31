package org.example.project.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.project.contact.ContactScreen
import org.example.project.conversation.ConversationScreen
import org.example.project.discovery.DiscoveryScreen
import org.example.project.mine.MineScreen
import org.example.project.ui.AppColors
import org.example.project.uitls.JsonUtil
import org.example.project.uitls.StatusBarUtils

@Composable
fun MainScreen(router: (String) -> Unit) {

    val navigationItemSaver = Saver<NavigationItem, String>(save = { item -> JsonUtil.toJson(item) },
        restore = { data -> JsonUtil.fromJson(data) })
    var navigationItem: NavigationItem by rememberSaveable(stateSaver = navigationItemSaver) {
        mutableStateOf(
            NavigationItem.Home
        )
    }

    val navController: NavHostController = rememberNavController()
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1.0f)) {

            NavHost(navController = navController, startDestination = NavigationItem.Home.route) {

                composable(NavigationItem.Home.route,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }) {
                    ConversationScreen() {
                        router.invoke("message?userId=${it.userId}")
                    }
                }
                composable(NavigationItem.Channel.route,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }) {
                    ChannelScreen()
                }
                composable(NavigationItem.Discovery.route,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }) {
                    DiscoveryScreen()

                }
                composable(NavigationItem.Contact.route,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }) {
                    ContactScreen() {
                        router.invoke("message?userId=${it.userID}")
                    }
                }
                composable(NavigationItem.Mine.route,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }) {
                    MineScreen() {
                        router.invoke(it)
                    }
                }
            }
        }
        BottomNavigationBar(navigationItem) { item ->

            navController.navigate(item.route) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
            }
            navigationItem = item
        }
    }
}


@Composable
fun ChannelScreen() {

    Box(
        modifier = Modifier.fillMaxSize().background(AppColors.FFF5F5F5)
            .padding(top = StatusBarUtils.getStatusBarHeight().dp)
    ) {
        Text("频道", fontSize = 20.sp, color = Color.Black, modifier = Modifier.align(Alignment.Center))
    }
}
