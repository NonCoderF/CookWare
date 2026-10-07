package com.sparkstudios.cookware.presentation.cuisine

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sparkstudios.cookware.domain.model.Cuisine
import com.sparkstudios.cookware.presentation.camera.CameraUiState
import com.sparkstudios.cookware.presentation.camera.CameraViewModel
import com.sparkstudios.cookware.ui.theme.HerbGreen
import com.sparkstudios.cookware.ui.theme.Muted
import com.sparkstudios.cookware.ui.theme.Terracotta
import com.sparkstudios.cookware.ui.theme.WarmSurface

private val SelectedCuisineTint = Color(0xFFEAF4E9)

@Composable
fun CuisineScreen(viewModel: CameraViewModel, onSuccess: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }
    val cuisines = remember(query) {
        Cuisine.entries
            .filter { it != Cuisine.ANY }
            .filter { cuisine ->
                query.isBlank() || cuisine.displayName.contains(query.trim(), ignoreCase = true) ||
                    cuisine.shortDescription.contains(query.trim(), ignoreCase = true)
            }
    }

    LaunchedEffect(state.uiState) {
        if (state.uiState is CameraUiState.Success) onSuccess()
    }

    if (state.uiState is CameraUiState.Analyzing) {
        LoadingScreen()
    } else {
        Column(
            Modifier
                .fillMaxSize()
                .testTag("cuisine_screen")
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text("What are you craving?", style = MaterialTheme.typography.displaySmall, color = HerbGreen)
            Text("We'll make the most of what you have.", color = Muted, modifier = Modifier.padding(top = 6.dp))
            Spacer(Modifier.height(16.dp))

            CuisineCard(Cuisine.ANY, state.selectedCuisine == Cuisine.ANY, { viewModel.selectCuisine(Cuisine.ANY) }, featured = true)

            Text("Explore cuisines", style = MaterialTheme.typography.titleLarge, color = HerbGreen, modifier = Modifier.padding(top = 18.dp, bottom = 8.dp))
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.testTag("cuisine_search").fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Search cuisines", color = Muted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = HerbGreen) },
                shape = RoundedCornerShape(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f).padding(top = 12.dp),
                contentPadding = PaddingValues(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cuisines, key = { it.apiValue }) { cuisine ->
                    CuisineCard(cuisine, state.selectedCuisine == cuisine, { viewModel.selectCuisine(cuisine) })
                }
            }

            if (state.uiState is CameraUiState.Error) {
                Text((state.uiState as CameraUiState.Error).message, color = Terracotta, modifier = Modifier.padding(bottom = 8.dp))
            }
            Button(
                onClick = viewModel::analyze,
                enabled = state.selectedCuisine != null,
                modifier = Modifier.testTag("find_recipes").fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(18.dp)
            ) { Text("Find My Recipes") }
        }
    }
}

@Composable
private fun CuisineCard(cuisine: Cuisine, selected: Boolean, onClick: () -> Unit, featured: Boolean = false) {
    Card(
        onClick = onClick,
        modifier = Modifier.testTag("cuisine_card_${cuisine.apiValue}").fillMaxWidth().heightIn(min = if (featured) 92.dp else 104.dp),
        shape = RoundedCornerShape(if (featured) 22.dp else 18.dp),
        border = if (selected) BorderStroke(2.dp, HerbGreen) else null,
        colors = CardDefaults.cardColors(containerColor = if (selected) SelectedCuisineTint else WarmSurface),
        elevation = CardDefaults.cardElevation(if (selected) 4.dp else 1.dp)
    ) {
        Row(Modifier.padding(if (featured) 18.dp else 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(cuisine.emoji, style = MaterialTheme.typography.headlineMedium)
            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                Text(cuisine.displayName, style = if (featured) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium, color = HerbGreen, fontWeight = FontWeight.SemiBold)
                Text(cuisine.shortDescription, color = Muted, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 3.dp))
            }
            if (selected) Icon(Icons.Default.Check, contentDescription = "Selected", tint = HerbGreen, modifier = Modifier.size(22.dp))
        }
    }
}

@Composable
private fun LoadingScreen() {
    var message by remember { mutableStateOf(0) }
    val messages = listOf("Checking your ingredients…", "Looking through the kitchen…", "Finding the best dishes…", "Almost ready…")
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1800)
            message = (message + 1) % messages.size
        }
    }
    Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(28.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Icon(Icons.Default.Restaurant, null, tint = Terracotta, modifier = Modifier.size(56.dp))
        Spacer(Modifier.height(24.dp))
        AnimatedContent(messages[message], label = "loading") { Text(it, style = MaterialTheme.typography.headlineSmall, color = HerbGreen) }
        Text("Making the most of what you have.", color = Muted, modifier = Modifier.padding(top = 10.dp))
    }
}
