package org.example.project.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.ui.AppColors

@Composable
fun VerifyCodeButton(
    modifier: Modifier = Modifier,
    totalSeconds: Int = 60,
    onSend: suspend () -> Unit
) {
    var secondsLeft by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    val isCounting = secondsLeft > 0
    val buttonText = if (isCounting) "重新获取(${secondsLeft}s)" else "获取验证码"

    LaunchedEffect(secondsLeft) {
        if (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    Text(
        text = buttonText,
        fontSize = 10.sp,
        color = if (isCounting) AppColors.TextSecondary else AppColors.TextPrimary,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AppColors.FFF2F2F2)
            .clickable(enabled = !isCounting) {
                scope.launch {
                    onSend()
                    secondsLeft = totalSeconds
                }
            }
            .padding(horizontal = 6.dp, vertical = 4.dp)
    )
}
