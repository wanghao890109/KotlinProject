package org.example.project.login

import io.ktor.client.call.body
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.project.network.ApiResponse
import org.example.project.network.ApiResult
import org.example.project.network.AppHttpClient

object LoginApiService : LoginApi {

//    private val client = AppHttpClient("http://imfunc.dev.sharexm.cn/api/im-portal")
    private val client = AppHttpClient("https://imfunc.sharexm.cn/api/im-portal")

    override suspend fun getVerifyCode(
        phone: String,
        ticket: String?,
        randstr: String?
    ): ApiResult<LoginSendSmsResponse> {
        return try {
            val response = client.post("/api/app/sms/send") {
                setBody(LoginSendSmsRequest(phone, ticket = ticket, randstr = randstr))
            }
            val body = response.body<ApiResponse<LoginSendSmsResponse>>()
            if (body.code != 0) {
                return ApiResult.Error(body.message, body.code)
            } else {
                return ApiResult.Success(body.data ?: LoginSendSmsResponse())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println(e.message)
            ApiResult.Error(e.message ?: "获取失败")
        }
    }

    override suspend fun smsLogin(
        phone: String,
        code: String,
        inviterId: String,
        inviteQRCode: String,
        inviterPhone: String
    ): ApiResult<LoginResponse> {
        return try {
            val params =  Json.encodeToString(mapOf(
                "phone" to phone,
                "code" to code,
                "inviterId" to inviterId,
                "inviteQRCode" to inviteQRCode,
                "inviterPhone" to inviterPhone,
                "deviceInfo" to "HUAWEI_TAS-AN00_12",
                "deviceId" to "43e32c0c4d18ae88"
            ))


            val response = client.post("/api/app/sms/login") {
                contentType(ContentType.Application.Json)
                setBody(params)
            }
            var body = response.body<ApiResponse<Int>>()
            if (body.code != 0) {
                return ApiResult.Error(body.message, body.code)
            } else {
                if (body.data == null) {
                    val newBody = response.body<LoginResponse>()
                    return ApiResult.Success(newBody)
                }
                return ApiResult.Error(body.message, body.code)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println(e.message)
            ApiResult.Error(e.message ?: "登录失败")
        }
    }
}