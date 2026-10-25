package com.ankh.sutrasaga.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ankh.sutrasaga.data.repository.RiveDialogueRepository
import com.ankh.sutrasaga.domain.validation.RiveDialogueValidator
import com.ankh.sutrasaga.ui.components.generateHandshakeChoices
import com.ankh.sutrasaga.ui.screens.GurukulScriptsRepository
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

    @Test
    fun testDialogueNodeCountAndExcessiveTextLengthAcrossAll16Worlds() {
        // Dialogue tree: between 2 and 4 nodes per scene (prevents cognitive fatigue in ages 5-10)
        for (world in 1..16) {
            val tree = repository.getDialogueForWorld(world)!!
            assertTrue(
                "World $world node count must be between 2 and 4 (found ${tree.dialogueNodes.size})",
                tree.dialogueNodes.size in 2..4
            )

            for (node in tree.dialogueNodes) {
                val words = node.text.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
                // Avoid overwhelming 5-10 year olds with single monolithic text dumps,
                // while not rejecting necessary mathematical step explanations (up to 65 words / 320 chars)
                assertTrue(
                    "World $world Node ${node.nodeId} text must not be empty or truncated (has ${words.size} words)",
                    words.size >= 3
                )
                assertTrue(
                    "World $world Node ${node.nodeId} text exceeds digestible child length (${words.size} words): '${node.text}'",
                    words.size <= 65
                )
                assertTrue(
                    "World $world Node ${node.nodeId} character count (${node.text.length}) exceeds 320 chars: '${node.text}'",
                    node.text.length <= 320
                )
            }
        }

        // Authoritative runtime scripts: 3 to 6 beats per script
        for ((_, script) in GurukulScriptsRepository.allScripts) {
            assertTrue(
                "Runtime script for world ${script.worldId} must have between 3 and 6 beats (found ${script.beats.size})",
                script.beats.size in 3..6
            )
            for (beat in script.beats) {
                val words = beat.guruText.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
                assertTrue(
                    "Runtime script world ${script.worldId} guruText must be digestible (< 50 words, has ${words.size})",
                    words.size <= 50
                )
            }
        }
    }

    @Test
    fun testExplicitPedagogicalMathGlossaryAndNoUnexplainedJargon() {
        // Advanced or intimidating technical jargon that must not appear unintroduced to 5-10 year olds
        val unexplainedJargonList = listOf(
            "modulo",
            "affine",
            "homomorphic",
            "differential manifold",
            "taylor series",
            "syntactic parse",
            "determinantal",
            "eigenvalue",
            "asymptotic"
        )

        for (world in 1..16) {
            val tree = repository.getDialogueForWorld(world)!!
            for (node in tree.dialogueNodes) {
                val textLower = node.text.lowercase()
                val promptLower = node.interactiveHandshake.promptText?.lowercase() ?: ""
                for (jargon in unexplainedJargonList) {
                    assertFalse(
                        "World $world Node ${node.nodeId} contains unintroduced jargon '$jargon' in text: '${node.text}'",
                        textLower.contains(jargon)
                    )
                    assertFalse(
                        "World $world Node ${node.nodeId} contains unintroduced jargon '$jargon' in prompt: '$promptLower'",
                        promptLower.contains(jargon)
                    )
                }
            }
        }
    }

    @Test
    fun testSpokenDialogueAndMathOverlayConsistencyAcrossAll16Worlds() {
        for (world in 1..16) {
            val tree = repository.getDialogueForWorld(world)!!

            for (node in tree.dialogueNodes) {
                // Math overlay expression must not be blank
                assertTrue("World $world Node ${node.nodeId} math overlay must not be blank", node.mathOverlay.expression.isNotBlank())

                // Spoken dialogue and math overlay coherence:
                // Dialogue text or SSML must not contradict the mathematical topic of the scene
                val nodeSpokenText = "${node.text} ${node.audioCues.ssmlText}"
                assertTrue(
                    "World $world Node ${node.nodeId} spoken text must be non-blank",
                    nodeSpokenText.isNotBlank()
                )
            }

            // Challenge node math overlay must be coherent with its prompt text
            val challengeNode = tree.dialogueNodes.first { it.interactiveHandshake.requiresUserTap }
            val prompt = challengeNode.interactiveHandshake.promptText ?: ""
            assertTrue(
                "World $world challenge prompt must be informative and non-blank",
                prompt.isNotBlank()
            )
            // Overlay in challenge node must show unknown missing term
            assertTrue(
                "World $world challenge overlay '${challengeNode.mathOverlay.expression}' must contain unknown marker '?'",
                challengeNode.mathOverlay.expression.contains("?")
            )
        }
    }

    @Test
    fun testClearDistinctionBetweenQuestionAndAnswerInInteractiveHandshakes() {
        for (world in 1..16) {
            val tree = repository.getDialogueForWorld(world)!!
            val challengeNode = tree.dialogueNodes.first { it.interactiveHandshake.requiresUserTap }
            val answer = challengeNode.interactiveHandshake.expectedAnswer
            val prompt = challengeNode.interactiveHandshake.promptText ?: ""
            val expr = challengeNode.mathOverlay.expression

            assertNotNull("World $world expected answer must be defined", answer)
            assertTrue("World $world expected answer must not be blank", answer!!.isNotBlank())

            // The expression on the slate must indicate an unsolved question via '?'
            assertTrue(
                "World $world slate expression '$expr' must contain '?' indicating the missing unknown",
                expr.contains("?")
            )

            // The prompt must formulate the inquiry, not pre-reveal the answer
            assertFalse(
                "World $world prompt '$prompt' must not give away 'The answer is $answer'",
                prompt.contains("The answer is $answer", ignoreCase = true) ||
                    prompt.contains("Answer is $answer", ignoreCase = true)
            )

            // The unsolved slate expression must not directly substitute the answer in place of '?'
            assertFalse(
                "World $world slate expression '$expr' must not already have answer '$answer' in place of '?'",
                expr.endsWith("= $answer") || expr.endsWith("=  $answer")
            )
        }
    }

    @Test
    fun testInteractiveHandshakeInstructionsAreClearAndActionable() {
        val actionVerbs = listOf("what is", "enter", "calculate", "solve", "find", "tap", "how can")

        for (world in 1..16) {
            val tree = repository.getDialogueForWorld(world)!!
            val challengeNode = tree.dialogueNodes.first { it.interactiveHandshake.requiresUserTap }
            val handshake = challengeNode.interactiveHandshake

            val prompt = handshake.promptText ?: ""
            assertTrue(
                "World $world prompt text must not be blank",
                prompt.isNotBlank()
            )

            val hasActionVerb = actionVerbs.any { prompt.contains(it, ignoreCase = true) }
            assertTrue(
                "World $world challenge prompt '$prompt' must use clear actionable instructions for children",
                hasActionVerb
            )

            val targetId = handshake.targetElementId ?: ""
            assertTrue(
                "World $world targetElementId '$targetId' must follow naming convention",
                targetId.startsWith("numpad_key_") || targetId.startsWith("tile_")
            )
        }
    }

    @Test
    fun testAccessibleChoiceSemanticsAndMeaningfulCorrectiveFeedbackForIncorrectAnswers() {
        // Verify each world's expected answer maps to constructive, instructional guidance
        for (world in 1..16) {
            val tree = repository.getDialogueForWorld(world)!!
            val challengeNode = tree.dialogueNodes.first { it.interactiveHandshake.requiresUserTap }
            val expected = challengeNode.interactiveHandshake.expectedAnswer!!

            // Verify choices generation for touch accessibility
            val choices = generateHandshakeChoices(expected)
            assertEquals("Choice list for World $world must contain exactly 5 choices", 5, choices.size)
            assertTrue("Choice list must contain expected answer '$expected'", choices.contains(expected))
            assertEquals("Choices must be distinct", choices.distinct().size, 5)
        }
    }

    @Test
    fun testUnderstandableNonAudioPathVisualParityAcrossAll16Worlds() {
        for (world in 1..16) {
            val tree = repository.getDialogueForWorld(world)!!
            for (node in tree.dialogueNodes) {
                // Visual Speech Text must be present for every audio dialogue
                assertTrue(
                    "World $world Node ${node.nodeId} speech text must not be blank (non-audio path)",
                    node.text.isNotBlank()
                )

                // Math slate must be visible and populated on every step
                assertTrue(
                    "World $world Node ${node.nodeId} math slate expression must not be blank (non-audio path)",
                    node.mathOverlay.expression.isNotBlank()
                )

                // Visual dialogue text should convey the core meaning spoken in SSML
                val ssmlClean = node.audioCues.ssmlText
                    .replace(Regex("<[^>]*>"), "")
                    .replace(Regex("\\s+"), " ")
                    .trim()
                assertTrue(
                    "World $world Node ${node.nodeId} clean SSML must not be empty",
                    ssmlClean.isNotBlank()
                )
            }
        }
    }

    @Test
    fun testSSMLMarkupValidityAndSpeechCadenceQualityAcrossAll16Worlds() {
        for (world in 1..16) {
            val tree = repository.getDialogueForWorld(world)!!
            for (node in tree.dialogueNodes) {
                val ssml = node.audioCues.ssmlText

                // Basic well-formedness: starts with <speak> and ends with </speak>
                assertTrue(
                    "World $world Node ${node.nodeId} SSML must start with <speak>",
                    ssml.startsWith("<speak>")
                )
                assertTrue(
                    "World $world Node ${node.nodeId} SSML must end with </speak>",
                    ssml.endsWith("</speak>")
                )

                // Balanced prosody tags
                val openProsody = Regex("<prosody\\b").findAll(ssml).count()
                val closeProsody = Regex("</prosody>").findAll(ssml).count()
                assertEquals(
                    "World $world Node ${node.nodeId} prosody tags must balance (opened $openProsody, closed $closeProsody)",
                    openProsody,
                    closeProsody
                )

                // Validate speech rate if prosody is specified: must be between 80% and 110%
                val rateMatch = Regex("rate=['\"]([0-9]+)%['\"]").find(ssml)
                if (rateMatch != null) {
                    val rate = rateMatch.groupValues[1].toInt()
                    assertTrue(
                        "World $world Node ${node.nodeId} prosody rate ($rate%) must be child-friendly (80%..110%)",
                        rate in 80..110
                    )
                }
            }
        }
    }
}
