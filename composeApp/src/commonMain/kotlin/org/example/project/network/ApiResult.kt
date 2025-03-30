package org.example.project.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
}


@Serializable
class ApiResponse<T> {
    var code = 0

    var message: String = ""
    var data: T? = null

}