package org.example.project.imsdk

interface TCallback<T> {
    fun onSuccess(t: T)

    fun onError(code: Int, desc: String?)
}