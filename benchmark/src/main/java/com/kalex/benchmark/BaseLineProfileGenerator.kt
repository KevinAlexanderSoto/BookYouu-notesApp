package com.kalex.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class BaseLineProfileGenerator {
    @OptIn(ExperimentalBaselineProfilesApi::class)
    @get:Rule
    val rule = BaselineProfileRule()

    @OptIn(ExperimentalBaselineProfilesApi::class)
    @Test
    fun generateRule() = rule.collectBaselineProfile(
        "com.kalex.bookyouu_notesapp",
    ) {
        startActivityAndWait()
        device.wait(
            Until.hasObject(By.text("Your subjects")),
            30_000,
        )
    }
}
