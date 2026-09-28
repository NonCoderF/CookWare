package com.sparkstudios.cookware.domain.model

data class Recipe(
    val id: String,
    val name: String,
    val description: String,
    val cuisine: String,
    val usedIngredients: List<String>,
    val missingIngredients: List<String>,
    val optionalIngredients: List<String>,
    val timeMinutes: Int?,
    val difficulty: String,
    val servings: Int?,
    val steps: List<String>
)
