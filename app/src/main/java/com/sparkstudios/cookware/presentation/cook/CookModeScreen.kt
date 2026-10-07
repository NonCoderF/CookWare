package com.sparkstudios.cookware.presentation.cook

import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import com.sparkstudios.cookware.domain.model.Recipe
import com.sparkstudios.cookware.ui.theme.*

@Composable fun CookModeScreen(recipe: Recipe, onDone: () -> Unit) { val view = LocalView.current; val window = (view.context as? android.app.Activity)?.window; var step by remember { mutableIntStateOf(0) }; DisposableEffect(Unit) { window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); onDispose { window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) } }; Column(Modifier.fillMaxSize().testTag("cook_mode_screen").windowInsetsPadding(WindowInsets.safeDrawing).padding(28.dp), verticalArrangement = Arrangement.SpaceBetween) { Column(Modifier.padding(top = 20.dp)) { Text("Step ${step + 1} of ${recipe.steps.size}", color = Terracotta, style = MaterialTheme.typography.labelLarge); Spacer(Modifier.height(28.dp)); Text(recipe.steps.getOrElse(step) { "All done." }, style = MaterialTheme.typography.displaySmall, color = HerbGreen) }; Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) { OutlinedButton({ if (step > 0) step-- }, enabled = step > 0, modifier = Modifier.testTag("cook_previous").weight(1f).height(54.dp)) { Text("Previous") }; Button({ if (step == recipe.steps.lastIndex) onDone() else step++ }, modifier = Modifier.testTag(if (step == recipe.steps.lastIndex) "cook_done" else "cook_next").weight(1f).height(54.dp)) { Text(if (step == recipe.steps.lastIndex) "Done" else "Next") } } } }
