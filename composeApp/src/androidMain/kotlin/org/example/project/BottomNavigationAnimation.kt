package org.example.project

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.opensource.svgaplayer.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Preview
@Composable
fun BottomNavigationAnimation(
    assetName: String,
    modifier: Modifier
) {
    val context = AppContextHolder.context
    var svgaDrawable by remember { mutableStateOf<SVGADrawable?>(null) }

    LaunchedEffect(assetName) {
        withContext(Dispatchers.IO) {
            try {
                val parser = SVGAParser(context)
                parser.decodeFromAssets(assetName, object : SVGAParser.ParseCompletion {
                    override fun onComplete(videoItem: SVGAVideoEntity) {
                        svgaDrawable = SVGADrawable(videoItem)
                    }

                    override fun onError() {
                        // 解析错误处理
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    AndroidView(
        modifier = modifier,
        factory = {
            SVGAImageView(AppContextHolder.context).apply {
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                loops = 1
                isClickable = false
            }
        },
        update = { imageView ->

            svgaDrawable?.let {
                imageView.setImageDrawable(it)
                imageView.stepToPercentage(1.0, false)
                imageView.startAnimation()
            }
        }
    )

}


