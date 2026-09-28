package com.sparkstudios.cookware.data.repository

import android.content.Context
import android.graphics.BitmapFactory
import com.sparkstudios.cookware.data.remote.CookApi
import com.sparkstudios.cookware.data.remote.dto.CookRequest
import com.sparkstudios.cookware.data.remote.dto.ImageRequest
import com.sparkstudios.cookware.domain.model.Ingredient
import com.sparkstudios.cookware.domain.model.IngredientPhoto
import com.sparkstudios.cookware.domain.model.Cuisine
import com.sparkstudios.cookware.domain.model.Recipe
import com.sparkstudios.cookware.domain.repository.AnalysisResult
import com.sparkstudios.cookware.domain.repository.CookRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import android.util.Base64
import javax.inject.Inject
import android.util.Log

class CookRepositoryImpl @Inject constructor(
    private val api: CookApi,
    @ApplicationContext private val context: Context
) : CookRepository {
    override suspend fun analyze(photos: List<IngredientPhoto>, cuisine: Cuisine): AnalysisResult = withContext(Dispatchers.IO) {
        val payload = photos.map { photo ->
            val original = context.contentResolver.openInputStream(photo.uri)?.use { it.readBytes() }
                ?: error("Image is unavailable")
            val compressed = compress(original)
            Log.d("COOK_UPLOAD", "original=${original.size} compressed=${compressed.size}")
            ImageRequest(photo.mimeType, Base64.encodeToString(compressed, Base64.NO_WRAP))
        }
        val response = api.analyze(CookRequest(cuisine.apiValue, payload))
        val ingredients = response.ingredients.mapNotNull { it.trim().takeIf(String::isNotEmpty) }
            .distinctBy(String::lowercase)
            .map { Ingredient(it) }
        val invalid = response.images.mapNotNull { image ->
            val index = image.index
            if (index != null && image.valid == false) index to (image.reason ?: "Couldn't recognize an ingredient.") else null
        }.toMap()
        val recipeResults = response.recipes.asSequence().distinctBy { it.id }
        val recipes = recipeResults.mapNotNull { it.toDomainRecipe(cuisine.displayName) }.toList()
        AnalysisResult(ingredients, invalid, recipes)
    }

    private fun compress(bytes: ByteArray): ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return bytes
        return ByteArrayOutputStream().use { stream ->
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 82, stream)
            bitmap.recycle()
            stream.toByteArray()
        }
    }
}
