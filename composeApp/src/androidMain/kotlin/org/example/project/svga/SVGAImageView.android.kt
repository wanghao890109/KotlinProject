package org.example.project.svga

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.opensource.svgaplayer.SVGAImageView
import org.example.project.BottomNavigationAnimation

actual fun getSvgaImageView(
    assetName: String,
    modifier: Modifier
): SvgaImageView {

    return object : SvgaImageView {

        override fun getSVGAView(): @Composable () -> Unit {
            return {
                BottomNavigationAnimation(
                    assetName = assetName,
                    modifier = modifier
                )
            }
        }
    }
}