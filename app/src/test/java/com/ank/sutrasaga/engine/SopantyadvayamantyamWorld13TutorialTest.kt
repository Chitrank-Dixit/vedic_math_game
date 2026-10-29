package com.ank.sutrasaga.engine

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ank.sutrasaga.data.repository.RiveDialogueRepository
import com.ank.sutrasaga.domain.models.InteractiveHandshake
import com.ank.sutrasaga.domain.models.RiveAnimationConfig
import com.ank.sutrasaga.domain.models.RiveSpeaker
import com.ank.sutrasaga.domain.models.RiveSutraDialogueTree
import com.ank.sutrasaga.domain.models.SutraProblem
import com.ank.sutrasaga.domain.validation.RiveDialogueValidator
import com.ank.sutrasaga.domain.validation.VedicMathValidator
import com.ank.sutrasaga.engine.rive.DefaultRiveAdapter
import com.ank.sutrasaga.engine.rive.RiveDialogueController
import com.ank.sutrasaga.ui.components.generateHandshakeChoices
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SopantyadvayamantyamWorld13TutorialTest {

    private lateinit var context: Context
    private lateinit var repository: RiveDialogueRepository
    private lateinit var world13Tree: RiveSutraDialogueTree
    private lateinit var generator: SopantyadvayamantyamGenerator

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = RiveDialogueRepository(context)
        val tree = repository.getDialogueForWorld(13)
        assertNotNull("World 13 dialogue must exist in repository", tree)
        world13Tree = tree!!
        generator = SopantyadvayamantyamGenerator()
    }

    // ── 1. Sutra Identification & Metadata ──────────────────────────────────────

    @Test
    fun testWorld13SutraIdentification() {
        assertEquals("sopantyadvayamantyam", world13Tree.sutraId)
        assertEquals("Sopāntyadvayamantyam", world13Tree.sutraName)
        assertEquals("The Ultimate and Twice the Penultimate", world13Tree.englishMeaning)
        assertEquals(13, world13Tree.worldNumber)
    }

    // ── 2. Mathematical Correctness & Independent Verification ──────────────────

    @Test
    fun testCanonicalTutorialProblemProducesProduct1716() {
        val problem: SutraProblem = generator.generateCanonicalTutorialProblem()
        assertEquals(1716L, problem.correctAnswer)
        assertEquals("Sopantyadvayamantyam", problem.sutraName)
        assertEquals("1716", problem.appendedSuffix)
        assertEquals(143L, problem.prefixPart)
        assertEquals(12L, problem.incrementedPrefix)
        assertEquals(2L, problem.prefixProduct)

        // Independent verification of 143 x 12
        val sol = generator.solveMultiplication(143, 12)
        assertEquals(listOf(1, 6, 11, 6), sol.rawValues.map { it.rawValue })
        assertEquals(listOf(1, 7, 1, 6), sol.finalDigits)
        assertEquals(1716L, sol.product)
        assertEquals(143L * 12L, sol.product)

        assertTrue(
            "VedicMathValidator must verify 143 x 12 with raw values [1, 6, 11, 6]",
            VedicMathValidator.verifySopantyadvayamantyam(
                multiplicand = 143L,
                multiplier = 12,
                expectedRawValues = listOf(1, 6, 11, 6),
                expectedProduct = 1716L
            )
        )
    }

    @Test
    fun testIntermediateStepRawValuesAndCarryResolution() {
        // Multiplicand: 143, Multiplier: 12 (N = 2)
        val sandwiched = listOf(0, 1, 4, 3, 0)
        val n = 2

        // Pos 0: digit 1, prev 0 -> 1 + 2(0) = 1
        val r0 = sandwiched[1] + n * sandwiched[0]
        assertEquals(1, r0)

        // Pos 1: digit 4, prev 1 -> 4 + 2(1) = 6
        val r1 = sandwiched[2] + n * sandwiched[1]
        assertEquals(6, r1)

        // Pos 2 (Handshake step): digit 3, prev 4 -> 3 + 2(4) = 11
        val r2 = sandwiched[3] + n * sandwiched[2]
        assertEquals(11, r2) // Handshake answer!

        // Pos 3: digit 0, prev 3 -> 0 + 2(3) = 6
        val r3 = sandwiched[4] + n * sandwiched[3]
        assertEquals(6, r3)

        // Carry propagation right-to-left:
        // Pos 3: raw 6 -> digit 6, carry 0
        var carry = 0
        val d3 = (r3 + carry) % 10
        carry = (r3 + carry) / 10
        assertEquals(6, d3)
        assertEquals(0, carry)

        // Pos 2: raw 11 + carry 0 = 11 -> digit 1, carry 1
        val d2 = (r2 + carry) % 10
        carry = (r2 + carry) / 10
        assertEquals(1, d2)
        assertEquals(1, carry)

        // Pos 1: raw 6 + carry 1 = 7 -> digit 7, carry 0
        val d1 = (r1 + carry) % 10
        carry = (r1 + carry) / 10
        assertEquals(7, d1)
        assertEquals(0, carry)

        // Pos 0: raw 1 + carry 0 = 1 -> digit 1, carry 0
        val d0 = (r0 + carry) % 10
        carry = (r0 + carry) / 10
        assertEquals(1, d0)
        assertEquals(0, carry)

        val product = "$d0$d1$d2$d3".toLong()
        assertEquals(1716L, product)
    }

    @Test
    fun testHandshakeCalculationThreePlusTwoTimesFourYieldsEleven() {
        val digit = 3
        val neighbor = 4
        val n = 2
        val rawValue = digit + n * neighbor
        assertEquals(11, rawValue)
    }

    @Test
    fun test20HandVerifiedCasesMultipliers12To19() {
        val cases = listOf(
            TestCase(12L, 12, listOf(1, 4, 4), 144L),
            TestCase(25L, 12, listOf(2, 9, 10), 300L),
            TestCase(34L, 13, listOf(3, 13, 12), 442L),
            TestCase(51L, 13, listOf(5, 16, 3), 663L),
            TestCase(62L, 14, listOf(6, 26, 8), 868L),
            TestCase(111L, 14, listOf(1, 5, 5, 4), 1554L),
            TestCase(43L, 15, listOf(4, 23, 15), 645L),
            TestCase(123L, 15, listOf(1, 7, 13, 15), 1845L),
            TestCase(72L, 16, listOf(7, 44, 12), 1152L),
            TestCase(214L, 16, listOf(2, 13, 10, 24), 3424L),
            TestCase(31L, 17, listOf(3, 22, 7), 527L),
            TestCase(105L, 17, listOf(1, 7, 5, 35), 1785L),
            TestCase(44L, 18, listOf(4, 36, 32), 792L),
            TestCase(222L, 18, listOf(2, 18, 18, 16), 3996L),
            TestCase(15L, 19, listOf(1, 14, 45), 285L),
            TestCase(101L, 19, listOf(1, 9, 1, 9), 1919L),
            TestCase(502L, 12, listOf(5, 10, 2, 4), 6024L),
            TestCase(313L, 13, listOf(3, 10, 6, 9), 4069L),
            TestCase(404L, 14, listOf(4, 16, 4, 16), 5656L),
            TestCase(707L, 15, listOf(7, 35, 7, 35), 10605L)
        )

        for (c in cases) {
            val sol = generator.solveMultiplication(c.multiplicand, c.multiplier)
            assertEquals("Product mismatch for ${c.multiplicand} × ${c.multiplier}", c.expectedProduct, sol.product)
            assertEquals("Raw values mismatch for ${c.multiplicand} × ${c.multiplier}", c.expectedRawValues, sol.rawValues.map { it.rawValue })
            assertTrue(
                "VedicMathValidator check for ${c.multiplicand} × ${c.multiplier}",
                VedicMathValidator.verifySopantyadvayamantyam(c.multiplicand, c.multiplier, c.expectedRawValues, c.expectedProduct)
            )
        }
    }

    private data class TestCase(
        val multiplicand: Long,
        val multiplier: Int,
        val expectedRawValues: List<Int>,
        val expectedProduct: Long
    )

    // ── 3. Dialogue Tree Schema & 4 Beats ──────────────────────────────────────

    @Test
    fun testDialogueTreePassesValidator() {
        val result = RiveDialogueValidator.validate(world13Tree)
        assertTrue("World 13 dialogue tree must be valid: ${result.errors}", result.isValid)
        assertTrue("World 13 dialogue tree must have zero validation errors", result.errors.isEmpty())
    }

    @Test
    fun testDialogueTreeHasExactlyFourBeats() {
        assertEquals("World 13 must have exactly 4 dialogue nodes", 4, world13Tree.dialogueNodes.size)
    }

    @Test
    fun testNode1IsShishyaHook() {
        val node1 = world13Tree.dialogueNodes[0]
        assertEquals(1, node1.nodeId)
        assertEquals(RiveSpeaker.SHISHYA, node1.speaker)
        assertTrue("Node 1 text must not be blank", node1.text.isNotBlank())
        assertTrue("Node 1 text must mention 143 by 12 or multi-line multiplication", node1.text.contains("143") || node1.text.contains("multiplication"))
        assertEquals("shishya_curious", node1.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.SHISHYA, node1.riveState.animation))
        assertFalse(node1.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode2IsGuruSutraReveal() {
        val node2 = world13Tree.dialogueNodes[1]
        assertEquals(2, node2.nodeId)
        assertEquals(RiveSpeaker.GURU, node2.speaker)
        assertTrue("Node 2 text must not be blank", node2.text.isNotBlank())
        assertTrue("Node 2 text must introduce Sopāntyadvayamantyam", node2.text.contains("Sopāntyadvayamantyam") || node2.text.contains("Twice the Penultimate"))
        assertEquals("guru_explain", node2.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node2.riveState.animation))
        assertFalse(node2.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode3IsGuruGuidedDemonstration() {
        val node3 = world13Tree.dialogueNodes[2]
        assertEquals(3, node3.nodeId)
        assertEquals(RiveSpeaker.GURU, node3.speaker)
        assertTrue("Node 3 text must not be blank", node3.text.isNotBlank())
        assertTrue("Node 3 text must explain product 1716", node3.text.contains("1716"))
        assertEquals("guru_explain", node3.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node3.riveState.animation))
        assertFalse(node3.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode4IsGuruActiveHandshake() {
        val node4 = world13Tree.dialogueNodes[3]
        assertEquals(4, node4.nodeId)
        assertEquals(RiveSpeaker.GURU, node4.speaker)
        assertTrue("Node 4 text must not be blank", node4.text.isNotBlank())
        assertTrue("Node 4 text must ask for the raw value 3 + (2 × 4)", node4.text.contains("3 + (2 × 4)") || node4.text.contains("raw value"))
        assertEquals("guru_magic_fx", node4.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node4.riveState.animation))
        assertTrue("Node 4 must require user tap", node4.interactiveHandshake.requiresUserTap)
        assertEquals("numpad_key_11", node4.interactiveHandshake.targetElementId)
        assertEquals("11", node4.interactiveHandshake.expectedAnswer)
    }

    @Test
    fun testGuruNeverWinks() {
        for (node in world13Tree.dialogueNodes) {
            if (node.speaker == RiveSpeaker.GURU) {
                assertFalse(
                    "Guru animations must never be guru_wink in node ${node.nodeId}",
                    node.riveState.animation.contains("wink")
                )
            }
        }
    }

    @Test
    fun testAllNodesHaveAudioCuesWithSpeakTag() {
        for (node in world13Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} SSML must contain <speak>", node.audioCues.ssmlText.contains("<speak>"))
            assertTrue("Node ${node.nodeId} SSML must contain </speak>", node.audioCues.ssmlText.contains("</speak>"))
            assertTrue("Node ${node.nodeId} SFX must not be blank", node.audioCues.sfx.isNotBlank())
        }
    }

    @Test
    fun testAllNodesHaveNonBlankMathOverlays() {
        for (node in world13Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} expression must not be blank", node.mathOverlay.expression.isNotBlank())
            assertTrue("Node ${node.nodeId} highlight tokens must not be empty", node.mathOverlay.highlightTokens.isNotEmpty())
        }
    }

    // ── 4. Active Handshake & Expected Answer Authority ────────────────────────

    @Test
    fun testHandshakeAuthoritativeAnswerIsEleven() {
        val handshake = world13Tree.dialogueNodes.last().interactiveHandshake
        assertEquals("Authoritative expected answer must be '11'", "11", handshake.expectedAnswer)
    }

    @Test
    fun testHandshakePromptDoesNotRevealExpectedAnswer() {
        val handshake = world13Tree.dialogueNodes.last().interactiveHandshake
        val prompt = handshake.promptText
        assertNotNull("Prompt must not be null", prompt)
        assertFalse(
            "Prompt '$prompt' must not leak '= 11' or 'is 11' or 'Tap 11'",
            prompt!!.contains("= 11") || prompt.contains("is 11") || prompt.contains("Tap 11")
        )
    }

    @Test
    fun testHandshakeMathOverlayExpressionDoesNotRevealExpectedAnswer() {
        val finalNode = world13Tree.dialogueNodes.last()
        val expr = finalNode.mathOverlay.expression
        assertTrue("Overlay should indicate unknown: '$expr'", expr.contains("?") || expr.contains("[ ? ]"))
        assertFalse("Overlay must not leak '= 11': '$expr'", expr.contains("= 11"))
    }

    @Test
    fun testHandshakeChoicesIncludeExpectedAnswer() {
        val expected = world13Tree.dialogueNodes.last().interactiveHandshake.expectedAnswer
        val choices = generateHandshakeChoices(expected)
        assertTrue("Generated choices $choices must contain '$expected'", choices.contains(expected))
        assertEquals("Generated choices for '11' must be 5 items", 5, choices.size)
        assertEquals(listOf("9", "10", "11", "12", "13"), choices)
    }

    @Test
    fun testHandshakeFailsClosedOnNullOrBlankExpectedAnswer() {
        val nullAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_11",
            promptText = "Calculate the raw value: 3 + (2 × 4) = ?",
            expectedAnswer = null
        )
        assertFalse(
            "Null expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(nullAnswerHandshake, "11")
        )

        val blankAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_11",
            promptText = "Calculate the raw value: 3 + (2 × 4) = ?",
            expectedAnswer = "   "
        )
        assertFalse(
            "Blank expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(blankAnswerHandshake, "11")
        )
    }

    @Test
    fun testHandshakeEvaluatesStrictlyAgainstExpectedAnswer() {
        val handshake = world13Tree.dialogueNodes.last().interactiveHandshake

        assertTrue("Submitting '11' must pass", VedicMathValidator.evaluateHandshake(handshake, "11"))
        assertTrue("Submitting ' 11 ' with whitespace must pass", VedicMathValidator.evaluateHandshake(handshake, " 11 "))

        assertFalse("Submitting '9' must fail", VedicMathValidator.evaluateHandshake(handshake, "9"))
        assertFalse("Submitting '10' must fail", VedicMathValidator.evaluateHandshake(handshake, "10"))
        assertFalse("Submitting '12' must fail", VedicMathValidator.evaluateHandshake(handshake, "12"))
        assertFalse("Submitting '13' must fail", VedicMathValidator.evaluateHandshake(handshake, "13"))
        assertFalse("Submitting '6' must fail", VedicMathValidator.evaluateHandshake(handshake, "6"))
        assertFalse("Submitting '1716' must fail", VedicMathValidator.evaluateHandshake(handshake, "1716"))
        assertFalse("Submitting empty string must fail", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("Submitting whitespace must fail", VedicMathValidator.evaluateHandshake(handshake, "   "))
    }

    // ── 5. Controller & Skip Behavior ──────────────────────────────────────────

    @Test
    fun testDialogueControllerAdvancesThroughFirstThreeNodes() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world13Tree, adapter)

        assertEquals(0, controller.currentNodeIndex.value)
        assertFalse(controller.isCompleted.value)

        // Advance to node 2
        assertTrue(controller.advanceNode())
        assertEquals(1, controller.currentNodeIndex.value)

        // Advance to node 3
        assertTrue(controller.advanceNode())
        assertEquals(2, controller.currentNodeIndex.value)

        // Advance to node 4 (handshake)
        assertTrue(controller.advanceNode())
        assertEquals(3, controller.currentNodeIndex.value)
    }

    @Test
    fun testDialogueControllerBlocksAdvanceOnUnresolvedHandshake() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world13Tree, adapter)

        // Advance to handshake node (node 4)
        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        // Cannot advance without solving handshake
        val advanced = controller.advanceNode()
        assertFalse("advanceNode must return false on active handshake node", advanced)
        assertFalse("Dialogue must not be marked completed", controller.isCompleted.value)
    }

    @Test
    fun testDialogueControllerSubmittingIncorrectAnswerFails() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world13Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("10")
        assertFalse("Incorrect answer '10' must return false", success)
        assertFalse("Dialogue must not complete", controller.isCompleted.value)
        assertNotNull("Handshake error message must be set", controller.handshakeError.value)
        assertEquals("shishya_puzzled", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSubmittingCorrectAnswerCompletesDialogue() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world13Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("11")
        assertTrue("Correct answer '11' must return true", success)
        assertTrue("Dialogue must be marked completed", controller.isCompleted.value)
        assertEquals("shishya_celebrate", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSkipBehavior() {
        // Case 1: allowBypassHandshake = false (default student path)
        val adapter1 = DefaultRiveAdapter()
        val controller1 = RiveDialogueController(world13Tree, adapter1)

        repeat(3) { controller1.advanceNode() }
        assertEquals(3, controller1.currentNodeIndex.value)

        val skipped1 = controller1.skip(allowBypassHandshake = false)
        assertFalse("skip must return false when handshake is unresolved and bypass is disabled", skipped1)
        assertFalse("Dialogue must not be completed", controller1.isCompleted.value)

        // Case 2: allowBypassHandshake = true (debug / reviewer bypass)
        val adapter2 = DefaultRiveAdapter()
        val controller2 = RiveDialogueController(world13Tree, adapter2)

        repeat(3) { controller2.advanceNode() }
        val skipped2 = controller2.skip(allowBypassHandshake = true)
        assertTrue("skip must return true when bypass is allowed", skipped2)
        assertTrue("Dialogue must be completed when bypassed", controller2.isCompleted.value)
    }

    @Test
    fun testDialogueControllerSkipAfterHandshakeCompletion() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world13Tree, adapter)

        repeat(3) { controller.advanceNode() }
        val solved = controller.submitHandshake("11")
        assertTrue(solved)

        val skipped = controller.skip(allowBypassHandshake = false)
        assertTrue("skip must succeed once handshake is completed", skipped)
        assertTrue(controller.isCompleted.value)
    }

    // ── 6. Non-regression of Worlds 1–12 ───────────────────────────────────────

    @Test
    fun testWorld1TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(1)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testWorld2TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(2)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testWorld3TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(3)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testWorld4TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(4)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testWorld5TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(5)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testWorld6TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(6)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testWorld7TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(7)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testWorld8TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(8)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testWorld9TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(9)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testWorld10TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(10)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testWorld11TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(11)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testWorld12TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(12)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testAllWorldsOneThroughThirteenHaveDistinctWorldNumbers() {
        for (i in 1..13) {
            val tree = repository.getDialogueForWorld(i)
            assertNotNull("World $i must exist", tree)
            assertEquals("World $i number mismatch", i, tree!!.worldNumber)
        }
    }
}
