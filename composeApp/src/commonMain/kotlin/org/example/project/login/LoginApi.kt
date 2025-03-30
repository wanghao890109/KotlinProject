package org.example.project.login

import org.example.project.network.ApiResult


interface LoginApi {
    suspend fun getVerifyCode(phone: String, ticket:String?, randstr:String?): ApiResult<LoginSendSmsResponse>

    suspend fun smsLogin(phone:String, code:String, inviterId:String, inviteQRCode:String, inviterPhone:String): ApiResult<LoginResponse>
}