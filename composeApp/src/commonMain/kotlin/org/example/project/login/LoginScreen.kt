package org.example.project.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_splash_logo
import kotlinx.coroutines.delay
import org.example.project.ui.AppColors
import org.example.project.uitls.AToast
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun LoginScreen(callback: () -> Unit) {
    val viewModel = viewModel<LoginViewModel>()

    val defaultPhone = "14400002201"
    var phoneText by remember { mutableStateOf(TextFieldValue(defaultPhone, TextRange(defaultPhone.length))) }
    var codeText by remember { mutableStateOf("0331") }
    var loginButtonState by remember { mutableStateOf(codeText.length == 4) }
    val phoneFocusRequester = remember { FocusRequester() }
    val codeFocusRequester = remember { FocusRequester() }

    val loginUtils = LoginUtils()
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(true) {
        delay(300)
        phoneFocusRequester.requestFocus()
    }
    LaunchedEffect(true) {
        viewModel.verifyCodeFailed.collect {
            when (it.code) {
                10005 -> {
                    keyboard?.hide()
                    loginUtils.showImageCodeWebView(object : ImageCodeCallBack {
                        override fun onResult(ticket: String, randstr: String) {
                            viewModel.getVerifyCode(phoneText.text, ticket, randstr)
                        }
                    })
                }

                else -> {
                    AToast.show(it.message)

                    //TODO
                }
            }
        }
    }
    LaunchedEffect(true) {
        viewModel.verifyCodeSucceed.collect {

        }
    }
    LaunchedEffect(true) {
        viewModel.loginSucceed.collect {
            LoginManager.login(it.toLoginUser(), it.token ?: "")
            callback.invoke()
        }
    }



    Box {

        Column(
            Modifier.fillMaxWidth().fillMaxHeight().padding(start = 55.dp, end = 55.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.fillMaxWidth().height(100.dp))

            Image(
                painter = painterResource(Res.drawable.compose_splash_logo),
                modifier = Modifier.width(120.dp).height(120.dp),
                contentDescription = null
            )

            Spacer(Modifier.fillMaxWidth().height(8.dp))

            Text(text = "拓志趣相投之友，享互利共赢之脉", fontSize = 16.sp, color = AppColors.TextPrimary)
            Spacer(Modifier.fillMaxWidth().height(36.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("手机号", fontSize = 14.sp, color = AppColors.BLACK)
                Spacer(Modifier.width(8.dp))
                TextField(value = phoneText, colors = colors(
                    focusedPlaceholderColor = AppColors.FFADADAD,
                    unfocusedPlaceholderColor = AppColors.FFADADAD,
                    focusedTextColor = AppColors.TextPrimary,
                    unfocusedTextColor = AppColors.TextPrimary,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ), placeholder = {
                    Text("请输入手机号", fontSize = 12.sp)
                }, modifier = Modifier.focusable().focusRequester(phoneFocusRequester), onValueChange = {
                    phoneText = it
                }, maxLines = 1
                )
            }
            HorizontalDivider(
                color = AppColors.FFE5E5E5, thickness = 0.5.dp
            )
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("验证码", fontSize = 14.sp, color = AppColors.BLACK)
                Spacer(Modifier.width(8.dp))
                TextField(
                    value = codeText,
                    colors = colors(
                        focusedPlaceholderColor = AppColors.FFADADAD,
                        unfocusedPlaceholderColor = AppColors.FFADADAD,
                        focusedTextColor = AppColors.TextPrimary,
                        unfocusedTextColor = AppColors.TextPrimary,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    placeholder = { Text("请输入验证码", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f).focusRequester(codeFocusRequester),
                    onValueChange = {
                        codeText = it
                        if (it.length >= 4) {
                            loginButtonState = true
                        }
                    },
                    maxLines = 1
                )
                VerifyCodeButton {
//                viewModel.getVerifyCode("18221570157")
                    viewModel.getVerifyCode(phoneText.text, null, null)
                }
            }
            HorizontalDivider(
                color = AppColors.FFE5E5E5, thickness = 0.5.dp
            )
            Spacer(Modifier.height(32.dp))

            Text("登录/注册",
                fontSize = 14.sp,
                color = AppColors.WHITE,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(9.dp))
                    .background(if (loginButtonState) AppColors.Primary else AppColors.PrimaryDark).clickable {
                        keyboard?.hide()
                        viewModel.smsLogin(phoneText.text, codeText, "", "", "")
                    }.padding(vertical = 12.dp))
        }


    }
    Box {

        loginUtils.initImageCodeWebView().invoke()
    }
}