package org.example.project.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.example.project.network.ApiResult
import org.example.project.uitls.AToast


class LoginViewModel : ViewModel() {

    private val _verifyCodeSucceed = MutableSharedFlow<LoginSendSmsResponse>(replay = 0)
    val verifyCodeSucceed = _verifyCodeSucceed.asSharedFlow()

    private val _verifyCodeFailed = MutableSharedFlow<ApiResult.Error>(replay = 0)
    val verifyCodeFailed = _verifyCodeFailed.asSharedFlow()

    private val _loginSucceed = MutableSharedFlow<LoginResponse>(replay = 0)
    val loginSucceed = _loginSucceed.asSharedFlow()

    fun getVerifyCode(phone: String, ticket: String?, randstr: String?) {
        viewModelScope.launch {
            val result = LoginApiService.getVerifyCode(phone, ticket, randstr)
            when (result) {
                is ApiResult.Success -> {
                    _verifyCodeSucceed.emit(result.data)
                }

                is ApiResult.Error -> {
                    _verifyCodeFailed.emit(result)
                }
            }
        }
    }

    fun smsLogin(phone: String, code: String, inviterId: String, inviteQRCode: String, inviterPhone: String) {
        viewModelScope.launch {
            val result = LoginApiService.smsLogin(phone, code, inviterId, inviteQRCode, inviterPhone)
            when (result) {
                is ApiResult.Success -> {
                    _loginSucceed.emit(result.data)
                }

                is ApiResult.Error -> {
                    AToast.show(result.message)
                }
            }
        }
    }
}