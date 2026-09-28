package com.sparkstudios.cookware.domain.model

import android.net.Uri

data class IngredientPhoto(
    val id: String,
    val uri: Uri,
    val mimeType: String = "image/jpeg",
    val invalidReason: String? = null
)
