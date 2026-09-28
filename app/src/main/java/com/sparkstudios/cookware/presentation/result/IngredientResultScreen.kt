package com.sparkstudios.cookware.presentation.result

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sparkstudios.cookware.domain.model.Recipe
import com.sparkstudios.cookware.presentation.camera.CameraViewModel
import com.sparkstudios.cookware.ui.theme.*

@Composable fun IngredientResultScreen(viewModel: CameraViewModel, onRecipe: (String) -> Unit, onAddMore: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LazyColumn(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(top = 20.dp, bottom = 24.dp)) {
        item { Spacer(Modifier.height(18.dp)); Text("Here's what you can cook", style = MaterialTheme.typography.displaySmall, color = HerbGreen); Text(state.selectedCuisine?.displayName.orEmpty(), color = Terracotta, style = MaterialTheme.typography.titleLarge); Text("Using what you have", color = Muted, modifier = Modifier.padding(top = 14.dp)) }
        item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { state.detectedIngredients.forEach { AssistChip(onClick = {}, label = { Text(it.name.replaceFirstChar { c -> c.uppercase() }) }, shape = RoundedCornerShape(12.dp)) } } }
        item { Text("Recipe ideas", style = MaterialTheme.typography.headlineSmall, color = HerbGreen) }
        if (state.photos.any { it.invalidReason != null }) item { Text("${state.photos.count { it.invalidReason != null }} photo couldn't be recognized.", color = Terracotta) }
        if (state.recipes.isEmpty()) item { Text("We found your ingredients, but couldn't find a good match for ${state.selectedCuisine?.displayName ?: "this cuisine"}.", color = Muted) }
        items(state.recipes, key = { it.id }) { recipe -> RecipeCard(recipe) { viewModel.setActiveRecipe(recipe.id); onRecipe(recipe.id) } }
        item { OutlinedButton(onAddMore, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(18.dp)) { Text("Add More Ingredients") } }
    }
}
@Composable private fun RecipeCard(recipe: Recipe, onClick: () -> Unit) { Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = WarmSurface), elevation = CardDefaults.cardElevation(3.dp)) { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) { Text(recipe.name, style = MaterialTheme.typography.headlineSmall, color = HerbGreen); Text(recipe.description, color = Muted); Text(listOfNotNull(recipe.timeMinutes?.let { "$it min" }, recipe.difficulty, recipe.servings?.let { "Serves $it" }).joinToString("  •  "), style = MaterialTheme.typography.labelLarge); Row { Icon(Icons.Default.CheckCircle, null, tint = HerbGreen, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text(if (recipe.missingIngredients.isEmpty()) "Everything you need is here" else "+ ${recipe.missingIngredients.size} ingredient needed", color = HerbGreen, style = MaterialTheme.typography.labelLarge) }; Button(onClick) { Text("View Recipe") } } } }
