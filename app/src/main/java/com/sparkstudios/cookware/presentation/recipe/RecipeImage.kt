package com.sparkstudios.cookware.presentation.recipe

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.sparkstudios.cookware.ui.theme.Cream
import com.sparkstudios.cookware.ui.theme.HerbGreen
import com.sparkstudios.cookware.ui.theme.Muted
import com.sparkstudios.cookware.ui.theme.Terracotta

@Composable
fun RecipeImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier,
    shape: Shape,
    cuisineLabel: String? = null
) {
    val context = LocalContext.current
    val request = remember(imageUrl) {
        imageUrl
            ?.takeIf(::isLoadableImageUrl)
            ?.let {
                ImageRequest.Builder(context)
                    .data(it)
                    .crossfade(true)
                    .size(1000)
                    .build()
            }
    }
    val painter = rememberAsyncImagePainter(model = request)
    val imageVisible = painter.state is AsyncImagePainter.State.Success
    val imageAlpha by animateFloatAsState(
        targetValue = if (imageVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "recipeImageFade"
    )

    Box(modifier.clip(shape).background(Cream)) {
        RecipeImagePlaceholder(Modifier.fillMaxSize())
        if (request != null) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize().alpha(imageAlpha),
                contentScale = ContentScale.Crop
            )
        }
        if (!cuisineLabel.isNullOrBlank()) {
            Surface(
                modifier = Modifier.align(Alignment.BottomStart).padding(12.dp),
                color = Color.Black.copy(alpha = .52f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(cuisineLabel, color = Color.White, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
            }
        }
    }
}

@Composable
private fun RecipeImagePlaceholder(modifier: Modifier) {
    Box(modifier.background(HerbGreen.copy(alpha = .12f)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Restaurant, contentDescription = null, tint = Terracotta, modifier = Modifier.size(42.dp))
            Spacer(Modifier.size(6.dp))
            Text("Cooking up something good", color = Muted, style = MaterialTheme.typography.labelMedium)
        }
    }
}

fun isLoadableImageUrl(value: String): Boolean {
    val uri = runCatching { Uri.parse(value) }.getOrNull() ?: return false
    return uri.scheme.equals("https", ignoreCase = true) && !uri.host.isNullOrBlank()
}

fun isValidSourceUrl(value: String): Boolean {
    val uri = runCatching { Uri.parse(value) }.getOrNull() ?: return false
    return (uri.scheme.equals("https", ignoreCase = true) || uri.scheme.equals("http", ignoreCase = true)) && !uri.host.isNullOrBlank()
}
