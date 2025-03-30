package org.example.project.discovery

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.discovery_live_play
import kotlinproject.composeapp.generated.resources.discovery_loading_logo
import org.example.project.ui.AppColors
import org.jetbrains.compose.resources.painterResource

var initialPage = 0

@Composable
fun DiscoveryScreen() {

    val viewModel = viewModel<DiscoveryViewModel>()

//    val discoveryListSaver = Saver<MutableList<InfoFlowBean?>, String>(save = { item ->
//        JsonUtil.toJson(item)
//    }, restore = { data ->
//        JsonUtil.fromJson(data)
//    })

//    val discoveryList: MutableList<InfoFlowBean?> by rememberSaveable(stateSaver = discoveryListSaver) {
//        mutableStateOf(mutableStateListOf())
//    }


    val discoveryListRemember = remember { discoveryList }

    val videoPlayersRemember = remember { videoPlayers }

    val videoPlayerStatesRemember = remember { videoPlayerStates }


    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = {
        discoveryListRemember.size
    })

    LaunchedEffect(pagerState.currentPage, pagerState.currentPageOffsetFraction) {
        if (discoveryListRemember.isNotEmpty()) {
            when {
                // 滑到顶部（第一页）
                pagerState.currentPage == 0 && pagerState.currentPageOffsetFraction == 0f -> {
                    viewModel.getInfoFlowUp()
                }
                // 滑到底部（最后一页）
                pagerState.currentPage == discoveryListRemember.size - 1 && pagerState.currentPageOffsetFraction == 0f -> {
                    viewModel.getInfoFlowDown()
                }
            }
        }
    }

    LaunchedEffect(pagerState.currentPage, discoveryListRemember.size) {
        initialPage = pagerState.currentPage
        initVideoPlayer(initialPage)
    }

    DisposableEffect(true) {
        onDispose {
            stopVideoPlayers()
        }
    }

    LaunchedEffect(true) {
        viewModel.initInfoFlow()
        viewModel.discoveryList.collect {
            discoveryListRemember.addAll(it)
        }
    }

    if (discoveryListRemember.isEmpty()) {
        drawLoading()
        return
    }

    VerticalPager(
        state = pagerState, modifier = Modifier.fillMaxSize().background(AppColors.BLACK), beyondViewportPageCount = 0
    ) { page ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            val pageCount = discoveryListRemember.size
            val videoInfo = discoveryListRemember[page]?.videoInfoList?.firstOrNull()
            val playerStates = videoPlayerStatesRemember[page]
            val videoPlayer = videoPlayersRemember[page]

            LaunchedEffect(playerStates?.playState) {
                if (page == pagerState.currentPage) {
                    if (playerStates?.playState == 4) {
                        val nextPage = (pagerState.currentPage + 1) % pageCount
                      //  pagerState.animateScrollToPage(nextPage)
                    }
                }
            }

            // 视频播放器
            videoPlayer?.getPlayerView()?.invoke()
            // 视频封面
            videoPlayerCover(videoInfo, playerStates)
        }
    }
}

@Composable
fun videoPlayerCover(videoInfo: VideoInfoBean?, videoPlayerStates: VideoPlayerStates?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        val playState = videoPlayerStates?.playState ?: 1
        val currentPosition = videoPlayerStates?.currentPosition ?: 0
        val duration = videoPlayerStates?.duration ?: Long.MAX_VALUE
        val playProgress = currentPosition.toFloat() / duration.toFloat()

        if (playState == 1) {
            AsyncImage(
                model = videoInfo?.videoFirstFrameUrl ?: "",
                contentDescription = null,
                modifier = Modifier.fillMaxSize().background(Color.Black).align(Alignment.Center),
            )
        } else {
            LinearProgressIndicator(
                progress = playProgress,
                modifier = Modifier.fillMaxWidth().height(2.dp).background(Color.White)
                    .align(Alignment.BottomCenter),
                color = AppColors.FF1D1D1D,
                trackColor = AppColors.FF1A1A1A,
                strokeCap = StrokeCap.Butt
            )
        }

        if (playState == 1 || playState == 2 || playState == 4) {//缓冲中，播放结束
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp), strokeWidth = 3.dp, color = Color(0x80000000)
            )
        }
    }
}

@Composable
fun drawLoading() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.discovery_live_play),
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Image(
            painter = painterResource(Res.drawable.discovery_loading_logo),
            modifier = Modifier.width(80.dp).height(80.dp),
            alignment = Alignment.Center,
            contentDescription = null
        )
    }
}


