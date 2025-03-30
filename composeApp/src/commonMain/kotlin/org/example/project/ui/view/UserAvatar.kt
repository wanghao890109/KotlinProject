package org.example.project.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_avatar_default
import org.jetbrains.compose.resources.painterResource

@Composable
fun UserAvatar(url: String, size: Dp = 44.dp, corner: Dp = 6.dp) {

    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        placeholder = painterResource(Res.drawable.compose_avatar_default),
        error = painterResource(Res.drawable.compose_avatar_default),
        modifier = Modifier
            .width(size)
            .height(size)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(corner))
            .background(Color.LightGray),
    )
}
