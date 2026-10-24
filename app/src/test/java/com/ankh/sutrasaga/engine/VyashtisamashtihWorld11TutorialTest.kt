package com.ankh.sutrasaga.engine

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ankh.sutrasaga.data.repository.RiveDialogueRepository
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.InteractiveHandshake
import com.ankh.sutrasaga.domain.models.RiveAnimationConfig
import com.ankh.sutrasaga.domain.models.RiveSpeaker
import com.ankh.sutrasaga.domain.models.RiveSutraDialogueTree
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.models.VyashtisamashtihClassification
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
class VyashtisamashtihWorld11TutorialTest {

    private lateinit var context: Context
    private lateinit var repository: RiveDialogueRepository
    private lateinit var world11Tree: RiveSutraDialogueTree
    private lateinit var generator: VyashtisamashtihGenerator

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = RiveDialogueRepository(context)
        val tree = repository.getDialogueForWorld(11)
        assertNotNull("World 11 dialogue must exist in repository", tree)
        world11Tree = tree!!
        generator = VyashtisamashtihGenerator()
    }

    // ── 1. Sutra Identification & Metadata ──────────────────────────────────────

    @Test
    fun testWorld11SutraIdentification() {
        assertEquals("vyashtisamashtih", world11Tree.sutraId)
        assertEquals("Vyaṣṭisamaṣṭiḥ", world11Tree.sutraName)
        assertEquals("Specific and General / Part and Whole", world11Tree.englishMeaning)
        assertEquals(11, world11Tree.worldNumber)
    }

    // ── 2. Mathematical Correctness & Independent Verification ──────────────────

    @Test
    fun testCanonicalTutorialProblemProducesProduct3596() {
        val problem: SutraProblem = generator.generateCanonicalTutorialProblem()
        assertEquals(3596L, problem.correctAnswer)
        assertEquals("Vyashtisamashtih", problem.sutraName)

        // Verify independent solver logic for 58 x 62
        val p = Fraction(58L, 1L)
        val q = Fraction(62L, 1L)
        val sol = generator.solveSymmetricProduct(p, q)
        assertEquals(VyashtisamashtihClassification.SUPPORTED_INTEGER_PATH, sol.classification)
        assertEquals(Fraction(60L, 1L), sol.parts.wholeAverage.reduced)
        assertEquals(Fraction(2L, 1L), sol.parts.positiveDeviation.reduced)
        assertEquals(Fraction(3600L, 1L), sol.wholeSquare.reduced)
        assertEquals(Fraction(4L, 1L), sol.deviationSquare.reduced)
        assertEquals(Fraction(3596L, 1L), sol.product.reduced)

        // Direct arithmetic verification
        assertEquals(58L * 62L, sol.product.numerator)

        // Verify with VedicMathValidator
        assertTrue(
            "VedicMathValidator must verify canonical symmetric product",
            VedicMathValidator.verifyVyashtisamashtih(
                p = 58L,
                q = 62L,
                expectedAverage = 60L,
                expectedDeviation = 2L,
                expectedProduct = 3596L
            )
        )
    }

    @Test
    fun testIntermediateStepAverageCalculation() {
        // Factors: 58 and 62
        val p = 58L
        val q = 62L

        // Whole average: (58 + 62) / 2 = 60
        val sum = p + q
        assertEquals(120L, sum)
        val avg = sum / 2L
        assertEquals(60L, avg)

        // Deviation: (62 - 58) / 2 = 2
        val diff = q - p
        assertEquals(4L, diff)
        val dev = diff / 2L
        assertEquals(2L, dev)

        // Difference of squares: 60^2 - 2^2 = 3600 - 4 = 3596
        val wholeSq = avg * avg
        val devSq = dev * dev
        assertEquals(3600L, wholeSq)
        assertEquals(4L, devSq)
        val prod = wholeSq - devSq
        assertEquals(3596L, prod)
        assertEquals(p * q, prod)
    }

    @Test
    fun testHandshakeCalculationFiftyEightPlusSixtyTwoDividedByTwoYieldsSixty() {
        val p = 58L
        val q = 62L
        val midpoint = (p + q) / 2L
        assertEquals(60L, midpoint)
    }

    @Test
    fun test20HandVerifiedIntegerCases() {
        val cases = listOf(
            TestCase(48L, 52L, 50L, 2L, 2496L),
            TestCase(94L, 106L, 100L, 6L, 9964L),
            TestCase(18L, 22L, 20L, 2L, 396L),
            TestCase(35L, 45L, 40L, 5L, 1575L),
            TestCase(67L, 73L, 70L, 3L, 4891L),

            TestCase(89L, 91L, 90L, 1L, 8099L),
            TestCase(14L, 16L, 15L, 1L, 224L),
            TestCase(26L, 34L, 30L, 4L, 884L),
            TestCase(43L, 57L, 50L, 7L, 2451L),
            TestCase(78L, 82L, 80L, 2L, 6396L),

            TestCase(11L, 19L, 15L, 4L, 209L),
            TestCase(24L, 36L, 30L, 6L, 864L),
            TestCase(37L, 43L, 40L, 3L, 1591L),
            TestCase(55L, 65L, 60L, 5L, 3575L),
            TestCase(72L, 88L, 80L, 8L, 6336L),

            TestCase(91L, 109L, 100L, 9L, 9919L),
            TestCase(13L, 17L, 15L, 2L, 221L),
            TestCase(22L, 28L, 25L, 3L, 616L),
            TestCase(46L, 54L, 50L, 4L, 2484L),
            TestCase(64L, 76L, 70L, 6L, 4864L)
        )

        for (c in cases) {
            val sol = generator.solveSymmetricProduct(Fraction(c.p, 1L), Fraction(c.q, 1L))
            assertEquals("Classification for ${c.p} x ${c.q}", VyashtisamashtihClassification.SUPPORTED_INTEGER_PATH, sol.classification)
            assertEquals("Average for ${c.p} x ${c.q}", c.avg, sol.parts.wholeAverage.numerator)
            assertEquals("Deviation for ${c.p} x ${c.q}", c.dev, sol.parts.positiveDeviation.numerator)
            assertEquals("Product for ${c.p} x ${c.q}", c.prod, sol.product.numerator)
            assertTrue(
                "VedicMathValidator check for ${c.p} x ${c.q}",
                VedicMathValidator.verifyVyashtisamashtih(c.p, c.q, c.avg, c.dev, c.prod)
            )
        }
    }

    private data class TestCase(
        val p: Long,
        val q: Long,
        val avg: Long,
        val dev: Long,
        val prod: Long
    )

    // ── 3. Dialogue Tree Schema & 4 Beats ──────────────────────────────────────

    @Test
    fun testDialogueTreePassesValidator() {
        val result = RiveDialogueValidator.validate(world11Tree)
        assertTrue("World 11 dialogue tree must be valid: ${result.errors}", result.isValid)
        assertTrue("World 11 dialogue tree must have zero validation errors", result.errors.isEmpty())
    }

    @Test
    fun testDialogueTreeHasExactlyFourBeats() {
        assertEquals("World 11 must have exactly 4 dialogue nodes", 4, world11Tree.dialogueNodes.size)
    }

    @Test
    fun testNode1IsShishyaHook() {
        val node1 = world11Tree.dialogueNodes[0]
        assertEquals(1, node1.nodeId)
        assertEquals(RiveSpeaker.SHISHYA, node1.speaker)
        assertTrue("Node 1 text must not be blank", node1.text.isNotBlank())
        assertTrue("Node 1 text must mention 58 × 62 or symmetry", node1.text.contains("58 × 62") || node1.text.contains("symmetry"))
        assertEquals("shishya_curious", node1.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.SHISHYA, node1.riveState.animation))
        assertFalse(node1.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode2IsGuruSutraReveal() {
        val node2 = world11Tree.dialogueNodes[1]
        assertEquals(2, node2.nodeId)
        assertEquals(RiveSpeaker.GURU, node2.speaker)
        assertTrue("Node 2 text must not be blank", node2.text.isNotBlank())
        assertTrue("Node 2 text must introduce Vyaṣṭisamaṣṭiḥ", node2.text.contains("Vyaṣṭisamaṣṭiḥ") || node2.text.contains("Part and Whole"))
        assertEquals("guru_explain", node2.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node2.riveState.animation))
        assertFalse(node2.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode3IsGuruGuidedDemonstration() {
        val node3 = world11Tree.dialogueNodes[2]
        assertEquals(3, node3.nodeId)
        assertEquals(RiveSpeaker.GURU, node3.speaker)
        assertTrue("Node 3 text must not be blank", node3.text.isNotBlank())
        assertTrue("Node 3 text must explain difference of squares 60² - 2² = 3596", node3.text.contains("3600") && node3.text.contains("3596"))
        assertEquals("guru_explain", node3.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node3.riveState.animation))
        assertFalse(node3.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode4IsGuruActiveHandshake() {
        val node4 = world11Tree.dialogueNodes[3]
        assertEquals(4, node4.nodeId)
        assertEquals(RiveSpeaker.GURU, node4.speaker)
        assertTrue("Node 4 text must not be blank", node4.text.isNotBlank())
        assertTrue("Node 4 text must ask for the midpoint average", node4.text.contains("average") || node4.text.contains("midpoint") || node4.text.contains("(58 + 62)"))
        assertEquals("guru_magic_fx", node4.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node4.riveState.animation))
        assertTrue("Node 4 must require user tap", node4.interactiveHandshake.requiresUserTap)
        assertEquals("numpad_key_60", node4.interactiveHandshake.targetElementId)
        assertEquals("60", node4.interactiveHandshake.expectedAnswer)
    }

    @Test
    fun testGuruNeverWinks() {
        for (node in world11Tree.dialogueNodes) {
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
        for (node in world11Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} SSML must contain <speak>", node.audioCues.ssmlText.contains("<speak>"))
            assertTrue("Node ${node.nodeId} SSML must contain </speak>", node.audioCues.ssmlText.contains("</speak>"))
            assertTrue("Node ${node.nodeId} SFX must not be blank", node.audioCues.sfx.isNotBlank())
        }
    }

    @Test
    fun testAllNodesHaveNonBlankMathOverlays() {
        for (node in world11Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} expression must not be blank", node.mathOverlay.expression.isNotBlank())
            assertTrue("Node ${node.nodeId} highlight tokens must not be empty", node.mathOverlay.highlightTokens.isNotEmpty())
        }
    }

    // ── 4. Active Handshake & Expected Answer Authority ────────────────────────

    @Test
    fun testHandshakeAuthoritativeAnswerIsSixty() {
        val handshake = world11Tree.dialogueNodes.last().interactiveHandshake
        assertEquals("Authoritative expected answer must be '60'", "60", handshake.expectedAnswer)
    }

    @Test
    fun testHandshakePromptDoesNotRevealExpectedAnswer() {
        val handshake = world11Tree.dialogueNodes.last().interactiveHandshake
        val prompt = handshake.promptText
        assertNotNull("Prompt must not be null", prompt)
        assertFalse(
            "Prompt '$prompt' must not leak '= 60' or 'is 60'",
            prompt!!.contains("= 60") || prompt.contains("is 60")
        )
    }

    @Test
    fun testHandshakeMathOverlayExpressionDoesNotRevealExpectedAnswer() {
        val finalNode = world11Tree.dialogueNodes.last()
        val expr = finalNode.mathOverlay.expression
        assertTrue("Overlay should indicate unknown: '$expr'", expr.contains("?") || expr.contains("[ ? ]"))
        assertFalse("Overlay must not leak '= 60': '$expr'", expr.contains("= 60"))
    }

    @Test
    fun testHandshakeChoicesIncludeExpectedAnswer() {
        val expected = world11Tree.dialogueNodes.last().interactiveHandshake.expectedAnswer
        val choices = generateHandshakeChoices(expected)
        assertTrue("Generated choices $choices must contain '$expected'", choices.contains(expected))
        assertEquals("Generated choices for '60' must be 5 items", 5, choices.size)
        assertEquals(listOf("58", "59", "60", "61", "62"), choices)
    }

    @Test
    fun testHandshakeFailsClosedOnNullOrBlankExpectedAnswer() {
        val nullAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_60",
            promptText = "Find the midpoint average: (58 + 62) / 2",
            expectedAnswer = null
        )
        assertFalse(
            "Null expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(nullAnswerHandshake, "60")
        )

        val blankAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_60",
            promptText = "Find the midpoint average: (58 + 62) / 2",
            expectedAnswer = "   "
        )
        assertFalse(
            "Blank expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(blankAnswerHandshake, "60")
        )
    }

    @Test
    fun testHandshakeEvaluatesStrictlyAgainstExpectedAnswer() {
        val handshake = world11Tree.dialogueNodes.last().interactiveHandshake

        assertTrue("Submitting '60' must pass", VedicMathValidator.evaluateHandshake(handshake, "60"))
        assertTrue("Submitting ' 60 ' with whitespace must pass", VedicMathValidator.evaluateHandshake(handshake, " 60 "))

        assertFalse("Submitting '58' must fail", VedicMathValidator.evaluateHandshake(handshake, "58"))
        assertFalse("Submitting '59' must fail", VedicMathValidator.evaluateHandshake(handshake, "59"))
        assertFalse("Submitting '61' must fail", VedicMathValidator.evaluateHandshake(handshake, "61"))
        assertFalse("Submitting '62' must fail", VedicMathValidator.evaluateHandshake(handshake, "62"))
        assertFalse("Submitting '2' must fail", VedicMathValidator.evaluateHandshake(handshake, "2"))
        assertFalse("Submitting '3596' must fail", VedicMathValidator.evaluateHandshake(handshake, "3596"))
        assertFalse("Submitting empty string must fail", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("Submitting whitespace must fail", VedicMathValidator.evaluateHandshake(handshake, "   "))
    }

    // ── 5. Controller & Skip Behavior ──────────────────────────────────────────

    @Test
    fun testDialogueControllerAdvancesThroughFirstThreeNodes() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world11Tree, adapter)

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
        val controller = RiveDialogueController(world11Tree, adapter)

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
        val controller = RiveDialogueController(world11Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("58")
        assertFalse("Incorrect answer '58' must return false", success)
        assertFalse("Dialogue must not complete", controller.isCompleted.value)
        assertNotNull("Handshake error message must be set", controller.handshakeError.value)
        assertEquals("shishya_puzzled", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSubmittingCorrectAnswerCompletesDialogue() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world11Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("60")
        assertTrue("Correct answer '60' must return true", success)
        assertTrue("Dialogue must be marked completed", controller.isCompleted.value)
        assertEquals("shishya_celebrate", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSkipBehavior() {
        // Case 1: allowBypassHandshake = false (default student path)
        val adapter1 = DefaultRiveAdapter()
        val controller1 = RiveDialogueController(world11Tree, adapter1)

        repeat(3) { controller1.advanceNode() }
        assertEquals(3, controller1.currentNodeIndex.value)

        val skipped1 = controller1.skip(allowBypassHandshake = false)
        assertFalse("skip must return false when handshake is unresolved and bypass is disabled", skipped1)
        assertFalse("Dialogue must not be completed", controller1.isCompleted.value)

        // Case 2: allowBypassHandshake = true (debug / reviewer bypass)
        val adapter2 = DefaultRiveAdapter()
        val controller2 = RiveDialogueController(world11Tree, adapter2)

        repeat(3) { controller2.advanceNode() }
        val skipped2 = controller2.skip(allowBypassHandshake = true)
        assertTrue("skip must return true when bypass is allowed", skipped2)
        assertTrue("Dialogue must be completed when bypassed", controller2.isCompleted.value)
    }

    @Test
    fun testDialogueControllerSkipAfterHandshakeCompletion() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world11Tree, adapter)

        repeat(3) { controller.advanceNode() }
        val solved = controller.submitHandshake("60")
        assertTrue(solved)

        val skipped = controller.skip(allowBypassHandshake = false)
        assertTrue("skip must succeed once handshake is completed", skipped)
        assertTrue(controller.isCompleted.value)
    }

    // ── 6. Non-regression of Worlds 1–10 ───────────────────────────────────────

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
    fun testAllWorldsOneThroughElevenHaveDistinctWorldNumbers() {
        for (i in 1..11) {
            val tree = repository.getDialogueForWorld(i)
            assertNotNull("World $i must exist", tree)
            assertEquals("World $i number mismatch", i, tree!!.worldNumber)
        }
    }
}
