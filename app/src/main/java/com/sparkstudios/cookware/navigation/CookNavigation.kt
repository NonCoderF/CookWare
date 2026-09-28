package com.sparkstudios.cookware.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sparkstudios.cookware.presentation.camera.*
import com.sparkstudios.cookware.presentation.cuisine.CuisineScreen
import com.sparkstudios.cookware.presentation.result.IngredientResultScreen
import com.sparkstudios.cookware.presentation.recipe.RecipeDetailScreen
import com.sparkstudios.cookware.presentation.cook.CookModeScreen

@Composable fun CookNavigation() {
    val nav = rememberNavController(); val vm: CameraViewModel = hiltViewModel(); val state by vm.state.collectAsStateWithLifecycle()
    NavHost(nav, "camera") {
        composable("camera") { CameraScreen(vm) { nav.navigate("cuisine") } }
        composable("cuisine") { CuisineScreen(vm) { nav.navigate("recipes") { popUpTo("cuisine") { inclusive = true } } } }
        composable("recipes") { IngredientResultScreen(vm, { id -> vm.setActiveRecipe(id); nav.navigate("detail/$id") }, { nav.popBackStack("camera", false) }) }
        composable("detail/{id}") { entry -> val id = entry.arguments?.getString("id").orEmpty(); vm.recipe(id)?.let { RecipeDetailScreen(it) { nav.navigate("cook/$id") } } }
        composable("cook/{id}") { entry ->
            val id = entry.arguments?.getString("id").orEmpty()
            vm.recipe(id)?.let {
                CookModeScreen(it) {
                    vm.resetSession()
                    nav.navigate("camera") {
                        popUpTo("camera") { inclusive = true }
                    }
                }
            }
        }
    }
}
