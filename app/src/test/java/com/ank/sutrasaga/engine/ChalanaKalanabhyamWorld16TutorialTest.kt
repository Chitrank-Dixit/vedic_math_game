package com.ank.sutrasaga.engine

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ank.sutrasaga.data.repository.RiveDialogueRepository
import com.ank.sutrasaga.domain.models.ChalanaClassification
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
class ChalanaKalanabhyamWorld16TutorialTest {

    private lateinit var context: Context
    private lateinit var repository: RiveDialogueRepository
    private lateinit var world16Tree: RiveSutraDialogueTree
    private lateinit var generator: ChalanaKalanabhyamGenerator

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = RiveDialogueRepository(context)
        val tree = repository.getDialogueForWorld(16)
        assertNotNull("World 16 dialogue must exist in repository", tree)
        world16Tree = tree!!
        generator = ChalanaKalanabhyamGenerator()
    }

    // ── 1. Sutra Identification & Metadata ──────────────────────────────────────

    @Test
    fun testWorld16SutraIdentification() {
        assertEquals("chalana_kalanabhyam", world16Tree.sutraId)
        assertEquals("Calana-Kalanābhyām", world16Tree.sutraName)
        assertTrue(
            "English meaning should describe Differential Calculus or Movement and Measure",
            world16Tree.englishMeaning.contains("Differential Calculus") ||
                    world16Tree.englishMeaning.contains("Movement and Measure")
        )
        assertEquals(16, world16Tree.worldNumber)
    }

    // ── 2. Mathematical Correctness & Independent Verification ──────────────────

    @Test
    fun testCanonicalTutorialProblemProducesRootsSevenAndOne() {
        val problem: SutraProblem = generator.generateCanonicalTutorialProblem()
        assertEquals(7L, problem.correctAnswer)
        assertEquals(1L, problem.expectedSecondaryAnswer)
        assertEquals("Chalana-Kalanabhyam", problem.sutraName)
        assertEquals(1L, problem.prefixPart)
        assertEquals(-8L, problem.incrementedPrefix)
        assertEquals(7L, problem.prefixProduct)
        assertEquals("36", problem.appendedSuffix)

        // Independent verification of x^2 - 8x + 7 = 0 via solveQuadratic
        val sol = generator.solveQuadratic(1, -8, 7)
        assertEquals(ChalanaClassification.TWO_REAL_ROOTS, sol.classification)
        assertEquals(2L, sol.derivative.coef)
        assertEquals(-8L, sol.derivative.const)
        assertEquals(36L, sol.discriminant.value)
        assertTrue(sol.discriminant.isPerfectSquare)
        assertEquals(6L, sol.discriminant.squareRoot)
        assertEquals(listOf(7L, 1L), sol.roots)

        assertTrue(
            "VedicMathValidator must verify canonical solution x^2 - 8x + 7 = 0",
            VedicMathValidator.verifyChalanaKalanabhyam(1L, -8L, 7L, 36L, 6L, 7L, 1L)
        )
    }

    @Test
    fun testDerivativeAndDiscriminantIndependentCalculations() {
        val a = 1L
        val b = -8L
        val c = 7L

        // D = B^2 - 4AC
        val d = b * b - 4 * a * c
        assertEquals(36L, d)
        val sqrtD = 6L
        assertEquals(d, sqrtD * sqrtD)

        // Derivative coefficients: f'(x) = 2Ax + B = 2x - 8
        val derivCoef = 2 * a
        val derivConst = b
        assertEquals(2L, derivCoef)
        assertEquals(-8L, derivConst)

        // Plus branch: 2x - 8 = +6 ⟹ 2x = 14 ⟹ x = 7 (Handshake question)
        val r1 = (-b + sqrtD) / (2 * a)
        assertEquals(7L, r1)

        // Minus branch: 2x - 8 = -6 ⟹ 2x = 2 ⟹ x = 1 (Guided demo)
        val r2 = (-b - sqrtD) / (2 * a)
        assertEquals(1L, r2)

        // Invariant f'(r)^2 == D
        val fPrimeR1 = derivCoef * r1 + derivConst // 2(7) - 8 = 6
        assertEquals(sqrtD, fPrimeR1)
        assertEquals(d, fPrimeR1 * fPrimeR1)

        val fPrimeR2 = derivCoef * r2 + derivConst // 2(1) - 8 = -6
        assertEquals(-sqrtD, fPrimeR2)
        assertEquals(d, fPrimeR2 * fPrimeR2)

        // Quadratic evaluation f(r) == 0
        assertEquals(0L, a * r1 * r1 + b * r1 + c)
        assertEquals(0L, a * r2 * r2 + b * r2 + c)
    }

    @Test
    fun testHandshakeCalculationFourteenDividedByTwoYieldsSeven() {
        val branchRhs = 14L
        val root = branchRhs / 2L
        assertEquals(7L, root)
    }

    @Test
    fun testRepeatedRootCaseDiscriminantZero() {
        // x^2 - 6x + 9 = 0
        val sol = generator.solveQuadratic(1, -6, 9)
        assertEquals(ChalanaClassification.REPEATED_REAL_ROOT, sol.classification)
        assertEquals(0L, sol.discriminant.value)
        assertEquals(listOf(3L, 3L), sol.roots)
    }

    @Test
    fun testNegativeDiscriminantNoRealRoots() {
        // x^2 + 2x + 5 = 0
        val sol = generator.solveQuadratic(1, 2, 5)
        assertEquals(ChalanaClassification.NO_REAL_ROOTS, sol.classification)
        assertEquals(-16L, sol.discriminant.value)
        assertTrue(sol.roots.isEmpty())
    }

    @Test
    fun testNotQuadraticAIsZero() {
        val sol = generator.solveQuadratic(0, 4, 8)
        assertEquals(ChalanaClassification.NOT_QUADRATIC, sol.classification)
        assertTrue(sol.roots.isEmpty())
    }

    @Test
    fun test20HandVerifiedQuadraticCases() {
        val cases = listOf(
            TestCase(1L, -8L, 7L, 36L, 6L, 7L, 1L),
            TestCase(1L, -5L, 6L, 1L, 1L, 3L, 2L),
            TestCase(1L, -7L, 12L, 1L, 1L, 4L, 3L),
            TestCase(1L, 7L, 10L, 9L, 3L, -2L, -5L),
            TestCase(1L, 1L, -6L, 25L, 5L, 2L, -3L),
            TestCase(1L, -9L, 20L, 1L, 1L, 5L, 4L),
            TestCase(1L, 0L, -16L, 64L, 8L, 4L, -4L),
            TestCase(1L, 0L, -25L, 100L, 10L, 5L, -5L),
            TestCase(1L, -8L, 15L, 4L, 2L, 5L, 3L),
            TestCase(1L, -1L, -20L, 81L, 9L, 5L, -4L),
            TestCase(2L, -10L, 12L, 4L, 2L, 3L, 2L),
            TestCase(2L, 4L, -30L, 256L, 16L, 3L, -5L),
            TestCase(3L, -15L, 18L, 9L, 3L, 3L, 2L),
            TestCase(2L, -14L, 24L, 4L, 2L, 4L, 3L),
            TestCase(3L, 3L, -18L, 225L, 15L, 2L, -3L),
            TestCase(2L, -16L, 30L, 16L, 4L, 5L, 3L),
            TestCase(3L, -27L, 60L, 9L, 3L, 5L, 4L),
            TestCase(2L, -8L, -24L, 256L, 16L, 6L, -2L),
            TestCase(3L, 6L, -24L, 324L, 18L, 2L, -4L),
            TestCase(2L, -2L, -12L, 100L, 10L, 3L, -2L)
        )

        for (c in cases) {
            val sol = generator.solveQuadratic(c.a, c.b, c.c)
            assertEquals("Classification for ${c.a}x^2 + ${c.b}x + ${c.c}", ChalanaClassification.TWO_REAL_ROOTS, sol.classification)
            assertTrue(
                "VedicMathValidator check for ${c.a}x^2 + ${c.b}x + ${c.c}",
                VedicMathValidator.verifyChalanaKalanabhyam(c.a, c.b, c.c, c.d, c.sqrtD, c.r1, c.r2)
            )
        }
    }

    private data class TestCase(
        val a: Long, val b: Long, val c: Long,
        val d: Long, val sqrtD: Long,
        val r1: Long, val r2: Long
    )

    // ── 3. Dialogue Tree Schema & 4 Beats ──────────────────────────────────────

    @Test
    fun testDialogueTreePassesValidator() {
        val result = RiveDialogueValidator.validate(world16Tree)
        assertTrue("World 16 dialogue tree must be valid: ${result.errors}", result.isValid)
        assertTrue("World 16 dialogue tree must have zero validation errors", result.errors.isEmpty())
    }

    @Test
    fun testDialogueTreeHasExactlyFourBeats() {
        assertEquals("World 16 must have exactly 4 dialogue nodes", 4, world16Tree.dialogueNodes.size)
    }

    @Test
    fun testNode1IsShishyaHook() {
        val node1 = world16Tree.dialogueNodes[0]
        assertEquals(1, node1.nodeId)
        assertEquals(RiveSpeaker.SHISHYA, node1.speaker)
        assertTrue("Node 1 text must not be blank", node1.text.isNotBlank())
        assertTrue("Node 1 text must mention quadratics or x² - 8x + 7", node1.text.contains("x² - 8x + 7") || node1.text.contains("quadratic"))
        assertEquals("shishya_curious", node1.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.SHISHYA, node1.riveState.animation))
        assertFalse(node1.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode2IsGuruSutraReveal() {
        val node2 = world16Tree.dialogueNodes[1]
        assertEquals(2, node2.nodeId)
        assertEquals(RiveSpeaker.GURU, node2.speaker)
        assertTrue("Node 2 text must not be blank", node2.text.isNotBlank())
        assertTrue("Node 2 text must introduce Calana-Kalanābhyām", node2.text.contains("Calana-Kalanābhyām") || node2.text.contains("Differential Calculus"))
        assertEquals("guru_explain", node2.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node2.riveState.animation))
        assertFalse(node2.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode3IsGuruGuidedDemonstration() {
        val node3 = world16Tree.dialogueNodes[2]
        assertEquals(3, node3.nodeId)
        assertEquals(RiveSpeaker.GURU, node3.speaker)
        assertTrue("Node 3 text must not be blank", node3.text.isNotBlank())
        assertTrue("Node 3 text must demonstrate f'(x) = 2x - 8, D = 36, and root x = 1", node3.text.contains("2x - 8") && node3.text.contains("x = 1"))
        assertEquals("guru_explain", node3.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node3.riveState.animation))
        assertFalse(node3.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode4IsGuruActiveHandshake() {
        val node4 = world16Tree.dialogueNodes[3]
        assertEquals(4, node4.nodeId)
        assertEquals(RiveSpeaker.GURU, node4.speaker)
        assertTrue("Node 4 text must not be blank", node4.text.isNotBlank())
        assertTrue("Node 4 text must ask for the root x from 2x = 14", node4.text.contains("2x = 14") || node4.text.contains("root x"))
        assertEquals("guru_magic_fx", node4.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node4.riveState.animation))
        assertTrue("Node 4 must require user tap", node4.interactiveHandshake.requiresUserTap)
        assertEquals("numpad_key_7", node4.interactiveHandshake.targetElementId)
        assertEquals("7", node4.interactiveHandshake.expectedAnswer)
    }

    @Test
    fun testGuruNeverWinks() {
        for (node in world16Tree.dialogueNodes) {
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
        for (node in world16Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} SSML must contain <speak>", node.audioCues.ssmlText.contains("<speak>"))
            assertTrue("Node ${node.nodeId} SSML must contain </speak>", node.audioCues.ssmlText.contains("</speak>"))
            assertTrue("Node ${node.nodeId} SFX must not be blank", node.audioCues.sfx.isNotBlank())
        }
    }

    @Test
    fun testAllNodesHaveNonBlankMathOverlays() {
        for (node in world16Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} expression must not be blank", node.mathOverlay.expression.isNotBlank())
            assertTrue("Node ${node.nodeId} highlight tokens must not be empty", node.mathOverlay.highlightTokens.isNotEmpty())
        }
    }

    // ── 4. Active Handshake & Expected Answer Authority ────────────────────────

    @Test
    fun testHandshakeAuthoritativeAnswerIsSeven() {
        val handshake = world16Tree.dialogueNodes.last().interactiveHandshake
        assertEquals("Authoritative expected answer must be '7'", "7", handshake.expectedAnswer)
    }

    @Test
    fun testHandshakePromptDoesNotRevealExpectedAnswer() {
        val handshake = world16Tree.dialogueNodes.last().interactiveHandshake
        val prompt = handshake.promptText
        assertNotNull("Prompt must not be null", prompt)
        assertFalse(
            "Prompt '$prompt' must not leak '=7' or 'is 7' or 'Tap 7'",
            prompt!!.contains("=7") || prompt.contains("is 7") || prompt.contains("Tap 7")
        )
    }

    @Test
    fun testHandshakeMathOverlayExpressionDoesNotRevealExpectedAnswer() {
        val finalNode = world16Tree.dialogueNodes.last()
        val expr = finalNode.mathOverlay.expression
        assertTrue("Overlay should indicate unknown: '$expr'", expr.contains("?") || expr.contains("[ ? ]"))
        assertFalse("Overlay must not leak '=7': '$expr'", expr.contains("=7"))
    }

    @Test
    fun testHandshakeChoicesIncludeExpectedAnswer() {
        val expected = world16Tree.dialogueNodes.last().interactiveHandshake.expectedAnswer
        val choices = generateHandshakeChoices(expected)
        assertTrue("Generated choices $choices must contain '$expected'", choices.contains(expected))
        assertEquals("Generated choices for '7' must be 5 items", 5, choices.size)
        assertEquals(listOf("5", "6", "7", "8", "9"), choices)
    }

    @Test
    fun testHandshakeFailsClosedOnNullOrBlankExpectedAnswer() {
        val nullAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_7",
            promptText = "Solve for root x: 2x = 14 ⟹ x = ?",
            expectedAnswer = null
        )
        assertFalse(
            "Null expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(nullAnswerHandshake, "7")
        )

        val blankAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_7",
            promptText = "Solve for root x: 2x = 14 ⟹ x = ?",
            expectedAnswer = "   "
        )
        assertFalse(
            "Blank expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(blankAnswerHandshake, "7")
        )
    }

    @Test
    fun testHandshakeEvaluatesStrictlyAgainstExpectedAnswer() {
        val handshake = world16Tree.dialogueNodes.last().interactiveHandshake

        assertTrue("Submitting '7' must pass", VedicMathValidator.evaluateHandshake(handshake, "7"))
        assertTrue("Submitting ' 7 ' with whitespace must pass", VedicMathValidator.evaluateHandshake(handshake, " 7 "))

        assertFalse("Submitting '5' must fail", VedicMathValidator.evaluateHandshake(handshake, "5"))
        assertFalse("Submitting '6' must fail", VedicMathValidator.evaluateHandshake(handshake, "6"))
        assertFalse("Submitting '8' must fail", VedicMathValidator.evaluateHandshake(handshake, "8"))
        assertFalse("Submitting '9' must fail", VedicMathValidator.evaluateHandshake(handshake, "9"))
        assertFalse("Submitting '-7' must fail", VedicMathValidator.evaluateHandshake(handshake, "-7"))
        assertFalse("Submitting '14' must fail", VedicMathValidator.evaluateHandshake(handshake, "14"))
        assertFalse("Submitting '1' must fail", VedicMathValidator.evaluateHandshake(handshake, "1"))
        assertFalse("Submitting empty string must fail", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("Submitting whitespace must fail", VedicMathValidator.evaluateHandshake(handshake, "   "))
    }

    // ── 5. Controller & Skip Behavior ──────────────────────────────────────────

    @Test
    fun testDialogueControllerAdvancesThroughFirstThreeNodes() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world16Tree, adapter)

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
        val controller = RiveDialogueController(world16Tree, adapter)

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
        val controller = RiveDialogueController(world16Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("5")
        assertFalse("Incorrect answer '5' must return false", success)
        assertFalse("Dialogue must not complete", controller.isCompleted.value)
        assertNotNull("Handshake error message must be set", controller.handshakeError.value)
        assertEquals("shishya_puzzled", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSubmittingCorrectAnswerCompletesDialogue() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world16Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("7")
        assertTrue("Correct answer '7' must return true", success)
        assertTrue("Dialogue must be marked completed", controller.isCompleted.value)
        assertEquals("shishya_celebrate", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSkipBehavior() {
        // Case 1: allowBypassHandshake = false (default student path)
        val adapter1 = DefaultRiveAdapter()
        val controller1 = RiveDialogueController(world16Tree, adapter1)

        repeat(3) { controller1.advanceNode() }
        assertEquals(3, controller1.currentNodeIndex.value)

        val skipped1 = controller1.skip(allowBypassHandshake = false)
        assertFalse("skip must return false when handshake is unresolved and bypass is disabled", skipped1)
        assertFalse("Dialogue must not be completed", controller1.isCompleted.value)

        // Case 2: allowBypassHandshake = true (debug / reviewer bypass)
        val adapter2 = DefaultRiveAdapter()
        val controller2 = RiveDialogueController(world16Tree, adapter2)

        repeat(3) { controller2.advanceNode() }
        val skipped2 = controller2.skip(allowBypassHandshake = true)
        assertTrue("skip must return true when bypass is allowed", skipped2)
        assertTrue("Dialogue must be completed when bypassed", controller2.isCompleted.value)
    }

    @Test
    fun testDialogueControllerSkipAfterHandshakeCompletion() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world16Tree, adapter)

        repeat(3) { controller.advanceNode() }
        val solved = controller.submitHandshake("7")
        assertTrue(solved)

        val skipped = controller.skip(allowBypassHandshake = false)
        assertTrue("skip must succeed once handshake is completed", skipped)
        assertTrue(controller.isCompleted.value)
    }

    // ── 6. Non-regression of Worlds 1–15 & Zero Answer Collision ────────────────

    @Test
    fun testWorldsOneThroughFifteenStillPassValidation() {
        for (w in 1..15) {
            val tree = repository.getDialogueForWorld(w)
            assertNotNull("World $w dialogue must exist", tree)
            val result = RiveDialogueValidator.validate(tree!!)
            assertTrue("World $w must pass validation: ${result.errors}", result.isValid)
        }
    }

    @Test
    fun testAllWorldsOneThroughSixteenHaveDistinctWorldNumbers() {
        for (i in 1..16) {
            val tree = repository.getDialogueForWorld(i)
            assertNotNull("World $i must exist", tree)
            assertEquals("World $i number mismatch", i, tree!!.worldNumber)
        }
    }

    @Test
    fun testAllWorldsOneThroughSixteenHaveUniqueHandshakeExpectedAnswers() {
        val answers = mutableMapOf<String, Int>()
        for (i in 1..16) {
            val tree = repository.getDialogueForWorld(i)
            assertNotNull("World $i must exist", tree)
            val handshakeNode = tree!!.dialogueNodes.find { it.interactiveHandshake.requiresUserTap }
            assertNotNull("World $i must have an active handshake node", handshakeNode)
            val ans = handshakeNode!!.interactiveHandshake.expectedAnswer
            assertNotNull("World $i must have a non-null expectedAnswer", ans)
            assertTrue("World $i expectedAnswer must not be blank", ans!!.isNotBlank())

            val previousWorld = answers[ans]
            assertTrue(
                "Collision detected! World $i and World $previousWorld share expected answer '$ans'",
                previousWorld == null
            )
            answers[ans] = i
        }
        assertEquals("Must have 16 distinct handshake expected answers for all 16 worlds", 16, answers.size)
    }
}
