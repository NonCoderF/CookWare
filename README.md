# FindMyRecipe

<div align="center">

<img src="docs/assets/cookware-icon.png" alt="FindMyRecipe app icon" width="180" />

### Turn what you have into something delicious.

FindMyRecipe is a friendly Android cooking assistant that uses your camera to recognize ingredients and suggest recipes you can actually make.

[![Android](https://img.shields.io/badge/Android-API%2026%2B-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.x-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/compose)
[![CameraX](https://img.shields.io/badge/Camera-CameraX-FF6F00)](https://developer.android.com/training/camerax)

</div>

## The idea

Open the camera, snap the ingredients in your kitchen, choose a cuisine, and let FindMyRecipe turn the moment into recipe inspiration. The experience is designed to feel quick, warm, and genuinely useful—even when your fridge is looking random.

## What makes it special

|  | Feature | Why it matters |
|---|---|---|
| 📸 | Camera-first ingredient capture | Photograph what you already have instead of typing a shopping list. |
| 🥕 | Ingredient recognition | Get feedback for each photo, including items that need another look. |
| 🌶️ | Cuisine-aware recipes | Choose a cuisine and get ideas tailored to that direction. |
| ✨ | Smooth, friendly UI | Rounded surfaces, responsive transitions, and a calm kitchen-inspired palette. |
| 🍳 | Step-by-step cooking mode | Move from recipe discovery to actually making the dish. |

## Visual direction

FindMyRecipe uses a warm kitchen palette: herb green for freshness, cream for comfort, and terracotta for energy. The app icon combines a cooking pot with a camera lens to make the product promise instantly recognizable.

## Tech stack

- Kotlin
- Jetpack Compose + Material 3
- CameraX
- Hilt
- Retrofit and Gson
- Coil
- Kotlin Coroutines and StateFlow

## Run locally

1. Open the project in Android Studio.
2. Add your backend configuration as required by `NetworkModule`.
3. Connect an Android device or start an emulator with camera support.
4. Run the `app` configuration.

Or build from the project root:

```powershell
.\gradlew.bat assembleDebug
```

The debug APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Project structure

```text
app/src/main/java/com/sparkstudios/cookware/
├── data/          # API models and repository implementation
├── domain/        # App models and repository contracts
├── navigation/    # Compose navigation graph
├── presentation/  # Camera, cuisine, recipe, and cooking screens
└── ui/theme/      # Colors, typography, and app theme
```

## Status

FindMyRecipe is an actively evolving prototype focused on making ingredient capture and recipe discovery feel effortless.

<div align="center">

Made with curiosity, cameras, and whatever is left in the fridge.

</div>
