package com.sparkstudios.cookware.data.remote.dto

data class CookResponse(
    val valid: Boolean? = null,
    val images: List<ImageResult> = emptyList(),
    val ingredients: List<String> = emptyList(),
    val recipes: List<RecipeResult> = emptyList()
)

data class ImageResult(
    val index: Int? = null,
    val valid: Boolean? = null,
    val ingredients: List<DetectedIngredient> = emptyList(),
    val reason: String? = null
)

data class DetectedIngredient(val name: String? = null, val confidence: Double? = null)

data class RecipeResult(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val cuisine: String? = null,
    val imageUrl: String? = null,
    val imageSourceUrl: String? = null,
    val usedIngredients: List<String> = emptyList(),
    val missingIngredients: List<String> = emptyList(),
    val optionalIngredients: List<String> = emptyList(),
    val timeMinutes: Int? = null,
    val difficulty: String? = null,
    val servings: Int? = null,
    val steps: List<String> = emptyList()
)
