package com.sparkstudios.cookware.appium

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

abstract class BasePage(protected val driver: AndroidDriver) {
    private val wait = WebDriverWait(driver, Duration.ofSeconds(20))

    protected fun byTestTag(tag: String) = AppiumBy.accessibilityId(tag)

    protected fun visible(tag: String): WebElement =
        wait.until(ExpectedConditions.visibilityOfElementLocated(byTestTag(tag)))

    protected fun clickable(tag: String): WebElement =
        wait.until(ExpectedConditions.elementToBeClickable(byTestTag(tag)))

    fun hasVisibleTag(tag: String): Boolean = runCatching { visible(tag); true }.getOrDefault(false)
}
