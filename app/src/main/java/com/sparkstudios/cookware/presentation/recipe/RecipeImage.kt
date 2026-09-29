package com.sparkstudios.cookware.presentation.recipe

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.sparkstudios.cookware.ui.theme.Cream
import com.sparkstudios.cookware.ui.theme.HerbGreen
import com.sparkstudios.cookware.ui.theme.Muted
import com.sparkstudios.cookware.ui.theme.Terracotta
import com.sparkstudios.cookware.domain.model.RecipeImageRef

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
                    .build()
            }
    }

    Box(modifier.clip(shape).background(Cream)) {
        if (request == null) {
            RecipeImagePlaceholder(Modifier.fillMaxSize())
        } else {
            SubcomposeAsyncImage(
                model = request,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = { RecipeImagePlaceholder(Modifier.fillMaxSize()) },
                error = {
                    Log.w("RECIPE_IMAGE", "Unable to load recipe image: $imageUrl")
                    RecipeImagePlaceholder(Modifier.fillMaxSize())
                },
                onSuccess = { Log.d("RECIPE_IMAGE", "Loaded recipe image: $imageUrl") }
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
fun RecipeImageCarousel(
    images: List<RecipeImageRef>,
    contentDescription: String?,
    modifier: Modifier,
    shape: Shape,
    cuisineLabel: String? = null,
    onImageSelected: (RecipeImageRef) -> Unit = {}
) {
    val items = if (images.isEmpty()) listOf(RecipeImageRef(null, null)) else images
    val listState = rememberLazyListState()
    var selectedIndex by remember(items) { mutableIntStateOf(0) }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        selectedIndex = listState.firstVisibleItemIndex.coerceIn(items.indices)
        onImageSelected(items[selectedIndex])
    }

    BoxWithConstraints(modifier.clip(shape)) {
        val pageWidth = maxWidth
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = items.size > 1
        ) {
            itemsIndexed(items) { index, image ->
                RecipeImage(
                    imageUrl = image.imageUrl,
                    contentDescription = contentDescription?.let { "$it image ${index + 1}" },
                    modifier = Modifier.width(pageWidth).fillMaxHeight(),
                    shape = shape,
                    cuisineLabel = cuisineLabel
                )
            }
        }
        if (items.size > 1) {
            Row(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items.forEachIndexed { index, _ ->
                    Box(
                        Modifier
                            .size(if (index == selectedIndex) 8.dp else 6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(if (index == selectedIndex) Color.White else Color.White.copy(alpha = .55f))
                    )
                }
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
