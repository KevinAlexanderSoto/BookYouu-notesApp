package com.kalex.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaseLineProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generateRule() = rule.collect(
        "com.kalex.bookyouu_notesapp",
    ) {
        startActivityAndWait()
        device.wait(
            Until.hasObject(By.text("Your subjects")),
            30_000,
        )
    }
}
