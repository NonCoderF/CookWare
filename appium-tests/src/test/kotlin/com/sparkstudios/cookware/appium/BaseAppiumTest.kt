package com.sparkstudios.cookware.appium

import io.appium.java_client.android.AndroidDriver
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class BaseAppiumTest {
    protected lateinit var driver: AndroidDriver

    @BeforeEach
    fun startDriver() {
        driver = DriverFactory.create()
    }

    @AfterEach
    fun stopDriver() {
        if (::driver.isInitialized) driver.quit()
    }
}
