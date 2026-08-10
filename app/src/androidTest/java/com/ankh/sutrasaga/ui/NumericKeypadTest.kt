package com.ankh.sutrasaga.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ankh.sutrasaga.ui.components.NumericKeypad
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NumericKeypadTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testNumericKeypadRendersAllKeysAndFiresCallbacks() {
        var clickedDigit: Char? = null
        var isBackspaceClicked = false
        var isClearClicked = false
        var isSubmitClicked = false

        composeTestRule.setContent {
            NumericKeypad(
                onDigitClick = { clickedDigit = it },
                onBackspaceClick = { isBackspaceClicked = true },
                onClearClick = { isClearClicked = true },
                onSubmitClick = { isSubmitClicked = true }
            )
        }

        // Verify keys exist
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
        composeTestRule.onNodeWithText("5").assertIsDisplayed()
        composeTestRule.onNodeWithText("0").assertIsDisplayed()
        composeTestRule.onNodeWithText("CLR").assertIsDisplayed()
        composeTestRule.onNodeWithText("⌫").assertIsDisplayed()
        composeTestRule.onNodeWithText("SUBMIT").assertIsDisplayed()

        // Perform clicks
        composeTestRule.onNodeWithText("7").performClick()
        assertEquals('7', clickedDigit)

        composeTestRule.onNodeWithText("⌫").performClick()
        assertTrue(isBackspaceClicked)

        composeTestRule.onNodeWithText("CLR").performClick()
        assertTrue(isClearClicked)

        composeTestRule.onNodeWithText("SUBMIT").performClick()
        assertTrue(isSubmitClicked)
    }
}
