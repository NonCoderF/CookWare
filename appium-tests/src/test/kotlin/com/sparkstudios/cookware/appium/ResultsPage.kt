package com.sparkstudios.cookware.appium

import io.appium.java_client.android.AndroidDriver

class ResultsPage(driver: AndroidDriver) : BasePage(driver) {
    fun assertLoaded(): ResultsPage {
        visible("results_screen")
        return this
    }

    fun openRecipe(recipeId: String) {
        clickable("view_recipe_$recipeId").click()
    }
}
