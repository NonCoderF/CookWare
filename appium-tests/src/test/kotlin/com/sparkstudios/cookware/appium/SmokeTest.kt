package com.sparkstudios.cookware.appium

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SmokeTest : BaseAppiumTest() {
    @Test
    fun launchesAndShowsMainCameraScreen() {
        val camera = CameraPage(driver).assertLoaded()
        assertTrue(camera.hasVisibleTag("camera_title"))
    }
}
