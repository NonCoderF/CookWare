package com.sparkstudios.cookware.appium

import io.appium.java_client.android.AndroidDriver

class CuisinePage(driver: AndroidDriver) : BasePage(driver) {
    fun search(query: String) {
        visible("cuisine_search").sendKeys(query)
    }

    fun selectCuisine(apiValue: String) {
        clickable("cuisine_card_$apiValue").click()
    }

    fun findRecipes() {
        clickable("find_recipes").click()
    }
}
