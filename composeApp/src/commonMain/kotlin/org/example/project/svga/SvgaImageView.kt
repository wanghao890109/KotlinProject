package org.example.project.svga

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface SvgaImageView {
    fun getSVGAView(): @Composable () -> Unit
}

expect fun getSvgaImageView(assetName: String,
                            modifier: Modifier
): SvgaImageView