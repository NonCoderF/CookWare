package com.sparkstudios.cookware.presentation.recipe

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import com.sparkstudios.cookware.domain.model.Recipe
import com.sparkstudios.cookware.ui.theme.*

@Composable fun RecipeDetailScreen(recipe: Recipe, onCook: () -> Unit) { LazyColumn(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(top = 20.dp, bottom = 28.dp)) { item { Text(recipe.name, style = MaterialTheme.typography.displaySmall, color = HerbGreen); Text(recipe.description, color = Muted); Text(listOfNotNull(recipe.timeMinutes?.let { "$it min" }, recipe.difficulty, recipe.servings?.let { "Serves $it" }).joinToString("  •  "), style = MaterialTheme.typography.labelLarge) }; item { Text("What you have", style = MaterialTheme.typography.headlineSmall, color = HerbGreen) }; items(recipe.usedIngredients) { IngredientLine(it, true) }; if (recipe.missingIngredients.isNotEmpty()) { item { Text("You'll also need", style = MaterialTheme.typography.headlineSmall, color = HerbGreen) }; items(recipe.missingIngredients) { IngredientLine(it, false) } }; if (recipe.optionalIngredients.isNotEmpty()) { item { Text("Optional", style = MaterialTheme.typography.headlineSmall, color = HerbGreen) }; items(recipe.optionalIngredients) { Text(it.replaceFirstChar { c -> c.uppercase() }, color = Muted) } }; item { Text("Let's cook", style = MaterialTheme.typography.headlineSmall, color = HerbGreen) }; itemsIndexed(recipe.steps) { i, step -> Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) { Surface(shape = RoundedCornerShape(50), color = Terracotta) { Text("${i + 1}", color = androidx.compose.ui.graphics.Color.White, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) }; Text(step, style = MaterialTheme.typography.bodyLarge) } }; item { Button(onCook, Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(18.dp)) { Text("Start Cooking") } } } }
@Composable private fun IngredientLine(name: String, have: Boolean) { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { if (have) Icon(Icons.Default.Check, null, tint = HerbGreen) else Text("•", color = Terracotta); Text(name.replaceFirstChar { it.uppercase() }) } }
