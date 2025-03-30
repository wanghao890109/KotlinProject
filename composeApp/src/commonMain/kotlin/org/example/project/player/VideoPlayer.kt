package org.example.project.player

import androidx.compose.runtime.Composable

interface VideoPlayer {
    fun getPlayerView(): @Composable () -> Unit
    fun setListener(listener: Listener)
    fun play()
    fun pause()
    fun stop()
}

interface Listener {
    fun onPlaybackStateChanged(playbackState: Int, currentPosition: Long, duration: Long)
}

expect fun createVideoPlayer(url: String): VideoPlayer