package com.sparkstudios.cookware.data.remote.dto

data class CookRequest(val cuisine: String, val images: List<ImageRequest>)

data class ImageRequest(val mimeType: String, val base64: String)
