package com.ank.sutrasaga.engine

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ank.sutrasaga.data.repository.RiveDialogueRepository
import com.ank.sutrasaga.domain.models.GunakasamuccayahClassification
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
class GunakasamuccayahWorld15TutorialTest {

    private lateinit var context: Context
    private lateinit var repository: RiveDialogueRepository
    private lateinit var world15Tree: RiveSutraDialogueTree
    private lateinit var generator: GunakasamuccayahGenerator

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = RiveDialogueRepository(context)
        val tree = repository.getDialogueForWorld(15)
        assertNotNull("World 15 dialogue must exist in repository", tree)
        world15Tree = tree!!
        generator = GunakasamuccayahGenerator()
    }

    // ── 1. Sutra Identification & Metadata ──────────────────────────────────────

    @Test
    fun testWorld15SutraIdentification() {
        assertEquals("gunakasamuccayah", world15Tree.sutraId)
        assertEquals("Guṇakasamuccayaḥ", world15Tree.sutraName)
        assertEquals("The Factors of the Sum is the Sum of the Factors", world15Tree.englishMeaning)
        assertEquals(15, world15Tree.worldNumber)
    }

    // ── 2. Mathematical Correctness & Independent Verification ──────────────────

    @Test
    fun testCanonicalTutorialProblemProducesRootsNegativeTwoAndNegativeFive() {
        val problem: SutraProblem = generator.generateCanonicalTutorialProblem()
        assertEquals(-2L, problem.correctAnswer)
        assertEquals(-5L, problem.expectedSecondaryAnswer)
        assertEquals("Gunakasamuccayah", problem.sutraName)
        assertEquals(7L, problem.prefixPart)
        assertEquals(10L, problem.incrementedPrefix)
        assertEquals(2L, problem.prefixProduct)
        assertEquals("5", problem.appendedSuffix)

        // Independent verification of x^2 + 7x + 10 = 0
        val sol = generator.solveQuadratic(7, 10)
        assertEquals(GunakasamuccayahClassification.FACTORED, sol.classification)
        assertEquals(2L, sol.matchingPair?.p)
        assertEquals(5L, sol.matchingPair?.q)
        assertEquals(listOf(-2L, -5L), sol.roots)

        assertTrue(
            "VedicMathValidator must verify canonical factorization x^2+7x+10 = 0",
            VedicMathValidator.verifyGunakasamuccayah(7L, 10L, 2L, 5L, -2L, -5L)
        )
    }

    @Test
    fun testIntermediateStepFactorPairSelectionAndRootSolving() {
        val b = 7L
        val c = 10L

        // Constant term factor pairs of 10:
        // (1, 10) -> 1 + 10 = 11 != 7
        assertEquals(11L, 1L + 10L)

        // (2, 5) -> 2 + 5 = 7 == B!
        val p = 2L
        val q = 5L
        assertEquals(10L, p * q)
        assertEquals(b, p + q)

        // Solve factors for roots:
        // x + 2 = 0 -> x = -2 (Handshake question)
        val r1 = -p
        assertEquals(-2L, r1)

        // x + 5 = 0 -> x = -5
        val r2 = -q
        assertEquals(-5L, r2)

        // Root substitution into x^2 + 7x + 10:
        assertEquals(0L, r1 * r1 + b * r1 + c)
        assertEquals(0L, r2 * r2 + b * r2 + c)
    }

    @Test
    fun testHandshakeCalculationZeroMinusTwoYieldsNegativeTwo() {
        val factorOffset = 2L
        val root = -factorOffset
        assertEquals(-2L, root)
    }

    @Test
    fun testNonFactorizableQuadraticDetected() {
        // x^2 + x + 1 = 0
        val sol = generator.solveQuadratic(1, 1)
        assertEquals(GunakasamuccayahClassification.NO_INTEGER_FACTOR_PAIR, sol.classification)
        assertTrue(sol.roots.isEmpty())
    }

    @Test
    fun test20HandVerifiedQuadraticCases() {
        val cases = listOf(
            TestCase(7L, 10L, 2L, 5L, -2L, -5L),
            TestCase(1L, -6L, 3L, -2L, -3L, 2L),
            TestCase(-9L, 20L, -4L, -5L, 4L, 5L),
            TestCase(5L, 6L, 2L, 3L, -2L, -3L),
            TestCase(-5L, 6L, -2L, -3L, 2L, 3L),
            TestCase(3L, 2L, 1L, 2L, -1L, -2L),
            TestCase(-3L, 2L, -1L, -2L, 1L, 2L),
            TestCase(8L, 15L, 3L, 5L, -3L, -5L),
            TestCase(-8L, 15L, -3L, -5L, 3L, 5L),
            TestCase(2L, -8L, 4L, -2L, -4L, 2L),
            TestCase(-2L, -8L, -4L, 2L, 4L, -2L),
            TestCase(4L, 3L, 1L, 3L, -1L, -3L),
            TestCase(-4L, 3L, -1L, -3L, 1L, 3L),
            TestCase(6L, 8L, 2L, 4L, -2L, -4L),
            TestCase(-6L, 8L, -2L, -4L, 2L, 4L),
            TestCase(0L, -9L, 3L, -3L, -3L, 3L),
            TestCase(0L, -16L, 4L, -4L, -4L, 4L),
            TestCase(9L, 14L, 2L, 7L, -2L, -7L),
            TestCase(-9L, 14L, -2L, -7L, 2L, 7L),
            TestCase(10L, 21L, 3L, 7L, -3L, -7L)
        )

        for (c in cases) {
            val sol = generator.solveQuadratic(c.b, c.c)
            assertEquals("Classification for x^2 + ${c.b}x + ${c.c}", GunakasamuccayahClassification.FACTORED, sol.classification)
            assertTrue(
                "VedicMathValidator check for x^2 + ${c.b}x + ${c.c}",
                VedicMathValidator.verifyGunakasamuccayah(c.b, c.c, c.p, c.q, c.r1, c.r2)
            )
        }
    }

    private data class TestCase(
        val b: Long, val c: Long,
        val p: Long, val q: Long,
        val r1: Long, val r2: Long
    )

    // ── 3. Dialogue Tree Schema & 4 Beats ──────────────────────────────────────

    @Test
    fun testDialogueTreePassesValidator() {
        val result = RiveDialogueValidator.validate(world15Tree)
        assertTrue("World 15 dialogue tree must be valid: ${result.errors}", result.isValid)
        assertTrue("World 15 dialogue tree must have zero validation errors", result.errors.isEmpty())
    }

    @Test
    fun testDialogueTreeHasExactlyFourBeats() {
        assertEquals("World 15 must have exactly 4 dialogue nodes", 4, world15Tree.dialogueNodes.size)
    }

    @Test
    fun testNode1IsShishyaHook() {
        val node1 = world15Tree.dialogueNodes[0]
        assertEquals(1, node1.nodeId)
        assertEquals(RiveSpeaker.SHISHYA, node1.speaker)
        assertTrue("Node 1 text must not be blank", node1.text.isNotBlank())
        assertTrue("Node 1 text must mention quadratics or x² + 7x + 10", node1.text.contains("quadratics") || node1.text.contains("x² + 7x + 10"))
        assertEquals("shishya_curious", node1.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.SHISHYA, node1.riveState.animation))
        assertFalse(node1.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode2IsGuruSutraReveal() {
        val node2 = world15Tree.dialogueNodes[1]
        assertEquals(2, node2.nodeId)
        assertEquals(RiveSpeaker.GURU, node2.speaker)
        assertTrue("Node 2 text must not be blank", node2.text.isNotBlank())
        assertTrue("Node 2 text must introduce Guṇakasamuccayaḥ", node2.text.contains("Guṇakasamuccayaḥ") || node2.text.contains("Sum of the Factors"))
        assertEquals("guru_explain", node2.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node2.riveState.animation))
        assertFalse(node2.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode3IsGuruGuidedDemonstration() {
        val node3 = world15Tree.dialogueNodes[2]
        assertEquals(3, node3.nodeId)
        assertEquals(RiveSpeaker.GURU, node3.speaker)
        assertTrue("Node 3 text must not be blank", node3.text.isNotBlank())
        assertTrue("Node 3 text must explain factor pair (2, 5) and roots −2 and −5", node3.text.contains("−2") && node3.text.contains("−5"))
        assertEquals("guru_explain", node3.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node3.riveState.animation))
        assertFalse(node3.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode4IsGuruActiveHandshake() {
        val node4 = world15Tree.dialogueNodes[3]
        assertEquals(4, node4.nodeId)
        assertEquals(RiveSpeaker.GURU, node4.speaker)
        assertTrue("Node 4 text must not be blank", node4.text.isNotBlank())
        assertTrue("Node 4 text must ask for the root x from x + 2 = 0", node4.text.contains("x + 2 = 0") || node4.text.contains("root x"))
        assertEquals("guru_magic_fx", node4.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node4.riveState.animation))
        assertTrue("Node 4 must require user tap", node4.interactiveHandshake.requiresUserTap)
        assertEquals("numpad_key_minus_2", node4.interactiveHandshake.targetElementId)
        assertEquals("-2", node4.interactiveHandshake.expectedAnswer)
    }

    @Test
    fun testGuruNeverWinks() {
        for (node in world15Tree.dialogueNodes) {
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
        for (node in world15Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} SSML must contain <speak>", node.audioCues.ssmlText.contains("<speak>"))
            assertTrue("Node ${node.nodeId} SSML must contain </speak>", node.audioCues.ssmlText.contains("</speak>"))
            assertTrue("Node ${node.nodeId} SFX must not be blank", node.audioCues.sfx.isNotBlank())
        }
    }

    @Test
    fun testAllNodesHaveNonBlankMathOverlays() {
        for (node in world15Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} expression must not be blank", node.mathOverlay.expression.isNotBlank())
            assertTrue("Node ${node.nodeId} highlight tokens must not be empty", node.mathOverlay.highlightTokens.isNotEmpty())
        }
    }

    // ── 4. Active Handshake & Expected Answer Authority ────────────────────────

    @Test
    fun testHandshakeAuthoritativeAnswerIsMinusTwo() {
        val handshake = world15Tree.dialogueNodes.last().interactiveHandshake
        assertEquals("Authoritative expected answer must be '-2'", "-2", handshake.expectedAnswer)
    }

    @Test
    fun testHandshakePromptDoesNotRevealExpectedAnswer() {
        val handshake = world15Tree.dialogueNodes.last().interactiveHandshake
        val prompt = handshake.promptText
        assertNotNull("Prompt must not be null", prompt)
        assertFalse(
            "Prompt '$prompt' must not leak '=-2' or 'is -2' or 'Tap -2'",
            prompt!!.contains("=-2") || prompt.contains("is -2") || prompt.contains("Tap -2")
        )
    }

    @Test
    fun testHandshakeMathOverlayExpressionDoesNotRevealExpectedAnswer() {
        val finalNode = world15Tree.dialogueNodes.last()
        val expr = finalNode.mathOverlay.expression
        assertTrue("Overlay should indicate unknown: '$expr'", expr.contains("?") || expr.contains("[ ? ]"))
        assertFalse("Overlay must not leak '=-2': '$expr'", expr.contains("=-2"))
    }

    @Test
    fun testHandshakeChoicesIncludeExpectedAnswer() {
        val expected = world15Tree.dialogueNodes.last().interactiveHandshake.expectedAnswer
        val choices = generateHandshakeChoices(expected)
        assertTrue("Generated choices $choices must contain '$expected'", choices.contains(expected))
        assertEquals("Generated choices for '-2' must be 5 items", 5, choices.size)
        assertEquals(listOf("-4", "-3", "-2", "-1", "0"), choices)
    }

    @Test
    fun testHandshakeFailsClosedOnNullOrBlankExpectedAnswer() {
        val nullAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_minus_2",
            promptText = "Solve the linear factor for x: x + 2 = 0 ⟹ x = ?",
            expectedAnswer = null
        )
        assertFalse(
            "Null expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(nullAnswerHandshake, "-2")
        )

        val blankAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_minus_2",
            promptText = "Solve the linear factor for x: x + 2 = 0 ⟹ x = ?",
            expectedAnswer = "   "
        )
        assertFalse(
            "Blank expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(blankAnswerHandshake, "-2")
        )
    }

    @Test
    fun testHandshakeEvaluatesStrictlyAgainstExpectedAnswer() {
        val handshake = world15Tree.dialogueNodes.last().interactiveHandshake

        assertTrue("Submitting '-2' must pass", VedicMathValidator.evaluateHandshake(handshake, "-2"))
        assertTrue("Submitting ' -2 ' with whitespace must pass", VedicMathValidator.evaluateHandshake(handshake, " -2 "))

        assertFalse("Submitting '2' must fail", VedicMathValidator.evaluateHandshake(handshake, "2"))
        assertFalse("Submitting '-4' must fail", VedicMathValidator.evaluateHandshake(handshake, "-4"))
        assertFalse("Submitting '-3' must fail", VedicMathValidator.evaluateHandshake(handshake, "-3"))
        assertFalse("Submitting '-1' must fail", VedicMathValidator.evaluateHandshake(handshake, "-1"))
        assertFalse("Submitting '0' must fail", VedicMathValidator.evaluateHandshake(handshake, "0"))
        assertFalse("Submitting '-5' must fail", VedicMathValidator.evaluateHandshake(handshake, "-5"))
        assertFalse("Submitting empty string must fail", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("Submitting whitespace must fail", VedicMathValidator.evaluateHandshake(handshake, "   "))
    }

    // ── 5. Controller & Skip Behavior ──────────────────────────────────────────

    @Test
    fun testDialogueControllerAdvancesThroughFirstThreeNodes() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world15Tree, adapter)

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
        val controller = RiveDialogueController(world15Tree, adapter)

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
        val controller = RiveDialogueController(world15Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("2")
        assertFalse("Incorrect answer '2' must return false", success)
        assertFalse("Dialogue must not complete", controller.isCompleted.value)
        assertNotNull("Handshake error message must be set", controller.handshakeError.value)
        assertEquals("shishya_puzzled", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSubmittingCorrectAnswerCompletesDialogue() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world15Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("-2")
        assertTrue("Correct answer '-2' must return true", success)
        assertTrue("Dialogue must be marked completed", controller.isCompleted.value)
        assertEquals("shishya_celebrate", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSkipBehavior() {
        // Case 1: allowBypassHandshake = false (default student path)
        val adapter1 = DefaultRiveAdapter()
        val controller1 = RiveDialogueController(world15Tree, adapter1)

        repeat(3) { controller1.advanceNode() }
        assertEquals(3, controller1.currentNodeIndex.value)

        val skipped1 = controller1.skip(allowBypassHandshake = false)
        assertFalse("skip must return false when handshake is unresolved and bypass is disabled", skipped1)
        assertFalse("Dialogue must not be completed", controller1.isCompleted.value)

        // Case 2: allowBypassHandshake = true (debug / reviewer bypass)
        val adapter2 = DefaultRiveAdapter()
        val controller2 = RiveDialogueController(world15Tree, adapter2)

        repeat(3) { controller2.advanceNode() }
        val skipped2 = controller2.skip(allowBypassHandshake = true)
        assertTrue("skip must return true when bypass is allowed", skipped2)
        assertTrue("Dialogue must be completed when bypassed", controller2.isCompleted.value)
    }

    @Test
    fun testDialogueControllerSkipAfterHandshakeCompletion() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world15Tree, adapter)

        repeat(3) { controller.advanceNode() }
        val solved = controller.submitHandshake("-2")
        assertTrue(solved)

        val skipped = controller.skip(allowBypassHandshake = false)
        assertTrue("skip must succeed once handshake is completed", skipped)
        assertTrue(controller.isCompleted.value)
    }

    // ── 6. Non-regression of Worlds 1–14 ───────────────────────────────────────

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
    fun testWorld13TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(13)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testWorld14TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(14)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testAllWorldsOneThroughFifteenHaveDistinctWorldNumbers() {
        for (i in 1..15) {
            val tree = repository.getDialogueForWorld(i)
            assertNotNull("World $i must exist", tree)
            assertEquals("World $i number mismatch", i, tree!!.worldNumber)
        }
    }
}
