package com.ankh.sutrasaga.engine

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ankh.sutrasaga.data.repository.RiveDialogueRepository
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.InteractiveHandshake
import com.ankh.sutrasaga.domain.models.QuadraticClassification
import com.ankh.sutrasaga.domain.models.QuadraticEquation
import com.ankh.sutrasaga.domain.models.RiveAnimationConfig
import com.ankh.sutrasaga.domain.models.RiveSpeaker
import com.ankh.sutrasaga.domain.models.RiveSutraDialogueTree
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.validation.RiveDialogueValidator
import com.ankh.sutrasaga.domain.validation.VedicMathValidator
import com.ankh.sutrasaga.engine.rive.DefaultRiveAdapter
import com.ankh.sutrasaga.engine.rive.RiveDialogueController
import com.ankh.sutrasaga.ui.components.generateHandshakeChoices
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
class PuranapuranabhyamWorld10TutorialTest {

    private lateinit var context: Context
    private lateinit var repository: RiveDialogueRepository
    private lateinit var world10Tree: RiveSutraDialogueTree
    private lateinit var generator: PuranapuranabhyamGenerator

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = RiveDialogueRepository(context)
        val tree = repository.getDialogueForWorld(10)
        assertNotNull("World 10 dialogue must exist in repository", tree)
        world10Tree = tree!!
        generator = PuranapuranabhyamGenerator()
    }

    // ── 1. Sutra Identification & Metadata ──────────────────────────────────────

    @Test
    fun testWorld10SutraIdentification() {
        assertEquals("puranapuranabhyam", world10Tree.sutraId)
        assertEquals("Pūraṇāpūraṇābhyām", world10Tree.sutraName)
        assertEquals("By the Completion or Non-Completion", world10Tree.englishMeaning)
        assertEquals(10, world10Tree.worldNumber)
    }

    // ── 2. Mathematical Correctness & Independent Verification ──────────────────

    @Test
    fun testCanonicalTutorialProblemProducesRootsMinus2AndMinus4() {
        val problem: SutraProblem = generator.generateCanonicalTutorialProblem()
        assertEquals(-2L, problem.correctAnswer)
        assertEquals(-4L, problem.expectedSecondaryAnswer)
        assertEquals("Puranapuranabhyam", problem.sutraName)

        // Verify independent solver logic for x² + 6x + 8 = 0
        val eq = QuadraticEquation(1L, 6L, 8L)
        val sol = generator.solveQuadratic(eq)
        assertEquals(QuadraticClassification.TWO_REAL_ROOTS, sol.classification)
        assertEquals(Fraction(-2L, 1L), sol.root1.reduced)
        assertEquals(Fraction(-4L, 1L), sol.root2.reduced)
        assertEquals(Fraction(9L, 1L), sol.completionTerm.reduced)

        // Verify with VedicMathValidator
        assertTrue(
            "VedicMathValidator must verify canonical quadratic completion and roots",
            VedicMathValidator.verifyPuranapuranabhyam(
                a = 1L,
                b = 6L,
                c = 8L,
                expectedCompletionNum = 9L,
                expectedCompletionDen = 1L,
                root1 = -2L,
                root2 = -4L
            )
        )
    }

    @Test
    fun testIntermediateStepCompletionTermCalculation() {
        // x² + 6x + 8 = 0 => x² + 6x = -8
        // Half of linear coefficient: 6 / 2 = 3
        val b = 6L
        val halfB = b / 2L
        assertEquals(3L, halfB)

        // Completion term: (b / 2)² = 3² = 9
        val tComp = halfB * halfB
        assertEquals(9L, tComp)

        // Adding 9 to both sides:
        val rhsMoved = -8L
        val rhsAfterCompletion = rhsMoved + tComp
        assertEquals(1L, rhsAfterCompletion)

        // Perfect square: (x + 3)² = 1 => x + 3 = ±1
        // Root 1: x + 3 = 1 => x = 1 - 3 = -2
        val r1 = 1L - halfB
        assertEquals(-2L, r1)

        // Root 2: x + 3 = -1 => x = -1 - 3 = -4
        val r2 = -1L - halfB
        assertEquals(-4L, r2)

        // Verify original quadratic equation holds for both roots
        assertEquals(0L, 1L * (r1 * r1) + 6L * r1 + 8L)
        assertEquals(0L, 1L * (r2 * r2) + 6L * r2 + 8L)
    }

    @Test
    fun testHandshakeCalculationHalfOfSixSquaredIsNine() {
        val halfCoeff = 6L / 2L
        val compTerm = halfCoeff * halfCoeff
        assertEquals(9L, compTerm)
    }

    @Test
    fun test20HandVerifiedQuadraticCases() {
        val cases = listOf(
            // Monic cases: (eq, root1, root2, compNum, compDen)
            QuadraticTestCase(1L, 6L, 8L, -2L, -4L, 9L, 1L),
            QuadraticTestCase(1L, 4L, 3L, -1L, -3L, 4L, 1L),
            QuadraticTestCase(1L, 2L, -3L, 1L, -3L, 1L, 1L),
            QuadraticTestCase(1L, 8L, 12L, -2L, -6L, 16L, 1L),
            QuadraticTestCase(1L, 10L, 21L, -3L, -7L, 25L, 1L),

            QuadraticTestCase(1L, -6L, 8L, 4L, 2L, 9L, 1L),
            QuadraticTestCase(1L, -4L, 3L, 3L, 1L, 4L, 1L),
            QuadraticTestCase(1L, -2L, -8L, 4L, -2L, 1L, 1L),
            QuadraticTestCase(1L, -8L, 15L, 5L, 3L, 16L, 1L),
            QuadraticTestCase(1L, -10L, 24L, 6L, 4L, 25L, 1L),

            QuadraticTestCase(1L, 4L, 4L, -2L, -2L, 4L, 1L),
            QuadraticTestCase(1L, -4L, 4L, 2L, 2L, 4L, 1L),
            QuadraticTestCase(1L, 6L, 9L, -3L, -3L, 9L, 1L),
            QuadraticTestCase(1L, -6L, 9L, 3L, 3L, 9L, 1L),
            QuadraticTestCase(1L, 2L, 1L, -1L, -1L, 1L, 1L),

            QuadraticTestCase(1L, 12L, 35L, -5L, -7L, 36L, 1L),
            QuadraticTestCase(1L, -12L, 32L, 8L, 4L, 36L, 1L),
            QuadraticTestCase(1L, 14L, 45L, -5L, -9L, 49L, 1L),
            QuadraticTestCase(1L, -14L, 48L, 8L, 6L, 49L, 1L),
            QuadraticTestCase(1L, 16L, 60L, -6L, -10L, 64L, 1L)
        )

        for (c in cases) {
            val eq = QuadraticEquation(c.a, c.b, c.c)
            val sol = generator.solveQuadratic(eq)
            val expectedClassif = if (c.r1 == c.r2) QuadraticClassification.REPEATED_REAL_ROOT else QuadraticClassification.TWO_REAL_ROOTS
            assertEquals("Classification for ${eq.toFormattedString()}", expectedClassif, sol.classification)
            assertEquals("Completion term for ${eq.toFormattedString()}", Fraction(c.compNum, c.compDen).reduced, sol.completionTerm.reduced)
            assertTrue(
                "VedicMathValidator check for ${eq.toFormattedString()}",
                VedicMathValidator.verifyPuranapuranabhyam(c.a, c.b, c.c, c.compNum, c.compDen, c.r1, c.r2)
            )
        }
    }

    private data class QuadraticTestCase(
        val a: Long,
        val b: Long,
        val c: Long,
        val r1: Long,
        val r2: Long,
        val compNum: Long,
        val compDen: Long
    )

    // ── 3. Dialogue Tree Schema & 4 Beats ──────────────────────────────────────

    @Test
    fun testDialogueTreePassesValidator() {
        val result = RiveDialogueValidator.validate(world10Tree)
        assertTrue("World 10 dialogue tree must be valid: ${result.errors}", result.isValid)
        assertTrue("World 10 dialogue tree must have zero validation errors", result.errors.isEmpty())
    }

    @Test
    fun testDialogueTreeHasExactlyFourBeats() {
        assertEquals("World 10 must have exactly 4 dialogue nodes", 4, world10Tree.dialogueNodes.size)
    }

    @Test
    fun testNode1IsShishyaHook() {
        val node1 = world10Tree.dialogueNodes[0]
        assertEquals(1, node1.nodeId)
        assertEquals(RiveSpeaker.SHISHYA, node1.speaker)
        assertTrue("Node 1 text must not be blank", node1.text.isNotBlank())
        assertTrue("Node 1 text must mention quadratic or x² + 6x + 8", node1.text.contains("x² + 6x + 8") || node1.text.contains("quadratics"))
        assertEquals("shishya_curious", node1.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.SHISHYA, node1.riveState.animation))
        assertFalse(node1.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode2IsGuruSutraReveal() {
        val node2 = world10Tree.dialogueNodes[1]
        assertEquals(2, node2.nodeId)
        assertEquals(RiveSpeaker.GURU, node2.speaker)
        assertTrue("Node 2 text must not be blank", node2.text.isNotBlank())
        assertTrue("Node 2 text must introduce Pūraṇāpūraṇābhyām", node2.text.contains("Pūraṇāpūraṇābhyām") || node2.text.contains("Completion"))
        assertEquals("guru_explain", node2.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node2.riveState.animation))
        assertFalse(node2.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode3IsGuruGuidedDemonstration() {
        val node3 = world10Tree.dialogueNodes[2]
        assertEquals(3, node3.nodeId)
        assertEquals(RiveSpeaker.GURU, node3.speaker)
        assertTrue("Node 3 text must not be blank", node3.text.isNotBlank())
        assertTrue("Node 3 text must explain adding 9 and perfect square (x + 3)²", node3.text.contains("9") && node3.text.contains("(x + 3)²"))
        assertEquals("guru_explain", node3.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node3.riveState.animation))
        assertFalse(node3.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode4IsGuruActiveHandshake() {
        val node4 = world10Tree.dialogueNodes[3]
        assertEquals(4, node4.nodeId)
        assertEquals(RiveSpeaker.GURU, node4.speaker)
        assertTrue("Node 4 text must not be blank", node4.text.isNotBlank())
        assertTrue("Node 4 text must ask for the completion term", node4.text.contains("completion term") || node4.text.contains("(6 ÷ 2)²") || node4.text.contains("(6 / 2)"))
        assertEquals("guru_magic_fx", node4.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node4.riveState.animation))
        assertTrue("Node 4 must require user tap", node4.interactiveHandshake.requiresUserTap)
        assertEquals("numpad_key_9", node4.interactiveHandshake.targetElementId)
        assertEquals("9", node4.interactiveHandshake.expectedAnswer)
    }

    @Test
    fun testGuruNeverWinks() {
        for (node in world10Tree.dialogueNodes) {
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
        for (node in world10Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} SSML must contain <speak>", node.audioCues.ssmlText.contains("<speak>"))
            assertTrue("Node ${node.nodeId} SSML must contain </speak>", node.audioCues.ssmlText.contains("</speak>"))
            assertTrue("Node ${node.nodeId} SFX must not be blank", node.audioCues.sfx.isNotBlank())
        }
    }

    @Test
    fun testAllNodesHaveNonBlankMathOverlays() {
        for (node in world10Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} expression must not be blank", node.mathOverlay.expression.isNotBlank())
            assertTrue("Node ${node.nodeId} highlight tokens must not be empty", node.mathOverlay.highlightTokens.isNotEmpty())
        }
    }

    // ── 4. Active Handshake & Expected Answer Authority ────────────────────────

    @Test
    fun testHandshakeAuthoritativeAnswerIsNine() {
        val handshake = world10Tree.dialogueNodes.last().interactiveHandshake
        assertEquals("Authoritative expected answer must be '9'", "9", handshake.expectedAnswer)
    }

    @Test
    fun testHandshakePromptDoesNotRevealExpectedAnswer() {
        val handshake = world10Tree.dialogueNodes.last().interactiveHandshake
        val prompt = handshake.promptText
        assertNotNull("Prompt must not be null", prompt)
        assertFalse(
            "Prompt '$prompt' must not leak '= 9' or 'is 9'",
            prompt!!.contains("= 9") || prompt.contains("is 9")
        )
    }

    @Test
    fun testHandshakeMathOverlayExpressionDoesNotRevealExpectedAnswer() {
        val finalNode = world10Tree.dialogueNodes.last()
        val expr = finalNode.mathOverlay.expression
        assertTrue("Overlay should indicate unknown: '$expr'", expr.contains("?") || expr.contains("[ ? ]"))
        assertFalse("Overlay must not leak '= 9': '$expr'", expr.contains("= 9"))
    }

    @Test
    fun testHandshakeChoicesIncludeExpectedAnswer() {
        val expected = world10Tree.dialogueNodes.last().interactiveHandshake.expectedAnswer
        val choices = generateHandshakeChoices(expected)
        assertTrue("Generated choices $choices must contain '$expected'", choices.contains(expected))
        assertEquals("Generated choices for '9' must be 5 items", 5, choices.size)
        assertEquals(listOf("7", "8", "9", "10", "11"), choices)
    }

    @Test
    fun testHandshakeFailsClosedOnNullOrBlankExpectedAnswer() {
        val nullAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_9",
            promptText = "Calculate (6 / 2)²",
            expectedAnswer = null
        )
        assertFalse(
            "Null expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(nullAnswerHandshake, "9")
        )

        val blankAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_9",
            promptText = "Calculate (6 / 2)²",
            expectedAnswer = "   "
        )
        assertFalse(
            "Blank expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(blankAnswerHandshake, "9")
        )
    }

    @Test
    fun testHandshakeEvaluatesStrictlyAgainstExpectedAnswer() {
        val handshake = world10Tree.dialogueNodes.last().interactiveHandshake

        assertTrue("Submitting '9' must pass", VedicMathValidator.evaluateHandshake(handshake, "9"))
        assertTrue("Submitting ' 9 ' with whitespace must pass", VedicMathValidator.evaluateHandshake(handshake, " 9 "))

        assertFalse("Submitting '3' must fail", VedicMathValidator.evaluateHandshake(handshake, "3"))
        assertFalse("Submitting '6' must fail", VedicMathValidator.evaluateHandshake(handshake, "6"))
        assertFalse("Submitting '8' must fail", VedicMathValidator.evaluateHandshake(handshake, "8"))
        assertFalse("Submitting '-2' must fail", VedicMathValidator.evaluateHandshake(handshake, "-2"))
        assertFalse("Submitting '-4' must fail", VedicMathValidator.evaluateHandshake(handshake, "-4"))
        assertFalse("Submitting empty string must fail", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("Submitting whitespace must fail", VedicMathValidator.evaluateHandshake(handshake, "   "))
    }

    // ── 5. Controller & Skip Behavior ──────────────────────────────────────────

    @Test
    fun testDialogueControllerAdvancesThroughFirstThreeNodes() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world10Tree, adapter)

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
        val controller = RiveDialogueController(world10Tree, adapter)

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
        val controller = RiveDialogueController(world10Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("3")
        assertFalse("Incorrect answer '3' must return false", success)
        assertFalse("Dialogue must not complete", controller.isCompleted.value)
        assertNotNull("Handshake error message must be set", controller.handshakeError.value)
        assertEquals("shishya_puzzled", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSubmittingCorrectAnswerCompletesDialogue() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world10Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("9")
        assertTrue("Correct answer '9' must return true", success)
        assertTrue("Dialogue must be marked completed", controller.isCompleted.value)
        assertEquals("shishya_celebrate", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSkipBehavior() {
        // Case 1: allowBypassHandshake = false (default student path)
        val adapter1 = DefaultRiveAdapter()
        val controller1 = RiveDialogueController(world10Tree, adapter1)

        repeat(3) { controller1.advanceNode() }
        assertEquals(3, controller1.currentNodeIndex.value)

        val skipped1 = controller1.skip(allowBypassHandshake = false)
        assertFalse("skip must return false when handshake is unresolved and bypass is disabled", skipped1)
        assertFalse("Dialogue must not be completed", controller1.isCompleted.value)

        // Case 2: allowBypassHandshake = true (debug / reviewer bypass)
        val adapter2 = DefaultRiveAdapter()
        val controller2 = RiveDialogueController(world10Tree, adapter2)

        repeat(3) { controller2.advanceNode() }
        val skipped2 = controller2.skip(allowBypassHandshake = true)
        assertTrue("skip must return true when bypass is allowed", skipped2)
        assertTrue("Dialogue must be completed when bypassed", controller2.isCompleted.value)
    }

    @Test
    fun testDialogueControllerSkipAfterHandshakeCompletion() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world10Tree, adapter)

        repeat(3) { controller.advanceNode() }
        val solved = controller.submitHandshake("9")
        assertTrue(solved)

        val skipped = controller.skip(allowBypassHandshake = false)
        assertTrue("skip must succeed once handshake is completed", skipped)
        assertTrue(controller.isCompleted.value)
    }

    // ── 6. Non-regression of Worlds 1–9 ────────────────────────────────────────

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
    fun testAllWorldsOneThroughTenHaveDistinctWorldNumbers() {
        for (i in 1..10) {
            val tree = repository.getDialogueForWorld(i)
            assertNotNull("World $i must exist", tree)
            assertEquals("World $i number mismatch", i, tree!!.worldNumber)
        }
    }
}
