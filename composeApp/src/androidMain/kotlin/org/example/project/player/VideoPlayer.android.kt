package org.example.project.player

import android.os.Handler
import android.os.Looper
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import org.example.project.AppContextHolder

actual fun createVideoPlayer(url: String): VideoPlayer = AndroidVideoPlayer(url)

class AndroidVideoPlayer(url: String) : VideoPlayer {

    private val handle = Handler(Looper.getMainLooper())
    private var listener: Listener? = null

    private val exoPlayer = ExoPlayer.Builder(AppContextHolder.context).build().apply {
        setMediaItem(MediaItem.fromUri(url))
        prepare()
        addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == 3) {
                    handle.post(object : Runnable {
                        override fun run() {
                            val currentPosition: Long = currentPosition
                            val duration: Long = duration
                            listener?.onPlaybackStateChanged(playbackState, currentPosition, duration)
                            handle.postDelayed(this, 500)
                        }
                    })
                } else {
                    val currentPosition: Long = currentPosition
                    val duration: Long = duration
                    listener?.onPlaybackStateChanged(playbackState, currentPosition, duration)
                    handle.removeCallbacksAndMessages(null)
                }
            }
        })
    }

    @OptIn(UnstableApi::class)
    override fun getPlayerView(): @Composable () -> Unit = {
        AndroidView(factory = { context ->
            PlayerView(context).apply {
                imageDisplayMode = PlayerView.IMAGE_DISPLAY_MODE_FILL
                useController = false
                controllerAutoShow = false
                player = exoPlayer
                hideController()
            }
        }, modifier = Modifier.fillMaxSize())
    }

    override fun play() {
        exoPlayer.playWhenReady = true
    }

    override fun pause() {
        exoPlayer.playWhenReady = false
    }

    override fun stop() {
        exoPlayer.stop()
        exoPlayer.release()
        listener = null
    }

    override fun setListener(listener: Listener) {
        this.listener = listener
    }
}
