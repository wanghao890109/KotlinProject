package org.example.project.discovery

import org.example.project.network.ApiResult


interface DiscoveryApi {
    suspend fun getInfoFlowList(
        preQueryOffset: Long? = null,//预加载偏移量，取信息流列表最后一条的infoFlowId,初始传null
        pullRefresh: Int
    ): ApiResult<List<InfoFlowBean?>>

}