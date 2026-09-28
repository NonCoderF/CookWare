package com.sparkstudios.cookware.domain.model

enum class Cuisine(val apiValue: String, val displayName: String, val tagline: String) {
    INDIAN("indian", "Indian", "Comforting classics & everyday favourites"),
    CHINESE("chinese", "Chinese", "Quick stir-fries & bold flavours"),
    ASSAMESE("assamese", "Assamese", "Simple, fresh flavours from Assam")
}
