package com.sparkstudios.cookware.presentation.recipe

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sparkstudios.cookware.domain.model.Recipe
import com.sparkstudios.cookware.ui.theme.HerbGreen
import com.sparkstudios.cookware.ui.theme.Muted
import com.sparkstudios.cookware.ui.theme.Terracotta

@Composable
fun RecipeDetailScreen(recipe: Recipe, onCook: () -> Unit) {
    val context = LocalContext.current
    val sourceUrl = recipe.imageSourceUrl?.takeIf(::isValidSourceUrl)

    LazyColumn(
        Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 28.dp)
    ) {
        item {
            RecipeImage(
                imageUrl = recipe.imageUrl,
                contentDescription = recipe.name,
                modifier = Modifier.fillMaxWidth().height(250.dp),
                shape = RoundedCornerShape(24.dp),
                cuisineLabel = recipe.cuisine
            )
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(recipe.name, style = MaterialTheme.typography.displaySmall, color = HerbGreen)
                Text(recipe.cuisine, color = Terracotta, style = MaterialTheme.typography.titleLarge)
                Text(
                    listOfNotNull(recipe.timeMinutes?.let { "$it min" }, recipe.difficulty, recipe.servings?.let { "Serves $it" }).joinToString("  •  "),
                    style = MaterialTheme.typography.labelLarge
                )
                if (sourceUrl != null) {
                    TextButton(onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(sourceUrl))
                        if (intent.resolveActivity(context.packageManager) != null) context.startActivity(intent)
                    }) { Text("Image source", color = HerbGreen) }
                }
                Text(recipe.description, color = Muted)
            }
        }
        item { Text("What you have", style = MaterialTheme.typography.headlineSmall, color = HerbGreen) }
        items(recipe.usedIngredients) { IngredientLine(it, true) }
        if (recipe.missingIngredients.isNotEmpty()) {
            item { Text("You'll also need", style = MaterialTheme.typography.headlineSmall, color = HerbGreen) }
            items(recipe.missingIngredients) { IngredientLine(it, false) }
        }
        if (recipe.optionalIngredients.isNotEmpty()) {
            item { Text("Optional", style = MaterialTheme.typography.headlineSmall, color = HerbGreen) }
            items(recipe.optionalIngredients) { Text(it.replaceFirstChar { c -> c.uppercase() }, color = Muted) }
        }
        item { Text("Let's cook", style = MaterialTheme.typography.headlineSmall, color = HerbGreen) }
        itemsIndexed(recipe.steps) { i, step ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.Top) {
                Surface(shape = RoundedCornerShape(50), color = Terracotta) {
                    Text("${i + 1}", color = Color.White, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                }
                Text(step, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            }
        }
        item { Button(onCook, Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(18.dp)) { Text("Start Cooking") } }
    }
}

@Composable
private fun IngredientLine(name: String, have: Boolean) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        if (have) Icon(Icons.Default.Check, null, tint = HerbGreen, modifier = Modifier.size(20.dp)) else Text("•", color = Terracotta)
        Text(name.replaceFirstChar { it.uppercase() })
    }
}
