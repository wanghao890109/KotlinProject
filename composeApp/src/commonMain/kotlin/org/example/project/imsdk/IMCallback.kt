package org.example.project.imsdk

interface IMCallback {
    fun onSuccess()

    fun onError(code: Int, desc: String?)
}