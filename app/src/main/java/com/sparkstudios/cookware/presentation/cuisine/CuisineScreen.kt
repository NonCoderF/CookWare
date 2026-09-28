package com.sparkstudios.cookware.presentation.cuisine

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sparkstudios.cookware.domain.model.Cuisine
import com.sparkstudios.cookware.presentation.camera.CameraUiState
import com.sparkstudios.cookware.presentation.camera.CameraViewModel
import com.sparkstudios.cookware.ui.theme.*

@Composable fun CuisineScreen(viewModel: CameraViewModel, onSuccess: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.uiState) { if (state.uiState is CameraUiState.Success) onSuccess() }
    if (state.uiState is CameraUiState.Analyzing) LoadingScreen() else Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("What are you craving?", style = MaterialTheme.typography.displaySmall, color = HerbGreen); Text("We'll make the most of what you have.", color = Muted); Spacer(Modifier.height(8.dp))
        Cuisine.entries.forEach { cuisine -> CuisineCard(cuisine, state.selectedCuisine == cuisine) { viewModel.selectCuisine(cuisine) } }
        Spacer(Modifier.weight(1f)); Button(viewModel::analyze, enabled = state.selectedCuisine != null, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(18.dp)) { Text("Find My Recipes") }
        if (state.uiState is CameraUiState.Error) Text((state.uiState as CameraUiState.Error).message, color = Terracotta)
    }
}
@Composable private fun CuisineCard(cuisine: Cuisine, selected: Boolean, onClick: () -> Unit) { Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, Terracotta) else null, colors = CardDefaults.cardColors(containerColor = if (selected) ColorTint else WarmSurface), elevation = CardDefaults.cardElevation(if (selected) 5.dp else 1.dp)) { Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Restaurant, null, tint = Terracotta, modifier = Modifier.size(30.dp)); Column(Modifier.weight(1f).padding(horizontal = 16.dp)) { Text(cuisine.displayName.uppercase(), style = MaterialTheme.typography.titleLarge, color = HerbGreen); Text(cuisine.tagline, color = Muted) }; if (selected) Icon(Icons.Default.Check, "Selected", tint = HerbGreen) } } }
@Composable private fun LoadingScreen() { var message by remember { mutableIntStateOf(0) }; val messages = listOf("Checking your ingredients…", "Looking through the kitchen…", "Finding the best dishes…", "Almost ready…"); LaunchedEffect(Unit) { while (true) { kotlinx.coroutines.delay(1800); message = (message + 1) % messages.size } }; Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(28.dp), Arrangement.Center, Alignment.CenterHorizontally) { Icon(Icons.Default.Restaurant, null, tint = Terracotta, modifier = Modifier.size(56.dp)); Spacer(Modifier.height(24.dp)); AnimatedContent(messages[message], label = "loading") { Text(it, style = MaterialTheme.typography.headlineSmall, color = HerbGreen) }; Text("Making the most of what you have.", color = Muted, modifier = Modifier.padding(top = 10.dp)) } }
private val ColorTint = androidx.compose.ui.graphics.Color(0xFFFFEEE8)
