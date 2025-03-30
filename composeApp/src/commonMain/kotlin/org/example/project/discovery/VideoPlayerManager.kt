package org.example.project.discovery

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import org.example.project.player.Listener
import org.example.project.player.VideoPlayer
import org.example.project.player.createVideoPlayer


val videoPlayers = mutableStateMapOf<Int, VideoPlayer>()
val videoPlayerStates = mutableStateMapOf<Int, VideoPlayerStates>()
val discoveryList = mutableStateListOf<InfoFlowBean?>()

data class VideoPlayerStates(val playState: Int, val currentPosition: Long = 0, val duration: Long = 0)

fun stopVideoPlayers() {
    videoPlayers.keys.toList().forEach { index ->
        videoPlayers.remove(index)?.stop()
    }
    videoPlayerStates.clear()
}

fun initVideoPlayer(currentIndex: Int) {
    // 清理不需要的播放器
    videoPlayers.keys.toList().forEach { index ->
        if (index < currentIndex - 1 || index > currentIndex + 1) {
            videoPlayers.remove(index)?.stop()
        }
    }
    // 确保当前页、前1页、后1页都有播放器
    (-1..1).map { offset ->
        val pageIndex = currentIndex + offset
        if (pageIndex in discoveryList.indices && !videoPlayers.containsKey(pageIndex)) {
            val videoInfo = discoveryList[pageIndex]?.videoInfoList?.firstOrNull()
            if (videoPlayers[pageIndex] == null) {
                videoPlayers[pageIndex] = createVideoPlayer(videoInfo?.videoUrl ?: "")
            }
            videoPlayers[pageIndex]?.setListener(object : Listener {
                override fun onPlaybackStateChanged(playbackState: Int, currentPosition: Long, duration: Long) {
                    videoPlayerStates[pageIndex] = VideoPlayerStates(playbackState, currentPosition, duration)
                }
            })
        }
    }
    // 控制播放状态：当前页播放，其他页暂停
    videoPlayers.forEach { (index, player) ->
        if (index == currentIndex) {
            player.play()
        } else{
            player.pause()
        }
    }
}