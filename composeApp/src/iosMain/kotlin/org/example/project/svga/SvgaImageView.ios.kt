package org.example.project.svga

import androidx.compose.ui.Modifier

actual fun getSvgaImageView(
    assetName: String,
    modifier: Modifier
): SvgaImageView {
    TODO("Not yet implemented")
}
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import kotlinx.cinterop.ExperimentalForeignApi
//import platform.CoreGraphics.CGRectMake
//import platform.UIKit.*
//import platform.Foundation.*
//import platform.SVGAPlayer.*
//
//@OptIn(ExperimentalForeignApi::class)
//actual fun getSvgaImageView(assetName: String, modifier: Modifier): SvgaImageView {
//    return object : SvgaImageView {
//        private val svgaView = SVGAImageView()
//        private val parser = SVGAParser.sharedParser()
//
//        init {
//            svgaView.frame = CGRectMake(0.0, 0.0, 300.0, 300.0)
//            loadAnimation(assetName)
//        }
//
//        private fun loadAnimation(assetName: String) {
//            parser.parseWithNamed(assetName, inBundle = NSBundle.mainBundle, callback = { videoItem, _ ->
//                videoItem?.let {
//                    svgaView.setVideoItem(it)
//                    svgaView.startAnimation()
//                }
//            })
//        }
//
//        override fun getSVGAView(): @Composable () -> Unit = {
//            UIKitView(
//                factory = { svgaView },
//                modifier = modifier
//            )
//        }
//
//        override fun startAnimation() {
//            svgaView.startAnimation()
//        }
//    }
//}