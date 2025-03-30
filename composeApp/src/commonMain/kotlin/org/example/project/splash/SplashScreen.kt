package org.example.project.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_splash_logo
import kotlinproject.composeapp.generated.resources.compose_splash_slogn
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
fun SplashScreen(callback: () -> Unit) {

    Column(Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Image(painter = painterResource(Res.drawable.compose_splash_logo),
            modifier = Modifier.width(115.dp).height(104.dp),
            contentDescription = null)
    }

    Column(Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom) {

        Image(painter = painterResource(Res.drawable.compose_splash_slogn),
            modifier = Modifier.width(223.dp).height(55.dp),
            alignment = Alignment.Center,
            contentDescription = null)

        Spacer(modifier = Modifier.height(80.dp))
    }


    LaunchedEffect(true) {
        delay(1000)
        callback.invoke()
    }
}