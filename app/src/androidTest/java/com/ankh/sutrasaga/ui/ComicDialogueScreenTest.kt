package com.ankh.sutrasaga.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ankh.sutrasaga.data.repository.SutraDialogueRepository
import com.ankh.sutrasaga.ui.components.ComicDialogueScreen
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComicDialogueScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testComicDialogueScreenRendersTitleAndPanels() {
        val script = SutraDialogueRepository.getStoryBeatScript(1)
        var isCompleted = false

        composeTestRule.setContent {
            ComicDialogueScreen(
                script = script,
                onComplete = { isCompleted = true }
            )
        }

        // Verify title is rendered
        composeTestRule.onNodeWithText("World 1: The Fateful Five").assertIsDisplayed()

        // Verify initial panel text
        composeTestRule.onNodeWithText("Welcome to Math-Loka! The Number Demons stripped the world of mental shortcuts. Armed only with your wits, you must find the 16 Sutra-Masters.").assertIsDisplayed()

        // Click Skip button
        composeTestRule.onNodeWithText("Skip").performClick()
        assertTrue(isCompleted)
    }

    @Test
    fun testComicDialogueScreenAdvancesPanelOnTap() {
        val script = SutraDialogueRepository.getStoryBeatScript(1)

        composeTestRule.setContent {
            ComicDialogueScreen(
                script = script,
                onComplete = {}
            )
        }

        // Tap screen to advance to panel 2
        composeTestRule.onNodeWithText("Welcome to Math-Loka! The Number Demons stripped the world of mental shortcuts. Armed only with your wits, you must find the 16 Sutra-Masters.").performClick()

        // Verify panel 2 text appears
        composeTestRule.onNodeWithText("Wait, we have to do mental arithmetic without battery power?! My solar panel is crying!").assertIsDisplayed()
    }
}
