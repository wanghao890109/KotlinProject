package org.example.project.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import platform.AVFoundation.*
import platform.UIKit.*
import platform.Foundation.*


actual fun createVideoPlayer(url: String): VideoPlayer = IOSVideoPlayer(url)

class IOSVideoPlayer(url: String) : VideoPlayer {
    private val player = AVPlayer().apply {
        replaceCurrentItemWithPlayerItem(platform.AVFoundation.AVPlayerItem(platform.Foundation.NSURL(string = url)))
    }
    private val playerLayer = AVPlayerLayer(player)

    override fun getPlayerView(): @Composable () -> Unit = {
        UIKitView(
            factory = { UIView().apply { layer.addSublayer(playerLayer) } },
            modifier = Modifier.fillMaxSize()
        )
    }

    override fun setListener(listener: Listener) {
        TODO("Not yet implemented")
    }

    override fun play() {
        player.play()
    }

    override fun pause() {
        player.pause()
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}
