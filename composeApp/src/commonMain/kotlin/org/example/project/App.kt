package org.example.project

import androidx.compose.animation.Crossfade
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.PlatformContext
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.json.Json
import org.example.project.imsdk.IMUser
import org.example.project.login.LoginScreen
import org.example.project.main.MainScreen
import org.example.project.message.MessageContent
import org.example.project.message.MessageScreen
import org.example.project.mine.SettingScreen
import org.example.project.splash.SplashScreen
import org.example.project.uitls.ALog
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class)
@Composable
@Preview
fun App() {
    val navController: NavHostController = rememberNavController()
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
        Surface {
            NavHost(navController = navController, startDestination = "splash") {
                composable("splash") {
                    SplashScreen(callback = {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    })
                }
                composable("login") {
                    LoginScreen(callback = {
                        navController.navigate("main") {
                            launchSingleTop = true  // ✅ 避免重复实例
                            restoreState = true      // ✅ 恢复保存的状态
                            popUpTo("login") { inclusive = true }
                        }
                    })
                }
                composable("main",
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }) {
                    MainScreen() {
                        navController.navigate(it) {
                            //popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
                composable("message?userId={userId}",
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }) {
                    val userId = it.arguments?.getString("userId")
                    userId?.apply {
                        MessageScreen(this) {
                            navController.popBackStack()
                        }
                    }
                }
                composable("setting",
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }) {
                    SettingScreen(router = { route->
                        navController.navigate(route) {
                            if (route == "login") {
                                popUpTo("main") { inclusive = true }
                                popUpTo("setting") { inclusive = true }
                            }
                        }
                    }, onBackClick = {
                        navController.popBackStack()
                    })
                }
            }
        }
    }
}