package com.sparkstudios.cookware.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
private val CookScheme = lightColorScheme(primary = HerbGreen, onPrimary = Cream, secondary = Terracotta, onSecondary = Cream, background = Cream, surface = WarmSurface, onBackground = Charcoal, onSurface = Charcoal, outline = androidx.compose.ui.graphics.Color(0xFFE3DCD0))
@Composable fun FindMyRecipeTheme(content: @Composable () -> Unit) { MaterialTheme(colorScheme = CookScheme, typography = Typography, content = content) }
