package com.sparkstudios.cookware.appium

import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import java.net.URI
import java.time.Duration

object DriverFactory {
    private const val DEFAULT_SERVER = "http://127.0.0.1:4723"
    private const val DEFAULT_DEVICE = "3c523b85"
    private const val DEFAULT_APK = "C:\\Users\\NIZAMUDDIN\\OneDrive\\Desktop\\app-debug.apk"

    fun create(): AndroidDriver {
        val options = UiAutomator2Options()
            .setPlatformName("Android")
            .setAutomationName("UiAutomator2")
            .setUdid(System.getProperty("appium.udid", DEFAULT_DEVICE))
            .setApp(System.getProperty("appium.apk", DEFAULT_APK))
            .setAppPackage("com.sparkstudios.findmyrecipe")
            .setAppActivity("com.sparkstudios.cookware.MainActivity")
            .setNoReset(true)
            .setAutoGrantPermissions(false)
            .setSkipDeviceInitialization(true)
            .setIgnoreHiddenApiPolicyError(true)

        return AndroidDriver(
            URI.create(System.getProperty("appium.server", DEFAULT_SERVER)).toURL(),
            options
        ).apply {
            manage().timeouts().implicitlyWait(Duration.ZERO)
        }
    }
}
