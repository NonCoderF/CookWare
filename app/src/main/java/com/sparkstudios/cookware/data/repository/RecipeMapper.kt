package com.sparkstudios.cookware.data.repository

import com.sparkstudios.cookware.data.remote.dto.RecipeResult
import com.sparkstudios.cookware.domain.model.Recipe
import com.sparkstudios.cookware.domain.model.RecipeImageRef

fun RecipeResult.toDomainRecipe(fallbackCuisine: String): Recipe? {
    val recipeId = id ?: return null
    return Recipe(
        id = recipeId,
        name = name.orEmpty().ifBlank { "Recipe idea" },
        description = description.orEmpty(),
        cuisine = cuisine?.takeIf(String::isNotBlank) ?: fallbackCuisine,
        imageUrl = imageUrl,
        imageSourceUrl = imageSourceUrl,
        images = images.map { RecipeImageRef(it.imageUrl, it.imageSourceUrl) },
        usedIngredients = usedIngredients,
        missingIngredients = missingIngredients,
        optionalIngredients = optionalIngredients,
        timeMinutes = timeMinutes,
        difficulty = difficulty ?: "Easy",
        servings = servings,
        steps = steps
    )
}
