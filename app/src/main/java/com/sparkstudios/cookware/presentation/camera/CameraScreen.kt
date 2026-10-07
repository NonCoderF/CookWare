package com.sparkstudios.cookware.presentation.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.indication
import androidx.compose.foundation.border
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.sparkstudios.cookware.domain.model.IngredientPhoto
import com.sparkstudios.cookware.ui.theme.*

private val CameraShape = RoundedCornerShape(24.dp)
private val PhotoTileShape = RoundedCornerShape(16.dp)

private enum class CameraFlashMode { OFF, ON }

@Composable
fun CameraScreen(viewModel: CameraViewModel, onContinue: () -> Unit) {
    val context = LocalContext.current; val owner = LocalLifecycleOwner.current; val state by viewModel.state.collectAsStateWithLifecycle()
    var capture by remember { mutableStateOf<ImageCapture?>(null) }; var ready by remember { mutableStateOf(false) }; var denied by remember { mutableStateOf(false) }; var review by remember { mutableStateOf<IngredientPhoto?>(null) }
    var flashMode by remember { mutableStateOf(CameraFlashMode.OFF) }
    var cameraHasFlash by remember { mutableStateOf(false) }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(cameraControl, flashMode) { cameraControl?.enableTorch(flashMode == CameraFlashMode.ON) }
    val cameraWeight by animateFloatAsState(
        targetValue = if (state.photos.isEmpty()) 1f else .82f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "cameraWeight"
    )
    val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { ready = it; denied = !it }
    LaunchedEffect(Unit) { if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) ready = true else permission.launch(Manifest.permission.CAMERA) }
    Column(Modifier.fillMaxSize().testTag("camera_screen").background(Cream).windowInsetsPadding(WindowInsets.safeDrawing).padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text("COOK\nWHAT I HAVE", style = MaterialTheme.typography.titleLarge, color = HerbGreen, lineHeight = 22.sp, modifier = Modifier.testTag("camera_title"))
        Text("Show me what's in your kitchen.", color = Muted, modifier = Modifier.padding(top = 6.dp, bottom = 14.dp))
        if (ready) CameraFrame(Modifier.fillMaxWidth().weight(cameraWeight), owner, state.photos.size < 10, flashMode, cameraHasFlash, { flashMode = it }, { image, control, hasFlash -> capture = image; cameraControl = control; cameraHasFlash = hasFlash }) { capture?.let { image -> val file = viewModel.newCaptureFile(); image.takePicture(ImageCapture.OutputFileOptions.Builder(file).build(), ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback { override fun onError(e: ImageCaptureException) {}; override fun onImageSaved(r: ImageCapture.OutputFileResults) { viewModel.addPhoto(Uri.fromFile(file)); haptic.performHapticFeedback(HapticFeedbackType.LongPress) } }) } } else PermissionCard(denied, Modifier.fillMaxWidth().weight(cameraWeight)) { if (denied) context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${context.packageName}"))) else permission.launch(Manifest.permission.CAMERA) }
        AnimatedVisibility(
            visible = state.photos.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(vertical = 14.dp)) {
                items(state.photos, key = { it.id }) { photo -> PhotoThumb(photo, { review = photo }) { viewModel.removePhoto(photo) } }
                items((state.photos.size until 10).toList(), key = { "empty-$it" }) { PhotoPlaceholder() }
            }
        }
        Button(onContinue, enabled = state.photos.isNotEmpty(), modifier = Modifier.testTag("camera_continue").fillMaxWidth().padding(top = 14.dp).height(56.dp), shape = RoundedCornerShape(18.dp)) { Text("Continue") }
        if (state.uiState is CameraUiState.Error) Text((state.uiState as CameraUiState.Error).message, color = Terracotta, modifier = Modifier.padding(10.dp))
    }
    review?.let { photo -> ReviewDialog(photo, { review = null }, { viewModel.removePhoto(photo); review = null }, { capture?.let { image -> val file = viewModel.newCaptureFile(); image.takePicture(ImageCapture.OutputFileOptions.Builder(file).build(), ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback { override fun onError(e: ImageCaptureException) {}; override fun onImageSaved(r: ImageCapture.OutputFileResults) { viewModel.replacePhoto(photo.id, Uri.fromFile(file)); review = null } }) } }) }
}

@Composable private fun CameraFrame(modifier: Modifier, owner: LifecycleOwner, enabled: Boolean, flashMode: CameraFlashMode, cameraHasFlash: Boolean, onFlashModeChange: (CameraFlashMode) -> Unit, onCapture: (ImageCapture, CameraControl, Boolean) -> Unit, onShutter: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) .88f else 1f, spring(stiffness = Spring.StiffnessMedium), label = "shutter")
    LaunchedEffect(pressed) { if (pressed) { kotlinx.coroutines.delay(130); pressed = false } }
    Box(modifier.clip(CameraShape).background(Color(0xFF152E26))) {
        CameraPreview(Modifier.fillMaxSize().clip(CameraShape), owner, onCapture)
        if (cameraHasFlash) FlashControl(flashMode, onFlashModeChange, Modifier.align(Alignment.TopEnd).padding(16.dp))
        Surface(modifier = Modifier.align(Alignment.BottomEnd).size(104.dp), shape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 24.dp), color = Cream, tonalElevation = 8.dp, shadowElevation = 6.dp) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Box(Modifier.testTag("camera_shutter").size(76.dp).scale(scale).clip(CircleShape).border(5.dp, HerbGreen, CircleShape).background(WarmSurface).clickable(enabled = enabled) { pressed = true; onShutter() }, contentAlignment = Alignment.Center) {
                    Box(Modifier.size(52.dp).clip(CircleShape).background(if (enabled) Terracotta else Muted).border(3.dp, Color.White.copy(alpha = .85f), CircleShape))
                }
            }
        }
    }
}

@Composable private fun FlashControl(mode: CameraFlashMode, onModeChange: (CameraFlashMode) -> Unit, modifier: Modifier = Modifier) {
    val icon = if (mode == CameraFlashMode.ON) Icons.Default.FlashOn else Icons.Default.FlashOff
    Surface(onClick = { onModeChange(if (mode == CameraFlashMode.ON) CameraFlashMode.OFF else CameraFlashMode.ON) }, modifier = modifier.testTag("camera_flash").size(52.dp), shape = RoundedCornerShape(18.dp), color = Color.Black.copy(alpha = .56f), contentColor = Color.White, tonalElevation = 4.dp) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = if (mode == CameraFlashMode.ON) "Turn flash off" else "Turn flash on", modifier = Modifier.size(22.dp))
        }
    }
}

@Composable private fun CameraPreview(modifier: Modifier, owner: LifecycleOwner, onCapture: (ImageCapture, CameraControl, Boolean) -> Unit) { val context = LocalContext.current; AndroidView(factory = { viewContext -> val view = PreviewView(viewContext); val future = ProcessCameraProvider.getInstance(context); future.addListener({ val provider = future.get(); val preview = Preview.Builder().build(); val image = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).setFlashMode(ImageCapture.FLASH_MODE_OFF).build(); preview.setSurfaceProvider(view.surfaceProvider); provider.unbindAll(); val camera = provider.bindToLifecycle(owner, CameraSelector.DEFAULT_BACK_CAMERA, preview, image); onCapture(image, camera.cameraControl, camera.cameraInfo.hasFlashUnit()) }, ContextCompat.getMainExecutor(context)); view }, modifier = modifier) }
@Composable private fun PhotoThumb(photo: IngredientPhoto, onClick: () -> Unit, onDelete: () -> Unit) { Box(Modifier.size(72.dp).clip(PhotoTileShape).background(if (photo.invalidReason == null) WarmSurface else Color(0xFFFFE2DB)).clickable(onClick = onClick)) { AsyncImage(photo.uri, "Captured ingredient", Modifier.fillMaxSize().clip(PhotoTileShape)); IconButton(onDelete, Modifier.align(Alignment.TopEnd).size(24.dp)) { Icon(Icons.Default.Close, "Remove", tint = Color.White, modifier = Modifier.background(Color.Black.copy(.55f), CircleShape).padding(3.dp)) }; if (photo.invalidReason != null) Icon(Icons.Default.WarningAmber, "Could not recognize", tint = Terracotta, modifier = Modifier.align(Alignment.BottomStart).padding(5.dp).size(18.dp)) } }
@Composable private fun PhotoPlaceholder() { Box(Modifier.size(72.dp).clip(PhotoTileShape).background(WarmSurface.copy(alpha = .65f)).border(1.dp, HerbGreen.copy(alpha = .14f), PhotoTileShape)) }
@Composable private fun PermissionCard(denied: Boolean, modifier: Modifier, action: () -> Unit) { Card(modifier, shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = WarmSurface)) { Column(Modifier.fillMaxSize().padding(28.dp), Arrangement.Center, Alignment.CenterHorizontally) { Icon(Icons.Default.AddAPhoto, null, tint = HerbGreen, modifier = Modifier.size(48.dp)); Text(if (denied) "Camera access is off" else "Camera access needed", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top = 18.dp)); Text(if (denied) "Open Settings to see your ingredients." else "We use your camera to photograph ingredients.", color = Muted, modifier = Modifier.padding(10.dp)); Button(action, modifier = Modifier.testTag("camera_permission_action")) { Text(if (denied) "Open Settings" else "Allow camera") } } } }
