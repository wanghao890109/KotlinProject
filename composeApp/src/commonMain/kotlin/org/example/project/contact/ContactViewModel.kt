package org.example.project.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.example.project.imsdk.IMSDK
import org.example.project.imsdk.IMUser
import org.example.project.imsdk.TCallback
import org.example.project.uitls.getFirstLetter


class ContactViewModel : ViewModel() {

    private val _friendMap = MutableSharedFlow<Map<Char, List<IMUser>>>(replay = 0)
    val friendMap = _friendMap.asSharedFlow()

    fun getFriendList() {
        IMSDK.relationshipManager.getFriendList(object : TCallback<List<IMUser>> {
            override fun onSuccess(t: List<IMUser>) {
                viewModelScope.launch {
                    val map = t.groupBy { getFirstLetter(it.name) }.toList().sortedBy {
                        if (it.first == '#') 'Z' else if (it.first.isLetter()) 'Y' else it.first
                    }.toMap(LinkedHashMap())
                    _friendMap.emit(map)
                }
            }

            override fun onError(code: Int, desc: String?) {

            }
        })
    }


}