package com.sparkstudios.cookware.presentation.camera
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sparkstudios.cookware.domain.model.IngredientPhoto
import com.sparkstudios.cookware.ui.theme.Terracotta
@Composable fun ReviewDialog(photo: IngredientPhoto, onDismiss: () -> Unit, onDelete: () -> Unit, onRetake: () -> Unit) { AlertDialog(onDismissRequest = onDismiss, title = { Text("Review ingredient") }, text = { Column { AsyncImage(photo.uri, "Ingredient photo", Modifier.fillMaxWidth().height(280.dp)); if (photo.invalidReason != null) Text(photo.invalidReason, color = Terracotta, modifier = Modifier.padding(top = 10.dp)) } }, confirmButton = { TextButton(onClick = onDelete) { Text("Delete") } }, dismissButton = { Row { TextButton(onClick = onRetake) { Text("Retake") }; TextButton(onClick = onDismiss) { Text("Done") } } }) }
