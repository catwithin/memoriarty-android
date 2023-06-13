package com.gamesofni.memoriarty.overview

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import androidx.compose.foundation.Image
import com.gamesofni.memoriarty.R


@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
) {
    GifImage(
        modifier = modifier,
        imageID = R.drawable.favicon,
    )
}

@Composable
private fun GifImage(
    modifier: Modifier = Modifier,
    imageID: Int,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image (
        painter =  rememberAsyncImagePainter(
            imageLoader = imageLoader,
            model = imageID,
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
    )
}
