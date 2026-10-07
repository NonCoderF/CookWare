package com.sparkstudios.cookware.appium

import io.appium.java_client.android.AndroidDriver

class CameraPage(driver: AndroidDriver) : BasePage(driver) {
    fun assertLoaded(): CameraPage {
        visible("camera_screen")
        visible("camera_title")
        return this
    }

    fun continueToCuisine() {
        clickable("camera_continue").click()
    }
}
