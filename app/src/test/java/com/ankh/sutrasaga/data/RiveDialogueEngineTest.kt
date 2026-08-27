package com.ankh.sutrasaga.data

import com.ankh.sutrasaga.data.repository.RiveDialogueRepository
import com.ankh.sutrasaga.domain.models.RiveEmotion
import com.ankh.sutrasaga.domain.models.RiveSpeaker
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class RiveDialogueEngineTest {

    private val allowedGuruAnimations = setOf(
        "guru_idle",
        "guru_explain",
        "guru_bless",
        "guru_magic_fx",
        "guru_nod"
    )

    private val allowedShishyaAnimations = setOf(
        "shishya_idle",
        "shishya_curious",
        "shishya_puzzled",
        "shishya_aha",
        "shishya_celebrate"
    )

    @Test
    fun testAll16VedicSutraDialoguesArePresentAndValid() {
        val jsonFile = File("src/main/assets/dialogue/rive_sutra_dialogues.json")
        assertTrue("Asset file must exist", jsonFile.exists())

        val repository = RiveDialogueRepository()
        val trees = repository.parseJson(jsonFile.readText())

        assertEquals("Must contain all 16 Primary Vedic Sutras", 16, trees.size)

        for (i in 1..16) {
            val tree = repository.getDialogueForWorld(i)
            assertNotNull("World $i must exist", tree)
            assertEquals("World number must match $i", i, tree!!.worldNumber)
            assertTrue("Sutra name must not be blank", tree.sutraName.isNotBlank())
            assertTrue("English meaning must not be blank", tree.englishMeaning.isNotBlank())

            // Rule 1: Short & Punchy (exactly 4 nodes)
            assertEquals("World $i must have exactly 4 dialogue nodes", 4, tree.dialogueNodes.size)

            for (node in tree.dialogueNodes) {
                assertTrue("Node text must not be blank", node.text.isNotBlank())
                assertTrue("Math overlay expression must not be blank", node.mathOverlay.expression.isNotBlank())
                assertTrue("SSML text must contain <speak> tag", node.audioCues.ssmlText.contains("<speak>"))
                assertTrue("SFX name must not be blank", node.audioCues.sfx.isNotBlank())

                // Check allowed animation names
                if (node.riveState.character == RiveSpeaker.GURU) {
                    assertTrue(
                        "Guru animation '${node.riveState.animation}' must be allowed in world $i node ${node.nodeId}",
                        allowedGuruAnimations.contains(node.riveState.animation)
                    )
                } else {
                    assertTrue(
                        "Shishya animation '${node.riveState.animation}' must be allowed in world $i node ${node.nodeId}",
                        allowedShishyaAnimations.contains(node.riveState.animation)
                    )
                }
            }

            // Rule 4: Active Handshake on Step 4
            val finalNode = tree.dialogueNodes.last()
            assertTrue("Final node must require user tap", finalNode.interactiveHandshake.requiresUserTap)
            assertNotNull("Final node must specify target element ID", finalNode.interactiveHandshake.targetElementId)
            assertNotNull("Final node must specify prompt text", finalNode.interactiveHandshake.promptText)
        }
    }
}
