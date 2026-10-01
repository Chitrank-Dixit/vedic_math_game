package com.ankh.sutrasaga.data

import com.ankh.sutrasaga.data.repository.RiveDialogueRepository
import com.ankh.sutrasaga.domain.models.AudioCues
import com.ankh.sutrasaga.domain.models.InteractiveHandshake
import com.ankh.sutrasaga.domain.models.MathOverlay
import com.ankh.sutrasaga.domain.models.RiveAnimationConfig
import com.ankh.sutrasaga.domain.models.RiveCharacterState
import com.ankh.sutrasaga.domain.models.RiveDialogueNode
import com.ankh.sutrasaga.domain.models.RiveEmotion
import com.ankh.sutrasaga.domain.models.RiveSpeaker
import com.ankh.sutrasaga.domain.models.RiveSutraDialogueTree
import com.ankh.sutrasaga.domain.validation.RiveDialogueValidator
import com.ankh.sutrasaga.domain.validation.VedicMathValidator
import com.ankh.sutrasaga.engine.rive.DefaultRiveAdapter
import com.ankh.sutrasaga.engine.rive.RiveDialogueController
import com.ankh.sutrasaga.ui.components.generateHandshakeChoices
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class RiveDialogueEngineTest {

    @Test
    fun testAll16VedicSutraDialoguesArePresentAndValid() {
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
            assertNotNull("Asset file rive_sutra_dialogues.json must exist in assets", jsonFile)
            jsonFile!!.readText()
        }

        val repository = RiveDialogueRepository()
        val trees = repository.parseJson(jsonContent)

        assertEquals("Must contain all 16 Primary Vedic Sutras", 16, trees.size)

        for (i in 1..16) {
            val tree = repository.getDialogueForWorld(i)
            assertNotNull("World $i must exist", tree)
            assertEquals("World number must match $i", i, tree!!.worldNumber)
            assertTrue("Sutra name must not be blank", tree.sutraName.isNotBlank())
            assertTrue("English meaning must not be blank", tree.englishMeaning.isNotBlank())

            // Rule: 2 to 4 nodes (canonical is exactly 4 nodes)
            assertTrue("World $i node count must be 2..4", tree.dialogueNodes.size in 2..4)

            val validation = RiveDialogueValidator.validate(tree)
            assertTrue(
                "World $i must pass RiveDialogueValidator with no errors: ${validation.errors}",
                validation.isValid
            )

            for (node in tree.dialogueNodes) {
                assertTrue("Node text must not be blank", node.text.isNotBlank())
                assertTrue("Math overlay expression must not be blank", node.mathOverlay.expression.isNotBlank())
                assertTrue("SSML text must contain <speak> tag", node.audioCues.ssmlText.contains("<speak>"))
                assertTrue("SFX name must not be blank", node.audioCues.sfx.isNotBlank())

                // Check allowed animation names via central config
                assertTrue(
                    "Animation '${node.riveState.animation}' must be allowed for ${node.riveState.character}",
                    RiveAnimationConfig.isValidAnimation(node.riveState.character, node.riveState.animation)
                )

                // STRICT: Guru must never wink in any dialogue tree
                assertNotEquals(
                    "Guru animations must never contain wink",
                    "guru_wink",
                    node.riveState.animation
                )
            }

            // Rule 4: Active Handshake on Step 4
            val finalNode = tree.dialogueNodes.last()
            assertTrue("Final node must require user tap", finalNode.interactiveHandshake.requiresUserTap)
            assertNotNull("Final node must specify target element ID", finalNode.interactiveHandshake.targetElementId)
            assertNotNull("Final node must specify prompt text", finalNode.interactiveHandshake.promptText)
            assertNotNull("Final node must specify authoritative expected answer", finalNode.interactiveHandshake.expectedAnswer)
            assertTrue("Authoritative expected answer must not be blank", finalNode.interactiveHandshake.expectedAnswer!!.isNotBlank())

            val choices = generateHandshakeChoices(finalNode.interactiveHandshake.expectedAnswer)
            assertTrue(
                "Generated choices $choices must contain expected answer '${finalNode.interactiveHandshake.expectedAnswer}' for world ${tree.worldNumber}",
                choices.contains(finalNode.interactiveHandshake.expectedAnswer)
            )

            val validationResult = RiveDialogueValidator.validate(tree)
            assertTrue("Every production dialogue tree must be valid: ${validationResult.errors}", validationResult.isValid)
            assertTrue("Production dialogue tree must have no validation errors", validationResult.errors.isEmpty())
        }
    }

    @Test
    fun testStructuralValidationRules() {
        val validTree = RiveSutraDialogueTree(
            sutraId = "test_sutra",
            sutraName = "Test Sutra",
            englishMeaning = "Test Meaning",
            worldNumber = 1,
            dialogueNodes = listOf(
                RiveDialogueNode(
                    nodeId = 1,
                    speaker = RiveSpeaker.SHISHYA,
                    text = "How do we solve 95²?",
                    riveState = RiveCharacterState(RiveSpeaker.SHISHYA, "shishya_curious", RiveEmotion.THINKING, true),
                    mathOverlay = MathOverlay("95² = ?", listOf("95")),
                    audioCues = AudioCues("<speak>How do we solve 95 squared?</speak>", "sfx_question"),
                    interactiveHandshake = InteractiveHandshake(false, null, null)
                ),
                RiveDialogueNode(
                    nodeId = 2,
                    speaker = RiveSpeaker.GURU,
                    text = "Multiply 9 × 10 = 90!",
                    riveState = RiveCharacterState(RiveSpeaker.GURU, "guru_explain", RiveEmotion.HAPPY, true),
                    mathOverlay = MathOverlay("9 × 10 = 90", listOf("90")),
                    audioCues = AudioCues("<speak>Multiply 9 by 10.</speak>", "sfx_sutra"),
                    interactiveHandshake = InteractiveHandshake(true, "numpad_key_9", "Tap 9 to continue", "9")
                )
            )
        )

        val result = RiveDialogueValidator.validate(validTree)
        assertTrue("Valid tree should pass validation", result.isValid)
        assertTrue("Errors should be empty", result.errors.isEmpty())
    }

    @Test
    fun testHandshakeMissingExpectedAnswerFailsValidation() {
        val invalidHandshakeTree = RiveSutraDialogueTree(
            sutraId = "test_sutra_invalid_handshake",
            sutraName = "Test Sutra",
            englishMeaning = "Test Meaning",
            worldNumber = 1,
            dialogueNodes = listOf(
                RiveDialogueNode(
                    nodeId = 1,
                    speaker = RiveSpeaker.SHISHYA,
                    text = "Intro node",
                    riveState = RiveCharacterState(RiveSpeaker.SHISHYA, "shishya_curious", RiveEmotion.THINKING, true),
                    mathOverlay = MathOverlay("1+1=2", listOf("1")),
                    audioCues = AudioCues("<speak>intro</speak>", "sfx"),
                    interactiveHandshake = InteractiveHandshake(false, null, null)
                ),
                RiveDialogueNode(
                    nodeId = 2,
                    speaker = RiveSpeaker.GURU,
                    text = "Handshake with null expected answer",
                    riveState = RiveCharacterState(RiveSpeaker.GURU, "guru_explain", RiveEmotion.HAPPY, true),
                    mathOverlay = MathOverlay("1+1=2", listOf("2")),
                    audioCues = AudioCues("<speak>handshake</speak>", "sfx"),
                    interactiveHandshake = InteractiveHandshake(
                        requiresUserTap = true,
                        targetElementId = "numpad_key_9",
                        promptText = "Tap 9 to continue",
                        expectedAnswer = null
                    )
                )
            )
        )

        val result = RiveDialogueValidator.validate(invalidHandshakeTree)
        assertFalse("Tree with null expected answer on active handshake must fail validation", result.isValid)
        assertTrue(result.errors.any { it.contains("expected_answer is null/blank") })
    }

    @Test
    fun testNodeCountConstraintViolation() {
        // Tree with only 1 node (< MIN_NODES = 2)
        val singleNodeTree = RiveSutraDialogueTree(
            sutraId = "test_sutra",
            sutraName = "Test Sutra",
            englishMeaning = "Test Meaning",
            worldNumber = 1,
            dialogueNodes = listOf(
                RiveDialogueNode(
                    nodeId = 1,
                    speaker = RiveSpeaker.SHISHYA,
                    text = "Single node text",
                    riveState = RiveCharacterState(RiveSpeaker.SHISHYA, "shishya_curious", RiveEmotion.THINKING, true),
                    mathOverlay = MathOverlay("1+1=2", listOf("1")),
                    audioCues = AudioCues("<speak>test</speak>", "sfx"),
                    interactiveHandshake = InteractiveHandshake(false, null, null)
                )
            )
        )

        val result = RiveDialogueValidator.validate(singleNodeTree)
        assertFalse("Single node tree should fail node count check", result.isValid)
        assertTrue(result.errors.any { it.contains("node count must be between") })
    }

    @Test
    fun testSpeakerMismatchDetection() {
        val mismatchTree = RiveSutraDialogueTree(
            sutraId = "test_sutra",
            sutraName = "Test Sutra",
            englishMeaning = "Test Meaning",
            worldNumber = 1,
            dialogueNodes = listOf(
                RiveDialogueNode(
                    nodeId = 1,
                    speaker = RiveSpeaker.GURU, // GURU speaker
                    text = "Mismatch node",
                    riveState = RiveCharacterState(RiveSpeaker.SHISHYA, "shishya_curious", RiveEmotion.THINKING, true), // SHISHYA character
                    mathOverlay = MathOverlay("1+1=2", listOf("1")),
                    audioCues = AudioCues("<speak>test</speak>", "sfx"),
                    interactiveHandshake = InteractiveHandshake(false, null, null)
                ),
                RiveDialogueNode(
                    nodeId = 2,
                    speaker = RiveSpeaker.GURU,
                    text = "Node 2",
                    riveState = RiveCharacterState(RiveSpeaker.GURU, "guru_explain", RiveEmotion.HAPPY, true),
                    mathOverlay = MathOverlay("1+1=2", listOf("1")),
                    audioCues = AudioCues("<speak>test</speak>", "sfx"),
                    interactiveHandshake = InteractiveHandshake(false, null, null)
                )
            )
        )

        val result = RiveDialogueValidator.validate(mismatchTree)
        assertFalse("Mismatch between speaker and character should fail", result.isValid)
        assertTrue(result.errors.any { it.contains("does not match") })
    }

    @Test
    fun testInvalidAnimationFallbackToIdle() {
        val invalidAnim = "guru_flying_kick"
        assertFalse(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, invalidAnim))

        val sanitized = RiveAnimationConfig.sanitizeAnimation(RiveSpeaker.GURU, invalidAnim)
        assertEquals("Invalid guru animation must fallback to guru_idle", "guru_idle", sanitized)

        val invalidShishyaAnim = "shishya_teleport"
        val sanitizedShishya = RiveAnimationConfig.sanitizeAnimation(RiveSpeaker.SHISHYA, invalidShishyaAnim)
        assertEquals("Invalid shishya animation must fallback to shishya_idle", "shishya_idle", sanitizedShishya)
    }

    @Test
    fun testGuruApprovalStateNeverTriggersWink() {
        // Assert that guru_wink is never considered valid
        assertFalse("guru_wink is strictly disallowed", RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, "guru_wink"))
        assertEquals("guru_wink must fallback to guru_idle", "guru_idle", RiveAnimationConfig.sanitizeAnimation(RiveSpeaker.GURU, "guru_wink"))

        // Assert valid approval states
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, "guru_nod"))
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, "guru_bless"))
    }

    @Test
    fun testMathematicalCalculationsAndHandshakeEvaluation() {
        // Mathematical validation of examples from curriculum
        assertTrue("95² must be 9025", VedicMathValidator.verifySquaringEndingIn5(95, 9025))
        assertTrue("45² must be 2025", VedicMathValidator.verifySquaringEndingIn5(45, 2025))
        assertTrue("65² must be 4225", VedicMathValidator.verifySquaringEndingIn5(65, 4225))

        assertTrue("96 × 92 must be 8832", VedicMathValidator.verifyNikhilamMultiplication(96, 92, 100))
        assertTrue("98 × 97 must be 9506", VedicMathValidator.verifyNikhilamMultiplication(98, 97, 100))
        assertTrue("99 × 96 must be 9504", VedicMathValidator.verifyNikhilamMultiplication(99, 96, 100))

        assertTrue("100 - 7 = 93", VedicMathValidator.verifyBaseSubtraction(100, 7, 93))
        assertTrue("1000 - 8 = 992", VedicMathValidator.verifyBaseSubtraction(1000, 8, 992))

        // Interactive Handshake Evaluation
        val handshakeWithExplicitAnswer = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_5",
            promptText = "Tap 5 to compute 4 × 5 = 20!",
            expectedAnswer = "5"
        )
        assertTrue(VedicMathValidator.evaluateHandshake(handshakeWithExplicitAnswer, "5"))
        assertTrue(VedicMathValidator.evaluateHandshake(handshakeWithExplicitAnswer, " 5 "))
        assertFalse(VedicMathValidator.evaluateHandshake(handshakeWithExplicitAnswer, "4"))
    }

    @Test
    fun testDialogueControllerPlaybackAndRetryState() {
        val tree = RiveSutraDialogueTree(
            sutraId = "test_controller",
            sutraName = "Test Controller Sutra",
            englishMeaning = "Test Controller Meaning",
            worldNumber = 1,
            dialogueNodes = listOf(
                RiveDialogueNode(
                    nodeId = 1,
                    speaker = RiveSpeaker.SHISHYA,
                    text = "Step 1 question",
                    riveState = RiveCharacterState(RiveSpeaker.SHISHYA, "shishya_curious", RiveEmotion.THINKING, true),
                    mathOverlay = MathOverlay("95² = ?", listOf("95")),
                    audioCues = AudioCues("<speak>step 1</speak>", "sfx"),
                    interactiveHandshake = InteractiveHandshake(false, null, null)
                ),
                RiveDialogueNode(
                    nodeId = 2,
                    speaker = RiveSpeaker.GURU,
                    text = "Step 2 reveal",
                    riveState = RiveCharacterState(RiveSpeaker.GURU, "guru_explain", RiveEmotion.HAPPY, true),
                    mathOverlay = MathOverlay("9 × 10 = 90", listOf("90")),
                    audioCues = AudioCues("<speak>step 2</speak>", "sfx"),
                    interactiveHandshake = InteractiveHandshake(false, null, null)
                ),
                RiveDialogueNode(
                    nodeId = 3,
                    speaker = RiveSpeaker.SHISHYA,
                    text = "Step 3 eureka",
                    riveState = RiveCharacterState(RiveSpeaker.SHISHYA, "shishya_aha", RiveEmotion.SURPRISED, true),
                    mathOverlay = MathOverlay("9025", listOf("9025")),
                    audioCues = AudioCues("<speak>step 3</speak>", "sfx"),
                    interactiveHandshake = InteractiveHandshake(false, null, null)
                ),
                RiveDialogueNode(
                    nodeId = 4,
                    speaker = RiveSpeaker.GURU,
                    text = "Step 4 handshake",
                    riveState = RiveCharacterState(RiveSpeaker.GURU, "guru_magic_fx", RiveEmotion.HAPPY, true),
                    mathOverlay = MathOverlay("45² = 2025", listOf("2025")),
                    audioCues = AudioCues("<speak>step 4</speak>", "sfx"),
                    interactiveHandshake = InteractiveHandshake(true, "numpad_key_5", "Tap 5 to begin!", "5")
                )
            )
        )

        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(tree, adapter)

        assertEquals(0, controller.currentNodeIndex.value)
        assertEquals("shishya_curious", adapter.shishyaAnimation.value)

        // Advance to node 2
        assertTrue(controller.advanceNode())
        assertEquals(1, controller.currentNodeIndex.value)
        assertEquals("guru_explain", adapter.guruAnimation.value)

        // Advance to node 3
        assertTrue(controller.advanceNode())
        assertEquals(2, controller.currentNodeIndex.value)
        assertEquals("shishya_aha", adapter.shishyaAnimation.value)

        // Advance to node 4
        assertTrue(controller.advanceNode())
        assertEquals(3, controller.currentNodeIndex.value)
        assertEquals("guru_magic_fx", adapter.guruAnimation.value)

        // On node 4 (interactive handshake), advanceNode should NOT bypass handshake
        assertFalse(controller.advanceNode())
        assertFalse(controller.isCompleted.value)

        // Incorrect handshake attempt
        val wrongResult = controller.submitHandshake("9")
        assertFalse("Wrong handshake answer should return false", wrongResult)
        assertFalse("Quiz must not start on incorrect answer", controller.isCompleted.value)
        assertNotNull("Handshake error message must be set", controller.handshakeError.value)
        assertEquals("shishya_puzzled", adapter.shishyaAnimation.value)

        // Correct handshake attempt retry
        val correctResult = controller.submitHandshake("5")
        assertTrue("Correct handshake answer should return true", correctResult)
        assertTrue("Controller must complete upon success", controller.isCompleted.value)
        assertNull("Handshake error must be cleared", controller.handshakeError.value)
        assertEquals("shishya_celebrate", adapter.shishyaAnimation.value)
        assertTrue("Celebration particle must be triggered", adapter.particleTriggered.value)
    }

    @Test
    fun testHandshakeValidationAuthoritativeAnswerRules() {
        // Case A: expectedAnswer = "5", targetElementId = "numpad_key_5", userAnswer = "5" -> true
        val caseA = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_5",
            promptText = "Tap 5 to compute 4 × 5 = 20!",
            expectedAnswer = "5"
        )
        assertTrue(
            "Case A: expectedAnswer='5', targetElementId='numpad_key_5', userAnswer='5' -> true",
            VedicMathValidator.evaluateHandshake(caseA, "5")
        )

        // Case B: expectedAnswer = "5", targetElementId = "numpad_key_7", userAnswer = "5" -> true
        // (Proves targetElementId is NOT authoritative)
        val caseB = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_7",
            promptText = "Tap 5 to compute 4 × 5 = 20!",
            expectedAnswer = "5"
        )
        assertTrue(
            "Case B: expectedAnswer='5', targetElementId='numpad_key_7', userAnswer='5' -> true",
            VedicMathValidator.evaluateHandshake(caseB, "5")
        )
        assertFalse(
            "Case B: targetElementId='numpad_key_7' must NOT make userAnswer='7' pass when expectedAnswer='5'",
            VedicMathValidator.evaluateHandshake(caseB, "7")
        )

        // Case C: expectedAnswer = "5", promptText = "Tap 7 for something else", userAnswer = "5" -> true
        // (Proves prompt text is NOT authoritative)
        val caseC = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_5",
            promptText = "Tap 7 for something else",
            expectedAnswer = "5"
        )
        assertTrue(
            "Case C: expectedAnswer='5', promptText='Tap 7...', userAnswer='5' -> true",
            VedicMathValidator.evaluateHandshake(caseC, "5")
        )
        assertFalse(
            "Case C: promptText mentioning 7 must NOT make userAnswer='7' pass when expectedAnswer='5'",
            VedicMathValidator.evaluateHandshake(caseC, "7")
        )

        // Case D: expectedAnswer = null, targetElementId = "numpad_key_5", userAnswer = "5" -> false
        // (Proves targetElementId cannot provide a fallback)
        val caseD = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_5",
            promptText = null,
            expectedAnswer = null
        )
        assertFalse(
            "Case D: expectedAnswer=null, targetElementId='numpad_key_5', userAnswer='5' -> false",
            VedicMathValidator.evaluateHandshake(caseD, "5")
        )

        // Case E: expectedAnswer = null, promptText = "Tap 5...", userAnswer = "5" -> false
        // (Proves prompt text cannot provide a fallback)
        val caseE = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = null,
            promptText = "Tap 5 to begin!",
            expectedAnswer = null
        )
        assertFalse(
            "Case E: expectedAnswer=null, promptText='Tap 5...', userAnswer='5' -> false",
            VedicMathValidator.evaluateHandshake(caseE, "5")
        )

        // Case F: expectedAnswer = "5", userAnswer = "7" -> false
        val caseF = InteractiveHandshake(
            requiresUserTap = true,
            expectedAnswer = "5"
        )
        assertFalse(
            "Case F: expectedAnswer='5', userAnswer='7' -> false",
            VedicMathValidator.evaluateHandshake(caseF, "7")
        )

        // Case G: expectedAnswer = "5", userAnswer = "5" -> true
        val caseG = InteractiveHandshake(
            requiresUserTap = true,
            expectedAnswer = "5"
        )
        assertTrue(
            "Case G: expectedAnswer='5', userAnswer='5' -> true",
            VedicMathValidator.evaluateHandshake(caseG, "5")
        )

        // Case H: expectedAnswer = blank -> false
        val caseH = InteractiveHandshake(
            requiresUserTap = true,
            expectedAnswer = "   "
        )
        assertFalse(
            "Case H: expectedAnswer='   ' (blank) -> false",
            VedicMathValidator.evaluateHandshake(caseH, "5")
        )

        // Additional edge case tests: empty and whitespace user answers
        val emptyUserAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            expectedAnswer = "5"
        )
        assertFalse(
            "Empty user answer must fail validation",
            VedicMathValidator.evaluateHandshake(emptyUserAnswerHandshake, "")
        )
        assertFalse(
            "Whitespace-only user answer must fail validation",
            VedicMathValidator.evaluateHandshake(emptyUserAnswerHandshake, "   ")
        )
    }

    @Test
    fun testDialogueControllerWithUnavailableExpectedAnswerFailsClosed() {
        // TEST 5 — Missing expected answer must never complete the dialogue
        val treeWithUnresolvableHandshake = RiveSutraDialogueTree(
            sutraId = "test_unresolvable",
            sutraName = "Test Unresolvable",
            englishMeaning = "Test Meaning",
            worldNumber = 1,
            dialogueNodes = listOf(
                RiveDialogueNode(
                    nodeId = 1,
                    speaker = RiveSpeaker.SHISHYA,
                    text = "Intro node",
                    riveState = RiveCharacterState(RiveSpeaker.SHISHYA, "shishya_curious", RiveEmotion.THINKING, true),
                    mathOverlay = MathOverlay("1+1=2", listOf("1")),
                    audioCues = AudioCues("<speak>intro</speak>", "sfx"),
                    interactiveHandshake = InteractiveHandshake(false, null, null)
                ),
                RiveDialogueNode(
                    nodeId = 2,
                    speaker = RiveSpeaker.GURU,
                    text = "Handshake with missing expected answer",
                    riveState = RiveCharacterState(RiveSpeaker.GURU, "guru_magic_fx", RiveEmotion.HAPPY, true),
                    mathOverlay = MathOverlay("1+1=2", listOf("2")),
                    audioCues = AudioCues("<speak>handshake</speak>", "sfx"),
                    interactiveHandshake = InteractiveHandshake(
                        requiresUserTap = true,
                        targetElementId = null,
                        promptText = null,
                        expectedAnswer = null
                    )
                )
            )
        )

        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(treeWithUnresolvableHandshake, adapter)

        // Advance to handshake node
        assertTrue(controller.advanceNode())
        assertEquals(1, controller.currentNodeIndex.value)

        // Attempt submitHandshake with arbitrary answer "5"
        val submitResult = controller.submitHandshake("5")
        assertFalse("submitHandshake must return false when expected answer is unavailable", submitResult)
        assertFalse("Dialogue must NOT complete when expected answer is unavailable", controller.isCompleted.value)
        assertNotNull("Handshake error message must be set on failure", controller.handshakeError.value)
        assertEquals("shishya_puzzled", adapter.shishyaAnimation.value)
    }

    @Test
    fun testGenerateHandshakeChoicesIncludesZeroAndNegatives() {
        // Zero target (e.g. World 5 Śūnyaṁ Sāmyasamuccaye)
        val zeroChoices = generateHandshakeChoices("0")
        assertTrue("Choices for 0 must contain '0'", zeroChoices.contains("0"))
        assertEquals(listOf("-2", "-1", "0", "1", "2"), zeroChoices)

        // Negative target
        val negChoices = generateHandshakeChoices("-4")
        assertTrue("Choices for -4 must contain '-4'", negChoices.contains("-4"))
        assertEquals(listOf("-6", "-5", "-4", "-3", "-2"), negChoices)

        // Positive target
        val posChoices = generateHandshakeChoices("5")
        assertTrue("Choices for 5 must contain '5'", posChoices.contains("5"))
        assertEquals(listOf("3", "4", "5", "6", "7"), posChoices)

        // Null / blank fallbacks
        val nullChoices = generateHandshakeChoices(null)
        assertEquals(listOf("0", "1", "2", "3", "4"), nullChoices)

        // Non-numeric custom text fallback
        val textChoices = generateHandshakeChoices("X")
        assertEquals(listOf("X"), textChoices)
    }
}

