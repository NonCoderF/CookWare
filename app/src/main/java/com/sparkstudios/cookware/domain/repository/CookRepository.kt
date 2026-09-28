package com.sparkstudios.cookware.domain.repository

import com.sparkstudios.cookware.domain.model.Ingredient
import com.sparkstudios.cookware.domain.model.IngredientPhoto
import com.sparkstudios.cookware.domain.model.Cuisine
import com.sparkstudios.cookware.domain.model.Recipe

data class AnalysisResult(
    val ingredients: List<Ingredient>,
    val invalidPhotoReasons: Map<Int, String>,
    val recipes: List<Recipe>
)

interface CookRepository {
    suspend fun analyze(photos: List<IngredientPhoto>, cuisine: Cuisine): AnalysisResult
}
