package com.betanooblabs.geoguesserandroidclone.screens.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import com.betanooblabs.geoguesserandroidclone.R

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun GifImage(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(ImageDecoderDecoder.Factory())
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(
            model = R.drawable.worldmap,
            imageLoader = imageLoader
        ),
        contentDescription = "Top GIF",
        modifier = modifier
    )
}
