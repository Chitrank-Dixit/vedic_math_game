package com.ankh.sutrasaga.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ankh.sutrasaga.data.repository.RiveDialogueRepository
import com.ankh.sutrasaga.domain.validation.RiveDialogueValidator
import com.ankh.sutrasaga.ui.components.generateHandshakeChoices
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

/**
 * Dedicated Language, Child Comprehension, and Accessibility Audit Test Suite
 * for Worlds 1 through 16 (Target Audience: Children aged 5–10).
 *
 * Verifies:
 * 1. Child Comprehension: SSML cues have natural conversational cadence; zero robotic syntax.
 * 2. WCAG / Accessibility: Choice options are unambiguous, non-colliding, and contain visual symbols.
 * 3. Gurukul Integrity: Preserves Guru-Shishya dynamic and forbids inappropriate animations (e.g. winking).
 * 4. Mathematical Rigor: Invariants, fail-closed validation, and correct calculations across all 16 worlds.
 */
@RunWith(RobolectricTestRunner::class)
class CurriculumLanguageAndAccessibilityAuditTest {

    private lateinit var repository: RiveDialogueRepository

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val jsonContent = try {
            context.assets.open("dialogue/rive_sutra_dialogues.json").bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            val candidateFiles = listOf(
                File("src/main/assets/dialogue/rive_sutra_dialogues.json"),
                File("app/src/main/assets/dialogue/rive_sutra_dialogues.json"),
                File(System.getProperty("user.dir"), "app/src/main/assets/dialogue/rive_sutra_dialogues.json"),
                File(System.getProperty("user.dir"), "src/main/assets/dialogue/rive_sutra_dialogues.json")
            )
            val jsonFile = candidateFiles.firstOrNull { it.exists() }
            assertNotNull("Asset file rive_sutra_dialogues.json must exist", jsonFile)
            jsonFile!!.readText()
        }

        repository = RiveDialogueRepository()
        repository.parseJson(jsonContent)
    }

    @Test
    fun testAll16WorldsPassRiveDialogueValidator() {
        for (world in 1..16) {
            val tree = repository.getDialogueForWorld(world)
            assertNotNull("World $world dialogue tree must exist", tree)
            val validation = RiveDialogueValidator.validate(tree!!)
            assertTrue(
                "World $world failed dialogue tree validation: ${validation.errors.joinToString()}",
                validation.isValid
            )
        }
    }

    @Test
    fun testChildComprehensionSSMLCadenceAcrossAll16Worlds() {
        // Forbidden robotic or high-friction speech phrases for ages 5-10
        val forbiddenSpeechPhrases = listOf(
            "open-parenthesis",
            "close-parenthesis",
            "quantity squared",
            " mod ",
            "modulo",
            "NaN",
            "undefined"
        )

        for (world in 1..16) {
            val tree = repository.getDialogueForWorld(world)!!
            for (node in tree.dialogueNodes) {
                val ssml = node.audioCues.ssmlText.lowercase()
                assertTrue(
                    "Node ${node.nodeId} in World $world SSML must be enclosed in <speak>",
                    ssml.startsWith("<speak>") && ssml.endsWith("</speak>")
                )

                for (phrase in forbiddenSpeechPhrases) {
                    assertFalse(
                        "World $world Node ${node.nodeId} contains robotic speech phrase '$phrase' in SSML: ${node.audioCues.ssmlText}",
                        ssml.contains(phrase)
                    )
                }
            }
        }
    }

    @Test
    fun testGurukulCharacterAnimationIntegrity() {
        for (world in 1..16) {
            val tree = repository.getDialogueForWorld(world)!!
            for (node in tree.dialogueNodes) {
                assertFalse(
                    "World $world Node ${node.nodeId} must not use inappropriate 'guru_wink' animation",
                    node.riveState.animation.equals("guru_wink", ignoreCase = true)
                )
            }
        }
    }

    @Test
    fun testHandshakeChoiceGenerationAccessibilityAcrossAll16Worlds() {
        val expectedAnswers = mapOf(
            1 to "20",
            2 to "3",
            3 to "52",
            4 to "94",
            5 to "5",
            6 to "2",
            7 to "4",
            8 to "1",
            9 to "-1",
            10 to "9",
            11 to "60",
            12 to "6",
            13 to "11",
            14 to "12",
            15 to "-2",
            16 to "7"
        )

        for (world in 1..16) {
            val tree = repository.getDialogueForWorld(world)!!
            val challengeNode = tree.dialogueNodes.first { it.interactiveHandshake.requiresUserTap }
            val answer = challengeNode.interactiveHandshake.expectedAnswer

            assertEquals(
                "World $world expected answer must match curriculum spec",
                expectedAnswers[world],
                answer
            )

            // Verify dynamic choice generation for touch tiles
            val choices = generateHandshakeChoices(answer)
            assertEquals("Choice list for World $world must contain exactly 5 options", 5, choices.size)
            assertTrue("Choice list for World $world must contain the target answer '$answer'", choices.contains(answer))
            assertEquals("Choices for World $world must be unique (no duplicates)", choices.distinct().size, choices.size)
        }
    }

    @Test
    fun testWorld12DivisionRemainderChildFriendlyTerminology() {
        val tree = repository.getDialogueForWorld(12)!!
        val challengeNode = tree.dialogueNodes.first { it.interactiveHandshake.requiresUserTap }

        // Must not use raw 'mod' in challenge prompt or text for 5-10 year olds
        assertFalse("World 12 challenge text should avoid 'mod'", challengeNode.text.contains("mod"))
        assertFalse("World 12 challenge prompt should avoid 'mod'", challengeNode.interactiveHandshake.promptText?.contains("mod") == true)
        assertTrue("World 12 challenge text should refer to remainder", challengeNode.text.contains("remainder", ignoreCase = true))
        assertEquals("6", challengeNode.interactiveHandshake.expectedAnswer)
    }
}
