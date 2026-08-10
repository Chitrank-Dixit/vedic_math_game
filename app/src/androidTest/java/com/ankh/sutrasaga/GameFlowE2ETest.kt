package com.ankh.sutrasaga

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameFlowE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testFullGameFlowFromWorldSelectToPracticeArena() {
        // 1. Assert World Select Screen renders title
        composeTestRule.onNodeWithText("Ankh: The Sutra Saga").assertIsDisplayed()
        composeTestRule.onNodeWithText("World 1: Ekadhikena Purvena").assertIsDisplayed()

        // 2. Click Play on World 1
        composeTestRule.onAllNodesWithText("Play").onFirst().performClick()

        // 3. Assert Story Beat Screen loads
        composeTestRule.onNodeWithText("World 1: The Fateful Five").assertIsDisplayed()

        // 4. Skip Story Beat
        composeTestRule.onNodeWithText("Skip").performClick()

        // 5. Assert Tutorial Screen loads
        composeTestRule.onNodeWithText("Tutorial 1: Ekadhikena Purvena").assertIsDisplayed()

        // 6. Skip Tutorial
        composeTestRule.onNodeWithText("Skip").performClick()

        // 7. Assert Practice Arena loads with keypad
        composeTestRule.onNodeWithText("PRACTICE ARENA").assertIsDisplayed()
        composeTestRule.onNodeWithText("SUBMIT").assertIsDisplayed()
    }
}
