package com.sparkstudios.cookware.presentation.camera
import com.sparkstudios.cookware.domain.model.*
sealed interface CameraUiState { data object Idle : CameraUiState; data object Analyzing : CameraUiState; data class Success(val invalidIndexes: Map<Int, String>) : CameraUiState; data class Error(val message: String) : CameraUiState }
data class CameraState(val photos: List<IngredientPhoto> = emptyList(), val selectedCuisine: Cuisine? = null, val uiState: CameraUiState = CameraUiState.Idle, val detectedIngredients: List<Ingredient> = emptyList(), val recipes: List<Recipe> = emptyList(), val activeRecipeId: String? = null)
