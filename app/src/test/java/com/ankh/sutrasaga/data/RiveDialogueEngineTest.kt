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
        // TEST 1 — Explicit expected answer = "5", user answer = "5" -> PASS
        val validHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_5",
            promptText = "Tap 5 to compute 4 × 5 = 20!",
            expectedAnswer = "5"
        )
        assertTrue(
            "TEST 1: Explicit expected answer '5' with user answer '5' must pass validation",
            VedicMathValidator.evaluateHandshake(validHandshake, "5")
        )

        // TEST 2 — Explicit expected answer = "5", user answer = "7" -> FAIL
        assertFalse(
            "TEST 2: Explicit expected answer '5' with wrong user answer '7' must fail validation",
            VedicMathValidator.evaluateHandshake(validHandshake, "7")
        )

        // TEST 3 — Expected answer missing (null) -> FAIL
        val missingExpectedHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_5",
            promptText = "Tap 5 to proceed!",
            expectedAnswer = null
        )
        assertFalse(
            "TEST 3: Missing (null) expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(missingExpectedHandshake, "5")
        )

        // TEST 4 — Expected answer blank -> FAIL
        val blankExpectedHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_5",
            promptText = "Tap 5 to proceed!",
            expectedAnswer = "   "
        )
        assertFalse(
            "TEST 4: Blank expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(blankExpectedHandshake, "5")
        )

        // TEST 5 — Expected answer = "5", targetElementId = "numpad_key_5" -> PASS
        assertTrue(
            "TEST 5: Expected answer '5' matching element id must pass validation",
            VedicMathValidator.evaluateHandshake(validHandshake, "5")
        )

        // TEST 6 — Expected answer = "5", targetElementId = "numpad_key_7" -> STILL PASS
        // (Changing targetElementId must NOT change the authoritative expected answer)
        val mismatchedTargetIdHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_7", // target element says 7
            promptText = "Tap key to compute",
            expectedAnswer = "5" // authoritative math answer is 5
        )
        assertTrue(
            "TEST 6: Changing targetElementId must NOT change the expected answer (5 must still pass)",
            VedicMathValidator.evaluateHandshake(mismatchedTargetIdHandshake, "5")
        )
        assertFalse(
            "TEST 6: Changing targetElementId must NOT make 7 pass when expectedAnswer is 5",
            VedicMathValidator.evaluateHandshake(mismatchedTargetIdHandshake, "7")
        )

        // TEST 7 — Expected answer = "5", Prompt changed -> STILL validates against "5"
        // (Changing prompt wording must NOT change the authoritative expected answer)
        val alteredPromptHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_5",
            promptText = "Tap 9 for 3² = 09!", // deceptive prompt text
            expectedAnswer = "5" // authoritative math answer is 5
        )
        assertTrue(
            "TEST 7: Changing prompt wording must NOT change expected answer (5 must still pass)",
            VedicMathValidator.evaluateHandshake(alteredPromptHandshake, "5")
        )
        assertFalse(
            "TEST 7: Prompt text mentioning 9 must NOT make 9 pass when expectedAnswer is 5",
            VedicMathValidator.evaluateHandshake(alteredPromptHandshake, "9")
        )

        // TEST 8 — No expected answer + targetElementId = "numpad_key_5" -> FAIL
        // (targetElementId alone without expectedAnswer cannot validate or infer an answer)
        val targetIdOnlyHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_5",
            promptText = null,
            expectedAnswer = null
        )
        assertFalse(
            "TEST 8: targetElementId without expectedAnswer must fail validation (no inference)",
            VedicMathValidator.evaluateHandshake(targetIdOnlyHandshake, "5")
        )

        // TEST 9 — No expected answer + prompt = "Tap 5 to begin" -> FAIL
        // (promptText alone without expectedAnswer cannot validate or infer an answer)
        val promptOnlyHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = null,
            promptText = "Tap 5 to begin!",
            expectedAnswer = null
        )
        assertFalse(
            "TEST 9: promptText without expectedAnswer must fail validation (no inference)",
            VedicMathValidator.evaluateHandshake(promptOnlyHandshake, "5")
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
}
