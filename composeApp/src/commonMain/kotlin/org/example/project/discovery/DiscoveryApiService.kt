package org.example.project.discovery

import io.ktor.client.call.body
import io.ktor.client.request.setBody
import org.example.project.login.LoginManager
import org.example.project.network.ApiResponse
import org.example.project.network.ApiResult
import org.example.project.network.AppHttpClient

object DiscoveryApiService : DiscoveryApi {
    private val client = AppHttpClient("https://channel-prod.sharexm.cn/api/channel-portal")

    /**
     * contentType - 0:短视频 1:广场帖子
     * preQueryOffset - 预加载偏移量
     * pushNewAlgorithmFlag - 0, "老算法" 1, "新算法"
     * pullRefresh - 0:默认加载，1：上拉加载更多，2：下拉刷新
     */
    override suspend fun getInfoFlowList(
        preQueryOffset: Long?,
        pullRefresh: Int
    ): ApiResult<List<InfoFlowBean?>> {

        return try {

            val response = client.post("/infoFlow/queryInfoFlowListV2") {
                setBody(InfoFlowRequest(0, preQueryOffset, 0, pullRefresh, LoginManager.getUserId() ?: "", 20))
            }
            var body = response.body<ApiResponse<List<InfoFlowBean?>>>()
            if (body.data != null) {
                return ApiResult.Success(body.data?: emptyList())
            }
            return ApiResult.Error(body.message)
        } catch (e: Exception) {
            e.printStackTrace()
            println(e.message)
            ApiResult.Error(e.message ?: "视频列表失败")
        }
    }
}