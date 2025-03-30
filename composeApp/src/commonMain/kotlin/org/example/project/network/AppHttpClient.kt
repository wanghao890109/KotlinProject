package org.example.project.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encodeUtf8
import org.example.project.login.LoginManager
import org.example.project.uitls.ALog

class AppHttpClient(private val baseUrl: String) {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(DefaultRequest) {

//            val version = "apk_not_found"
            val versionCode = "1"
            val xVersionCode = "apk_not_found"
            if (LoginManager.getUserId()?.isNotEmpty() == true) {
                header("X-User-Id", LoginManager.getUserId())
                header("userID", LoginManager.getUserId())
            }

            if (LoginManager.getToken()?.isNotEmpty() == true) {
                header("X-Authorization", LoginManager.getToken()) //token
            }

            header("version", versionCode)
//            header("version", "2025031710")
            header("language", "zh")
            header("X-User-Channel", "android")
            header("X-Version", versionCode)
//            header("X-Version", "2025031710")
            header("X-PublishChannel", "vivo")
            header("X-VersionName", "2.3.620")
            header("X-ENV", "prod")
            header("X-Emulator", "2")
            header("X-VersionCode", xVersionCode)

            header("Content-Type", "application/json; charset=utf-8")

        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    ALog.log("KtorClient", message) // Android 日志
                }
            }
            level = LogLevel.ALL
        }
    }

    fun buildUrl(path: String): String {
        val base =baseUrl.removeSuffix("/")
        val sub = path.removePrefix("/")
        val time = Clock.System.now().toEpochMilliseconds()
        val password = "android/${path}/data/5c44380b76f5cfb34d888f6f28082866/" + time
        //String query = "?app=android&time=" + time + "&sign=" + MD5.md5Password("android/" + api + "/" + json + "/5c44380b76f5cfb34d888f6f28082866/" + time);
        val query = "?app=android&time=" + time + "&sign=" +   password.encodeUtf8().md5().hex();
        return "$base/$sub$query"
    }

    suspend inline fun post(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = client.post { url(buildUrl(urlString)); block() }

    suspend inline fun get(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = client.get { url(buildUrl(urlString)); block() }

}
