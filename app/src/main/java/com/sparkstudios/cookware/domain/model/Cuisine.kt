package com.sparkstudios.cookware.domain.model

enum class Cuisine(
    val displayName: String,
    val apiValue: String,
    val shortDescription: String,
    val emoji: String
) {
    ANY("Any Cuisine", "any", "Show me everything I can make", "✨"),
    INDIAN("Indian", "indian", "Comforting classics & everyday favourites", "🍛"),
    ASSAMESE("Assamese", "assamese", "Simple, fresh flavours from Assam", "🌿"),
    CHINESE("Chinese", "chinese", "Quick stir-fries & bold flavours", "🥡"),
    JAPANESE("Japanese", "japanese", "Simple, balanced & umami-rich", "🍣"),
    KOREAN("Korean", "korean", "Bold, fermented & deeply savoury", "🥢"),
    THAI("Thai", "thai", "Bright, fragrant & perfectly spicy", "🌶️"),
    ITALIAN("Italian", "italian", "Rustic classics & comforting favourites", "🍝"),
    MEXICAN("Mexican", "mexican", "Vibrant, zesty & full of flavour", "🌮"),
    RUSSIAN("Russian", "russian", "Hearty, warming & homestyle", "🥣"),
    FRENCH("French", "french", "Elegant, buttery & beautifully simple", "🥐"),
    MEDITERRANEAN("Mediterranean", "mediterranean", "Fresh, sunny & ingredient-led", "🫒"),
    MIDDLE_EASTERN("Middle Eastern", "middle_eastern", "Aromatic, generous & spice-rich", "🧆"),
    VIETNAMESE("Vietnamese", "vietnamese", "Fresh, herbaceous & balanced", "🍜"),
    AMERICAN("American", "american", "Familiar favourites with a twist", "🍔");

    companion object {
        fun fromApiValue(value: String?): Cuisine? = entries.firstOrNull { it.apiValue.equals(value, ignoreCase = true) }
    }
}
