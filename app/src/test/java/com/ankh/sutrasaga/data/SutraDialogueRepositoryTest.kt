package com.ankh.sutrasaga.data

import com.ankh.sutrasaga.data.repository.SutraDialogueRepository
import com.ankh.sutrasaga.domain.models.Speaker
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SutraDialogueRepositoryTest {

    @Test
    fun testWorld1StoryBeatScriptIntegrity() {
        val script = SutraDialogueRepository.getStoryBeatScript(1)
        assertEquals(1, script.worldId)
        assertEquals("World 1: The Fateful Five", script.title)
        assertTrue("Story script should contain panels", script.panels.isNotEmpty())

        for (panel in script.panels) {
            assertTrue("Panel text must not be blank", panel.text.isNotBlank())
            assertNotNull("Speaker must be defined", panel.speaker)
            if (panel.highlightMathToken != null) {
                assertTrue(
                    "Highlight token '${panel.highlightMathToken}' must exist inside panel text",
                    panel.text.contains(panel.highlightMathToken!!)
                )
            }
        }
    }

    @Test
    fun testWorld1TutorialScriptIntegrity() {
        val script = SutraDialogueRepository.getTutorialScript(1)
        assertEquals(1, script.worldId)
        assertEquals("Tutorial 1: Ekadhikena Purvena", script.title)
        assertTrue(script.panels.isNotEmpty())

        // Verify worked example math values in script text
        val textConcat = script.panels.joinToString(" ") { it.text }
        assertTrue("Script must teach 65²", textConcat.contains("65²"))
        assertTrue("Script must contain 4225", textConcat.contains("4225"))
    }

    @Test
    fun testWorld2ScriptsIntegrity() {
        val story = SutraDialogueRepository.getStoryBeatScript(2)
        assertEquals(2, story.worldId)

        val tutorial = SutraDialogueRepository.getTutorialScript(2)
        assertEquals(2, tutorial.worldId)

        val textConcat = tutorial.panels.joinToString(" ") { it.text }
        assertTrue("World 2 tutorial must teach 10000 - 3468", textConcat.contains("10000") && textConcat.contains("3468"))
        assertTrue("World 2 tutorial must contain 6532", textConcat.contains("6532"))
    }

    @Test
    fun testWorld3ScriptsIntegrity() {
        val story = SutraDialogueRepository.getStoryBeatScript(3)
        assertEquals(3, story.worldId)

        val tutorial = SutraDialogueRepository.getTutorialScript(3)
        assertEquals(3, tutorial.worldId)

        val textConcat = tutorial.panels.joinToString(" ") { it.text }
        assertTrue("World 3 tutorial must teach 743 × 999", textConcat.contains("743") && textConcat.contains("999"))
        assertTrue("World 3 tutorial must contain 742257", textConcat.contains("742257"))
    }
}
