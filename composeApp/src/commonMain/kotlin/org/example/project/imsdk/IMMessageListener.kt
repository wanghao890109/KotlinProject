package org.example.project.imsdk

interface IMMessageListener {
    fun onRecvNewMessage(message: IMMessage?)
    fun onRecvMessageModified(message: IMMessage?)
}