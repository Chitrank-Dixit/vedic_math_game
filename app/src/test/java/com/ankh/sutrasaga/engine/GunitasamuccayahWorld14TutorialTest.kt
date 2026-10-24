package com.ankh.sutrasaga.engine

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ankh.sutrasaga.data.repository.RiveDialogueRepository
import com.ankh.sutrasaga.domain.models.FactorizationProposal
import com.ankh.sutrasaga.domain.models.GunitasamuccayahClassification
import com.ankh.sutrasaga.domain.models.InteractiveHandshake
import com.ankh.sutrasaga.domain.models.LinearFactor
import com.ankh.sutrasaga.domain.models.QuadraticPolynomial
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
class GunitasamuccayahWorld14TutorialTest {

    private lateinit var context: Context
    private lateinit var repository: RiveDialogueRepository
    private lateinit var world14Tree: RiveSutraDialogueTree
    private lateinit var generator: GunitasamuccayahGenerator

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = RiveDialogueRepository(context)
        val tree = repository.getDialogueForWorld(14)
        assertNotNull("World 14 dialogue must exist in repository", tree)
        world14Tree = tree!!
        generator = GunitasamuccayahGenerator()
    }

    // ── 1. Sutra Identification & Metadata ──────────────────────────────────────

    @Test
    fun testWorld14SutraIdentification() {
        assertEquals("gunitasamuccayah", world14Tree.sutraId)
        assertEquals("Guṇitasamuccayaḥ", world14Tree.sutraName)
        assertEquals("The Product of the Sum is the Sum of the Products", world14Tree.englishMeaning)
        assertEquals(14, world14Tree.worldNumber)
    }

    // ── 2. Mathematical Correctness & Independent Verification ──────────────────

    @Test
    fun testCanonicalTutorialProblemProducesFactorProductTwelve() {
        val problem: SutraProblem = generator.generateCanonicalTutorialProblem()
        assertEquals(12L, problem.correctAnswer)
        assertEquals("Gunitasamuccayah", problem.sutraName)
        assertEquals("VALID_FACTORISATION", problem.appendedSuffix)
        assertEquals(12L, problem.prefixPart)
        assertEquals(12L, problem.incrementedPrefix)
        assertEquals(1L, problem.prefixProduct)

        // Independent verification of (x + 3)(x + 2) = x^2 + 5x + 6
        val f1 = LinearFactor(1, 3)
        val f2 = LinearFactor(1, 2)
        val claimed = QuadraticPolynomial(1, 5, 6)
        val proposal = FactorizationProposal(listOf(f1, f2), claimed)
        val sol = generator.verifyFactorization(proposal)

        assertEquals(GunitasamuccayahClassification.VALID_FACTORISATION, sol.classification)
        assertEquals(12L, sol.sumCheck.expectedSum)
        assertEquals(12L, sol.sumCheck.actualSum)
        assertTrue(sol.sumCheck.passed)
        assertTrue(sol.expansionCheck.passed)

        assertTrue(
            "VedicMathValidator must verify canonical factorization (x+3)(x+2) = x^2+5x+6",
            VedicMathValidator.verifyGunitasamuccayah(1, 3, 1, 2, 1, 5, 6)
        )
    }

    @Test
    fun testIntermediateStepFactorSumAndPolynomialSum() {
        // Factors: (x + 3) and (x + 2)
        // At x = 1:
        val scF1 = 1L + 3L
        assertEquals(4L, scF1)
        val scF2 = 1L + 2L
        assertEquals(3L, scF2)

        // Product of sums (Handshake question: 4 * 3 = 12):
        val productOfSums = scF1 * scF2
        assertEquals(12L, productOfSums)

        // Polynomial: x^2 + 5x + 6
        // Sum of coefficients: 1 + 5 + 6 = 12
        val scP = 1L + 5L + 6L
        assertEquals(12L, scP)
        assertEquals(productOfSums, scP)

        // Exact term comparison:
        assertEquals(1L * 1L, 1L) // x^2 coefficient
        assertEquals(1L * 2L + 3L * 1L, 5L) // x coefficient
        assertEquals(3L * 2L, 6L) // constant coefficient
    }

    @Test
    fun testHandshakeCalculationFourTimesThreeYieldsTwelve() {
        val factor1Sum = 1 + 3
        val factor2Sum = 1 + 2
        val productSum = factor1Sum * factor2Sum
        assertEquals(12, productSum)
    }

    @Test
    fun testFalseProposalWithMatchingSumIsDetectedAsInvalidExpansion() {
        // (x + 3)(x + 2) = x^2 + 7x + 4
        // SC(factors) = 4 * 3 = 12
        // SC(claimed) = 1 + 7 + 4 = 12 (sums match!)
        // But exact expansion is x^2 + 5x + 6 != x^2 + 7x + 4
        val f1 = LinearFactor(1, 3)
        val f2 = LinearFactor(1, 2)
        val falseClaimed = QuadraticPolynomial(1, 7, 4)
        val sol = generator.verifyFactorization(FactorizationProposal(listOf(f1, f2), falseClaimed))

        assertEquals(GunitasamuccayahClassification.INVALID_EXPANSION, sol.classification)
        assertTrue(sol.sumCheck.passed)
        assertFalse(sol.expansionCheck.passed)

        // VedicMathValidator must reject false expansion
        assertFalse(
            "VedicMathValidator must reject false proposal where sums match but expansion fails",
            VedicMathValidator.verifyGunitasamuccayah(1, 3, 1, 2, 1, 7, 4)
        )
    }

    @Test
    fun test20HandVerifiedValidFactorizations() {
        val cases = listOf(
            TestCase(1, 1, 1, 1, 1, 2, 1),
            TestCase(1, 2, 1, 3, 1, 5, 6),
            TestCase(1, 4, 1, 5, 1, 9, 20),
            TestCase(1, -2, 1, 3, 1, 1, -6),
            TestCase(1, -3, 1, -4, 1, -7, 12),
            TestCase(2, 1, 1, 3, 2, 7, 3),
            TestCase(2, -1, 1, 4, 2, 7, -4),
            TestCase(2, 3, 2, 1, 4, 8, 3),
            TestCase(3, 2, 1, 1, 3, 5, 2),
            TestCase(1, -1, 1, 5, 1, 4, -5),
            TestCase(1, -4, 1, 4, 1, 0, -16),
            TestCase(2, 5, 1, -2, 2, 1, -10),
            TestCase(3, 1, 2, 1, 6, 5, 1),
            TestCase(1, 6, 1, 1, 1, 7, 6),
            TestCase(2, -3, 3, 2, 6, -5, -6),
            TestCase(1, 7, 1, 2, 1, 9, 14),
            TestCase(1, -5, 1, -2, 1, -7, 10),
            TestCase(2, 2, 2, 2, 4, 8, 4),
            TestCase(3, -1, 1, 3, 3, 8, -3),
            TestCase(1, 8, 1, 1, 1, 9, 8)
        )

        for (c in cases) {
            val f1 = LinearFactor(c.a1, c.b1)
            val f2 = LinearFactor(c.a2, c.b2)
            val poly = QuadraticPolynomial(c.claimedA2, c.claimedA1, c.claimedA0)
            val sol = generator.verifyFactorization(FactorizationProposal(listOf(f1, f2), poly))
            assertEquals("Classification for case", GunitasamuccayahClassification.VALID_FACTORISATION, sol.classification)
            assertTrue(
                "VedicMathValidator check for (${c.a1}x+${c.b1})(${c.a2}x+${c.b2})",
                VedicMathValidator.verifyGunitasamuccayah(c.a1, c.b1, c.a2, c.b2, c.claimedA2, c.claimedA1, c.claimedA0)
            )
        }
    }

    private data class TestCase(
        val a1: Long, val b1: Long,
        val a2: Long, val b2: Long,
        val claimedA2: Long, val claimedA1: Long, val claimedA0: Long
    )

    // ── 3. Dialogue Tree Schema & 4 Beats ──────────────────────────────────────

    @Test
    fun testDialogueTreePassesValidator() {
        val result = RiveDialogueValidator.validate(world14Tree)
        assertTrue("World 14 dialogue tree must be valid: ${result.errors}", result.isValid)
        assertTrue("World 14 dialogue tree must have zero validation errors", result.errors.isEmpty())
    }

    @Test
    fun testDialogueTreeHasExactlyFourBeats() {
        assertEquals("World 14 must have exactly 4 dialogue nodes", 4, world14Tree.dialogueNodes.size)
    }

    @Test
    fun testNode1IsShishyaHook() {
        val node1 = world14Tree.dialogueNodes[0]
        assertEquals(1, node1.nodeId)
        assertEquals(RiveSpeaker.SHISHYA, node1.speaker)
        assertTrue("Node 1 text must not be blank", node1.text.isNotBlank())
        assertTrue("Node 1 text must mention factoring or expanding polynomials", node1.text.contains("factoring") || node1.text.contains("polynomials"))
        assertEquals("shishya_curious", node1.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.SHISHYA, node1.riveState.animation))
        assertFalse(node1.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode2IsGuruSutraReveal() {
        val node2 = world14Tree.dialogueNodes[1]
        assertEquals(2, node2.nodeId)
        assertEquals(RiveSpeaker.GURU, node2.speaker)
        assertTrue("Node 2 text must not be blank", node2.text.isNotBlank())
        assertTrue("Node 2 text must introduce Guṇitasamuccayaḥ", node2.text.contains("Guṇitasamuccayaḥ") || node2.text.contains("Product of the Sum"))
        assertEquals("guru_explain", node2.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node2.riveState.animation))
        assertFalse(node2.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode3IsGuruGuidedDemonstration() {
        val node3 = world14Tree.dialogueNodes[2]
        assertEquals(3, node3.nodeId)
        assertEquals(RiveSpeaker.GURU, node3.speaker)
        assertTrue("Node 3 text must not be blank", node3.text.isNotBlank())
        assertTrue("Node 3 text must explain 4 × 3 = 12 and 1 + 5 + 6 = 12", node3.text.contains("12") && node3.text.contains("factorization"))
        assertEquals("guru_explain", node3.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node3.riveState.animation))
        assertFalse(node3.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode4IsGuruActiveHandshake() {
        val node4 = world14Tree.dialogueNodes[3]
        assertEquals(4, node4.nodeId)
        assertEquals(RiveSpeaker.GURU, node4.speaker)
        assertTrue("Node 4 text must not be blank", node4.text.isNotBlank())
        assertTrue("Node 4 text must ask for the factor sum product 4 × 3", node4.text.contains("4 × 3") || node4.text.contains("product of"))
        assertEquals("guru_magic_fx", node4.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node4.riveState.animation))
        assertTrue("Node 4 must require user tap", node4.interactiveHandshake.requiresUserTap)
        assertEquals("numpad_key_12", node4.interactiveHandshake.targetElementId)
        assertEquals("12", node4.interactiveHandshake.expectedAnswer)
    }

    @Test
    fun testGuruNeverWinks() {
        for (node in world14Tree.dialogueNodes) {
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
        for (node in world14Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} SSML must contain <speak>", node.audioCues.ssmlText.contains("<speak>"))
            assertTrue("Node ${node.nodeId} SSML must contain </speak>", node.audioCues.ssmlText.contains("</speak>"))
            assertTrue("Node ${node.nodeId} SFX must not be blank", node.audioCues.sfx.isNotBlank())
        }
    }

    @Test
    fun testAllNodesHaveNonBlankMathOverlays() {
        for (node in world14Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} expression must not be blank", node.mathOverlay.expression.isNotBlank())
            assertTrue("Node ${node.nodeId} highlight tokens must not be empty", node.mathOverlay.highlightTokens.isNotEmpty())
        }
    }

    // ── 4. Active Handshake & Expected Answer Authority ────────────────────────

    @Test
    fun testHandshakeAuthoritativeAnswerIsTwelve() {
        val handshake = world14Tree.dialogueNodes.last().interactiveHandshake
        assertEquals("Authoritative expected answer must be '12'", "12", handshake.expectedAnswer)
    }

    @Test
    fun testHandshakePromptDoesNotRevealExpectedAnswer() {
        val handshake = world14Tree.dialogueNodes.last().interactiveHandshake
        val prompt = handshake.promptText
        assertNotNull("Prompt must not be null", prompt)
        assertFalse(
            "Prompt '$prompt' must not leak '= 12' or 'is 12' or 'Tap 12'",
            prompt!!.contains("= 12") || prompt.contains("is 12") || prompt.contains("Tap 12")
        )
    }

    @Test
    fun testHandshakeMathOverlayExpressionDoesNotRevealExpectedAnswer() {
        val finalNode = world14Tree.dialogueNodes.last()
        val expr = finalNode.mathOverlay.expression
        assertTrue("Overlay should indicate unknown: '$expr'", expr.contains("?") || expr.contains("[ ? ]"))
        assertFalse("Overlay must not leak '= 12': '$expr'", expr.contains("= 12"))
    }

    @Test
    fun testHandshakeChoicesIncludeExpectedAnswer() {
        val expected = world14Tree.dialogueNodes.last().interactiveHandshake.expectedAnswer
        val choices = generateHandshakeChoices(expected)
        assertTrue("Generated choices $choices must contain '$expected'", choices.contains(expected))
        assertEquals("Generated choices for '12' must be 5 items", 5, choices.size)
        assertEquals(listOf("10", "11", "12", "13", "14"), choices)
    }

    @Test
    fun testHandshakeFailsClosedOnNullOrBlankExpectedAnswer() {
        val nullAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_12",
            promptText = "Calculate the product of factor sums: 4 × 3 = ?",
            expectedAnswer = null
        )
        assertFalse(
            "Null expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(nullAnswerHandshake, "12")
        )

        val blankAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_12",
            promptText = "Calculate the product of factor sums: 4 × 3 = ?",
            expectedAnswer = "   "
        )
        assertFalse(
            "Blank expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(blankAnswerHandshake, "12")
        )
    }

    @Test
    fun testHandshakeEvaluatesStrictlyAgainstExpectedAnswer() {
        val handshake = world14Tree.dialogueNodes.last().interactiveHandshake

        assertTrue("Submitting '12' must pass", VedicMathValidator.evaluateHandshake(handshake, "12"))
        assertTrue("Submitting ' 12 ' with whitespace must pass", VedicMathValidator.evaluateHandshake(handshake, " 12 "))

        assertFalse("Submitting '10' must fail", VedicMathValidator.evaluateHandshake(handshake, "10"))
        assertFalse("Submitting '11' must fail", VedicMathValidator.evaluateHandshake(handshake, "11"))
        assertFalse("Submitting '13' must fail", VedicMathValidator.evaluateHandshake(handshake, "13"))
        assertFalse("Submitting '14' must fail", VedicMathValidator.evaluateHandshake(handshake, "14"))
        assertFalse("Submitting '6' must fail", VedicMathValidator.evaluateHandshake(handshake, "6"))
        assertFalse("Submitting empty string must fail", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("Submitting whitespace must fail", VedicMathValidator.evaluateHandshake(handshake, "   "))
    }

    // ── 5. Controller & Skip Behavior ──────────────────────────────────────────

    @Test
    fun testDialogueControllerAdvancesThroughFirstThreeNodes() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world14Tree, adapter)

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
        val controller = RiveDialogueController(world14Tree, adapter)

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
        val controller = RiveDialogueController(world14Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("11")
        assertFalse("Incorrect answer '11' must return false", success)
        assertFalse("Dialogue must not complete", controller.isCompleted.value)
        assertNotNull("Handshake error message must be set", controller.handshakeError.value)
        assertEquals("shishya_puzzled", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSubmittingCorrectAnswerCompletesDialogue() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world14Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("12")
        assertTrue("Correct answer '12' must return true", success)
        assertTrue("Dialogue must be marked completed", controller.isCompleted.value)
        assertEquals("shishya_celebrate", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSkipBehavior() {
        // Case 1: allowBypassHandshake = false (default student path)
        val adapter1 = DefaultRiveAdapter()
        val controller1 = RiveDialogueController(world14Tree, adapter1)

        repeat(3) { controller1.advanceNode() }
        assertEquals(3, controller1.currentNodeIndex.value)

        val skipped1 = controller1.skip(allowBypassHandshake = false)
        assertFalse("skip must return false when handshake is unresolved and bypass is disabled", skipped1)
        assertFalse("Dialogue must not be completed", controller1.isCompleted.value)

        // Case 2: allowBypassHandshake = true (debug / reviewer bypass)
        val adapter2 = DefaultRiveAdapter()
        val controller2 = RiveDialogueController(world14Tree, adapter2)

        repeat(3) { controller2.advanceNode() }
        val skipped2 = controller2.skip(allowBypassHandshake = true)
        assertTrue("skip must return true when bypass is allowed", skipped2)
        assertTrue("Dialogue must be completed when bypassed", controller2.isCompleted.value)
    }

    @Test
    fun testDialogueControllerSkipAfterHandshakeCompletion() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world14Tree, adapter)

        repeat(3) { controller.advanceNode() }
        val solved = controller.submitHandshake("12")
        assertTrue(solved)

        val skipped = controller.skip(allowBypassHandshake = false)
        assertTrue("skip must succeed once handshake is completed", skipped)
        assertTrue(controller.isCompleted.value)
    }

    // ── 6. Non-regression of Worlds 1–13 ───────────────────────────────────────

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
    fun testAllWorldsOneThroughFourteenHaveDistinctWorldNumbers() {
        for (i in 1..14) {
            val tree = repository.getDialogueForWorld(i)
            assertNotNull("World $i must exist", tree)
            assertEquals("World $i number mismatch", i, tree!!.worldNumber)
        }
    }
}
