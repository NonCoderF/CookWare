package com.sparkstudios.cookware.presentation.camera

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparkstudios.cookware.domain.model.*
import com.sparkstudios.cookware.domain.repository.CookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(private val repository: CookRepository, @ApplicationContext private val context: Context) : ViewModel() {
    private val _state = MutableStateFlow(CameraState()); val state: StateFlow<CameraState> = _state.asStateFlow()
    fun addPhoto(uri: Uri) { if (_state.value.photos.size < 10) _state.value = _state.value.copy(photos = _state.value.photos + IngredientPhoto(System.nanoTime().toString(), uri), uiState = CameraUiState.Idle) }
    fun replacePhoto(id: String, uri: Uri) { _state.value = _state.value.copy(photos = _state.value.photos.map { if (it.id == id) it.copy(uri = uri, invalidReason = null) else it }) }
    fun removePhoto(photo: IngredientPhoto) { _state.value = _state.value.copy(photos = _state.value.photos.filterNot { it.id == photo.id }); runCatching { File(photo.uri.path ?: "").delete() } }
    fun selectCuisine(cuisine: Cuisine) { _state.value = _state.value.copy(selectedCuisine = cuisine); Log.d("COOK_CAPTURE", "cuisine=${cuisine.apiValue}") }
    fun newCaptureFile(): File = File.createTempFile("cook_", ".jpg", context.cacheDir)
    fun recipe(id: String): Recipe? = _state.value.recipes.firstOrNull { it.id == id }
    fun setActiveRecipe(id: String) { _state.value = _state.value.copy(activeRecipeId = id) }
    fun clearError() { if (_state.value.uiState is CameraUiState.Error) _state.value = _state.value.copy(uiState = CameraUiState.Idle) }
    fun analyze() {
        val current = _state.value; val cuisine = current.selectedCuisine ?: return
        if (current.photos.isEmpty() || current.uiState is CameraUiState.Analyzing) return
        _state.value = current.copy(uiState = CameraUiState.Analyzing)
        viewModelScope.launch {
            try {
                Log.d("COOK_API", "upload start images=${current.photos.size} cuisine=${cuisine.apiValue}")
                val result = repository.analyze(current.photos, cuisine)
                _state.value = _state.value.copy(uiState = CameraUiState.Success(result.invalidPhotoReasons), detectedIngredients = result.ingredients, recipes = result.recipes, photos = _state.value.photos.mapIndexed { i, p -> p.copy(invalidReason = result.invalidPhotoReasons[i]) })
                Log.d("COOK_RESULT", "detected=${result.ingredients.size} recipes=${result.recipes.size}")
            } catch (e: Exception) {
                Log.e("COOK_API", "analysis failed", e)
                _state.value = _state.value.copy(uiState = CameraUiState.Error(when (e) { is SocketTimeoutException -> "The kitchen is taking a little longer. Try again."; is ConnectException -> "You're offline. Check your connection and try again."; is HttpException -> when (e.code()) { 400 -> "We couldn't understand those photos."; 401 -> "Your session needs to be refreshed."; 402, 500 -> "Our kitchen hit a snag. Try again."; else -> "Our kitchen hit a snag. Try again." }; else -> "Our kitchen hit a snag. Try again." }))
            }
        }
    }
}
