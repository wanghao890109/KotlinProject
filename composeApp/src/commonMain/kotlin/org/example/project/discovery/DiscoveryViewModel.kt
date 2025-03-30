package org.example.project.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.example.project.network.ApiResult
import org.example.project.store.DataStoreManager


class DiscoveryViewModel : ViewModel() {

    private val _discoveryList = MutableSharedFlow<List<InfoFlowBean?>>(replay = 0)
    val discoveryList = _discoveryList.asSharedFlow()
    private var preQueryOffset: Long? = null

    suspend fun initInfoFlow(): List<InfoFlowBean> {
        val list = DataStoreManager.getSerializable<List<InfoFlowBean>>("InfoFlowInit").firstOrNull() ?: emptyList()
        if (list.isNotEmpty()) {
            viewModelScope.launch {
                delay(50)
                _discoveryList.emit(list)
            }
        } else {
            getInfoFlowUp()
        }
        return list
    }

    fun getInfoFlowUp() {
        preQueryOffset = null
        getInfoFlowList(preQueryOffset, 2)
    }

    fun getInfoFlowDown() {
        getInfoFlowList(preQueryOffset, 1)
    }

    private fun getInfoFlowList(
        queryOffset: Long? = null, pullRefresh: Int
    ) {
        viewModelScope.launch {
            val result = DiscoveryApiService.getInfoFlowList(queryOffset, pullRefresh)
            when (result) {
                is ApiResult.Success -> {
                    DataStoreManager.putSerializable("InfoFlowInit", result.data)
                    _discoveryList.emit(result.data)
                    this@DiscoveryViewModel.preQueryOffset = result.data.last()?.infoFlowId ?: 0
                }

                is ApiResult.Error -> {

                }
            }
        }
    }

}