package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.data.repository.RiveDialogueRepository
import com.ankh.sutrasaga.domain.models.CommonFactorEquation
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.InteractiveHandshake
import com.ankh.sutrasaga.domain.models.LinearExpression
import com.ankh.sutrasaga.domain.models.RiveAnimationConfig
import com.ankh.sutrasaga.domain.models.RiveSpeaker
import com.ankh.sutrasaga.domain.models.RiveSutraDialogueTree
import com.ankh.sutrasaga.domain.models.ShunyamClassification
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
import java.io.File
import kotlin.random.Random

/**
 * P1 World 9 Tutorial Tests — Śūnyaṁ Sāmyasamuccaye (Refined)
 *
 * Covers:
 * - Four-beat dialogue tree structure and schema compliance
 * - Sutra name and meaning correctness ("When the Collection is the Same, it is Zero")
 * - 7(x + 1) = 8(x + 1) demonstration produces x = -1
 * - Intermediate calculations:
 *     Common factor identification: F(x) = x + 1
 *     Unequal multipliers: 7 != 8 => (7 - 8)(x + 1) = 0 => x + 1 = 0
 *     Root resolution: x = -1
 * - Handshake: x + 1 = 0 => x = -1
 * - Handshake expected answer is "-1", not "0", "1", "7", or "8"
 * - Handshake prompt does NOT reveal the solution
 * - P0.1 regression: null/blank expectedAnswer fails closed
 * - P1.1 regression: submitting wrong answers fails when expected is "-1"
 * - Controller: incorrect answer does NOT advance tutorial
 * - Controller: correct answer "-1" completes tutorial
 * - Non-regression: Worlds 1–8 dialogue trees and generators unaffected
 */
@RunWith(RobolectricTestRunner::class)
class ShunyamWorld9TutorialTest {

    private lateinit var generator: ShunyamSamyasamuccayeGenerator
    private lateinit var world9Tree: RiveSutraDialogueTree
    private val repository = RiveDialogueRepository()

    @Before
    fun setUp() {
        generator = ShunyamSamyasamuccayeGenerator(Random(42))

        val candidateFiles = listOf(
            File("src/main/assets/dialogue/rive_sutra_dialogues.json"),
            File("app/src/main/assets/dialogue/rive_sutra_dialogues.json"),
            File(System.getProperty("user.dir"), "app/src/main/assets/dialogue/rive_sutra_dialogues.json"),
            File(System.getProperty("user.dir"), "src/main/assets/dialogue/rive_sutra_dialogues.json")
        )
        val jsonFile = candidateFiles.firstOrNull { it.exists() }
        assertNotNull("Asset file rive_sutra_dialogues.json must be found", jsonFile)
        val jsonContent = jsonFile!!.readText()
        repository.parseJson(jsonContent)
        val loaded = repository.getDialogueForWorld(9)
        assertNotNull("World 9 dialogue tree must exist in asset file", loaded)
        world9Tree = loaded!!
    }

    // ── 1. Sutra identity ──────────────────────────────────────────────────────

    @Test
    fun testSutraNameIsShunyamSamyasamuccaye() {
        assertEquals("Shunyam Samyasamuccaye", generator.sutraName)
    }

    @Test
    fun testDialogueTreeSutraId() {
        assertEquals("shunyam_samyasamuccaye", world9Tree.sutraId)
    }

    @Test
    fun testDialogueTreeEnglishMeaningRefersToCollectionAndZero() {
        assertTrue(
            "English meaning must reference 'When the Collection is the Same, it is Zero': '${world9Tree.englishMeaning}'",
            world9Tree.englishMeaning.contains("When the Collection is the Same, it is Zero", ignoreCase = true)
        )
    }

    @Test
    fun testDialogueTreeWorldNumber() {
        assertEquals(9, world9Tree.worldNumber)
    }

    // ── 2. Mathematical correctness ────────────────────────────────────────────

    @Test
    fun testCanonicalTutorialProblemProducesXMinus1() {
        val problem = generator.generateCanonicalTutorialProblem()
        assertEquals(-1L, problem.correctAnswer)

        // Verify independent solver logic
        val eq = CommonFactorEquation(
            k1 = 7L,
            factor = LinearExpression(Fraction(1L, 1L), Fraction(1L, 1L)),
            k2 = 8L
        )
        val sol = generator.solveFamilyA(eq)
        assertEquals(ShunyamClassification.UNIQUE_SOLUTION, sol.classification)
        assertEquals(Fraction(-1L, 1L), sol.candidateSolution.reduced)

        // Verify with VedicMathValidator
        assertTrue(
            "VedicMathValidator must verify canonical common factor system",
            VedicMathValidator.verifyShunyamCommonFactor(7L, 1L, 1L, 8L, -1L)
        )
    }

    @Test
    fun testIntermediateStepEquationXPlus1Equals0() {
        // 7(x+1) = 8(x+1) => (7 - 8)(x + 1) = 0 => -1(x + 1) = 0 => x + 1 = 0
        val k1 = 7L
        val k2 = 8L
        val diff = k1 - k2
        assertEquals(-1L, diff)
        assertTrue("Multipliers must be unequal", diff != 0L)

        // Solving x + 1 = 0 gives x = -1
        val x = -1L
        assertEquals(0L, x + 1L)
        assertEquals(0L, k1 * (x + 1L))
        assertEquals(0L, k2 * (x + 1L))
    }

    @Test
    fun testHandshakeCalculationXPlus1EqualsZeroYieldsMinus1() {
        val x = -1L
        assertEquals(0L, x + 1L)
    }

    @Test
    fun test20HandVerifiedFamilyACases() {
        val cases = listOf(
            Pair(CommonFactorEquation(2L, LinearExpression(Fraction(1L, 1L), Fraction(3L, 1L)), 5L), -3L),
            Pair(CommonFactorEquation(4L, LinearExpression(Fraction(1L, 1L), Fraction(-2L, 1L)), 9L), 2L),
            Pair(CommonFactorEquation(3L, LinearExpression(Fraction(2L, 1L), Fraction(6L, 1L)), 7L), -3L),
            Pair(CommonFactorEquation(5L, LinearExpression(Fraction(3L, 1L), Fraction(-9L, 1L)), 11L), 3L),
            Pair(CommonFactorEquation(6L, LinearExpression(Fraction(1L, 1L), Fraction(5L, 1L)), 12L), -5L),

            Pair(CommonFactorEquation(8L, LinearExpression(Fraction(4L, 1L), Fraction(12L, 1L)), 15L), -3L),
            Pair(CommonFactorEquation(10L, LinearExpression(Fraction(1L, 1L), Fraction(-7L, 1L)), 3L), 7L),
            Pair(CommonFactorEquation(1L, LinearExpression(Fraction(5L, 1L), Fraction(10L, 1L)), 6L), -2L),
            Pair(CommonFactorEquation(9L, LinearExpression(Fraction(2L, 1L), Fraction(-4L, 1L)), 2L), 2L),
            Pair(CommonFactorEquation(14L, LinearExpression(Fraction(3L, 1L), Fraction(15L, 1L)), 20L), -5L),

            Pair(CommonFactorEquation(7L, LinearExpression(Fraction(1L, 1L), Fraction(8L, 1L)), 13L), -8L),
            Pair(CommonFactorEquation(11L, LinearExpression(Fraction(2L, 1L), Fraction(8L, 1L)), 4L), -4L),
            Pair(CommonFactorEquation(3L, LinearExpression(Fraction(4L, 1L), Fraction(-16L, 1L)), 8L), 4L),
            Pair(CommonFactorEquation(12L, LinearExpression(Fraction(1L, 1L), Fraction(-10L, 1L)), 5L), 10L),
            Pair(CommonFactorEquation(15L, LinearExpression(Fraction(6L, 1L), Fraction(18L, 1L)), 2L), -3L),

            Pair(CommonFactorEquation(18L, LinearExpression(Fraction(1L, 1L), Fraction(4L, 1L)), 7L), -4L),
            Pair(CommonFactorEquation(21L, LinearExpression(Fraction(3L, 1L), Fraction(-12L, 1L)), 10L), 4L),
            Pair(CommonFactorEquation(13L, LinearExpression(Fraction(2L, 1L), Fraction(14L, 1L)), 25L), -7L),
            Pair(CommonFactorEquation(16L, LinearExpression(Fraction(5L, 1L), Fraction(-25L, 1L)), 3L), 5L),
            Pair(CommonFactorEquation(19L, LinearExpression(Fraction(1L, 1L), Fraction(-6L, 1L)), 4L), 6L)
        )

        for ((eq, expectedRoot) in cases) {
            val sol = generator.solveFamilyA(eq)
            assertEquals("Classification for ${eq.toFormattedString()}", ShunyamClassification.UNIQUE_SOLUTION, sol.classification)
            assertEquals("Root for ${eq.toFormattedString()}", expectedRoot, sol.candidateSolution.reduced.numerator)
            assertTrue(
                "VedicMathValidator check for ${eq.toFormattedString()}",
                VedicMathValidator.verifyShunyamCommonFactor(eq.k1, eq.factor.a.numerator, eq.factor.b.numerator, eq.k2, expectedRoot)
            )
        }
    }

    // ── 3. Dialogue tree schema & 4 beats ──────────────────────────────────────

    @Test
    fun testDialogueTreePassesValidator() {
        val result = RiveDialogueValidator.validate(world9Tree)
        assertTrue("World 9 dialogue tree must be valid: ${result.errors}", result.isValid)
        assertTrue("World 9 dialogue tree must have zero validation errors", result.errors.isEmpty())
    }

    @Test
    fun testDialogueTreeHasExactlyFourBeats() {
        assertEquals("World 9 must have exactly 4 dialogue nodes", 4, world9Tree.dialogueNodes.size)
    }

    @Test
    fun testNode1IsShishyaHook() {
        val node1 = world9Tree.dialogueNodes[0]
        assertEquals(1, node1.nodeId)
        assertEquals(RiveSpeaker.SHISHYA, node1.speaker)
        assertTrue("Node 1 text must not be blank", node1.text.isNotBlank())
        assertTrue("Node 1 text must mention 7(x + 1) or brackets", node1.text.contains("7(x + 1)") || node1.text.contains("brackets"))
        assertEquals("shishya_curious", node1.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.SHISHYA, node1.riveState.animation))
        assertFalse(node1.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode2IsGuruSutraReveal() {
        val node2 = world9Tree.dialogueNodes[1]
        assertEquals(2, node2.nodeId)
        assertEquals(RiveSpeaker.GURU, node2.speaker)
        assertTrue("Node 2 text must not be blank", node2.text.isNotBlank())
        assertTrue("Node 2 text must introduce Śūnyaṁ Sāmyasamuccaye", node2.text.contains("Śūnyaṁ Sāmyasamuccaye") || node2.text.contains("Collection is the Same"))
        assertEquals("guru_explain", node2.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node2.riveState.animation))
        assertFalse(node2.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode3IsGuruGuidedDemonstration() {
        val node3 = world9Tree.dialogueNodes[2]
        assertEquals(3, node3.nodeId)
        assertEquals(RiveSpeaker.GURU, node3.speaker)
        assertTrue("Node 3 text must not be blank", node3.text.isNotBlank())
        assertTrue("Node 3 text must show subtraction to x + 1 = 0 and x = -1", node3.text.contains("x + 1 = 0") && node3.text.contains("-1"))
        assertEquals("guru_explain", node3.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node3.riveState.animation))
        assertFalse(node3.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode4IsGuruActiveHandshake() {
        val node4 = world9Tree.dialogueNodes[3]
        assertEquals(4, node4.nodeId)
        assertEquals(RiveSpeaker.GURU, node4.speaker)
        assertTrue("Node 4 text must not be blank", node4.text.isNotBlank())
        assertTrue("Node 4 must ask to solve x + 1 = 0", node4.text.contains("x + 1 = 0"))
        assertEquals("guru_magic_fx", node4.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node4.riveState.animation))
        assertTrue("Node 4 must require user tap", node4.interactiveHandshake.requiresUserTap)
        assertEquals("numpad_key_neg1", node4.interactiveHandshake.targetElementId)
        assertEquals("-1", node4.interactiveHandshake.expectedAnswer)
    }

    @Test
    fun testGuruNeverWinks() {
        for (node in world9Tree.dialogueNodes) {
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
        for (node in world9Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} SSML must contain <speak>", node.audioCues.ssmlText.contains("<speak>"))
            assertTrue("Node ${node.nodeId} SSML must contain </speak>", node.audioCues.ssmlText.contains("</speak>"))
            assertTrue("Node ${node.nodeId} SFX must not be blank", node.audioCues.sfx.isNotBlank())
        }
    }

    @Test
    fun testAllNodesHaveNonBlankMathOverlays() {
        for (node in world9Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} expression must not be blank", node.mathOverlay.expression.isNotBlank())
            assertTrue("Node ${node.nodeId} highlight tokens must not be empty", node.mathOverlay.highlightTokens.isNotEmpty())
        }
    }

    // ── 4. Active Handshake & expected answer authority ────────────────────────

    @Test
    fun testHandshakeAuthoritativeAnswerIsMinusOne() {
        val handshake = world9Tree.dialogueNodes.last().interactiveHandshake
        assertEquals("Authoritative expected answer must be '-1'", "-1", handshake.expectedAnswer)
    }

    @Test
    fun testHandshakePromptDoesNotRevealExpectedAnswer() {
        val handshake = world9Tree.dialogueNodes.last().interactiveHandshake
        val prompt = handshake.promptText
        assertNotNull("Prompt must not be null", prompt)
        assertFalse(
            "Prompt '$prompt' must not leak '= -1'",
            prompt!!.contains("= -1") || prompt.contains("is -1")
        )
    }

    @Test
    fun testHandshakeMathOverlayExpressionDoesNotRevealExpectedAnswer() {
        val finalNode = world9Tree.dialogueNodes.last()
        val expr = finalNode.mathOverlay.expression
        assertTrue("Overlay should indicate unknown: '$expr'", expr.contains("?") || expr.contains("[ ? ]"))
        assertFalse("Overlay must not leak '= -1': '$expr'", expr.contains("= -1"))
    }

    @Test
    fun testHandshakeChoicesIncludeExpectedAnswer() {
        val expected = world9Tree.dialogueNodes.last().interactiveHandshake.expectedAnswer
        val choices = generateHandshakeChoices(expected)
        assertTrue("Generated choices $choices must contain '$expected'", choices.contains(expected))
        assertEquals("Generated choices for '-1' must be 5 items", 5, choices.size)
        assertEquals(listOf("-3", "-2", "-1", "0", "1"), choices)
    }

    @Test
    fun testHandshakeFailsClosedOnNullOrBlankExpectedAnswer() {
        val nullAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_neg1",
            promptText = "Solve x + 1 = 0",
            expectedAnswer = null
        )
        assertFalse(
            "Null expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(nullAnswerHandshake, "-1")
        )

        val blankAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_neg1",
            promptText = "Solve x + 1 = 0",
            expectedAnswer = "   "
        )
        assertFalse(
            "Blank expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(blankAnswerHandshake, "-1")
        )
    }

    @Test
    fun testHandshakeEvaluatesStrictlyAgainstExpectedAnswer() {
        val handshake = world9Tree.dialogueNodes.last().interactiveHandshake

        assertTrue("Submitting '-1' must pass", VedicMathValidator.evaluateHandshake(handshake, "-1"))
        assertTrue("Submitting ' -1 ' with whitespace must pass", VedicMathValidator.evaluateHandshake(handshake, " -1 "))

        assertFalse("Submitting '0' must fail", VedicMathValidator.evaluateHandshake(handshake, "0"))
        assertFalse("Submitting '1' must fail", VedicMathValidator.evaluateHandshake(handshake, "1"))
        assertFalse("Submitting '7' must fail", VedicMathValidator.evaluateHandshake(handshake, "7"))
        assertFalse("Submitting '8' must fail", VedicMathValidator.evaluateHandshake(handshake, "8"))
        assertFalse("Submitting empty string must fail", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("Submitting whitespace must fail", VedicMathValidator.evaluateHandshake(handshake, "   "))
    }

    // ── 5. Controller & Skip behavior ──────────────────────────────────────────

    @Test
    fun testDialogueControllerAdvancesThroughFirstThreeNodes() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world9Tree, adapter)

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
        val controller = RiveDialogueController(world9Tree, adapter)

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
        val controller = RiveDialogueController(world9Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("0")
        assertFalse("Incorrect answer '0' must return false", success)
        assertFalse("Dialogue must not complete", controller.isCompleted.value)
        assertNotNull("Handshake error message must be set", controller.handshakeError.value)
        assertEquals("shishya_puzzled", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSubmittingCorrectAnswerCompletesDialogue() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world9Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("-1")
        assertTrue("Correct answer '-1' must return true", success)
        assertTrue("Dialogue must be marked completed", controller.isCompleted.value)
        assertEquals("shishya_celebrate", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSkipBehavior() {
        // Case 1: allowBypassHandshake = false (default student path)
        val adapter1 = DefaultRiveAdapter()
        val controller1 = RiveDialogueController(world9Tree, adapter1)

        repeat(3) { controller1.advanceNode() }
        assertEquals(3, controller1.currentNodeIndex.value)

        val skipped1 = controller1.skip(allowBypassHandshake = false)
        assertFalse("skip must return false when handshake is unresolved and bypass is disabled", skipped1)
        assertFalse("Dialogue must not be completed", controller1.isCompleted.value)

        // Case 2: allowBypassHandshake = true (debug / reviewer bypass)
        val adapter2 = DefaultRiveAdapter()
        val controller2 = RiveDialogueController(world9Tree, adapter2)

        repeat(3) { controller2.advanceNode() }
        val skipped2 = controller2.skip(allowBypassHandshake = true)
        assertTrue("skip must return true when bypass is allowed", skipped2)
        assertTrue("Dialogue must be completed when bypassed", controller2.isCompleted.value)
    }

    @Test
    fun testDialogueControllerSkipAfterHandshakeCompletion() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world9Tree, adapter)

        repeat(3) { controller.advanceNode() }
        val solved = controller.submitHandshake("-1")
        assertTrue(solved)

        val skipped = controller.skip(allowBypassHandshake = false)
        assertTrue("skip must succeed once handshake is completed", skipped)
        assertTrue(controller.isCompleted.value)
    }

    // ── 6. Non-regression of Worlds 1–8 ────────────────────────────────────────

    @Test
    fun testWorld1TutorialStillPassesValidation() {
        val world1 = repository.getDialogueForWorld(1)
        assertNotNull("World 1 dialogue must still exist", world1)
        val result = RiveDialogueValidator.validate(world1!!)
        assertTrue("World 1 must remain valid: ${result.errors}", result.isValid)
    }

    @Test
    fun testWorld2TutorialStillPassesValidation() {
        val world2 = repository.getDialogueForWorld(2)
        assertNotNull("World 2 dialogue must still exist", world2)
        val result = RiveDialogueValidator.validate(world2!!)
        assertTrue("World 2 must remain valid: ${result.errors}", result.isValid)
    }

    @Test
    fun testWorld3TutorialStillPassesValidation() {
        val world3 = repository.getDialogueForWorld(3)
        assertNotNull("World 3 dialogue must still exist", world3)
        val result = RiveDialogueValidator.validate(world3!!)
        assertTrue("World 3 must remain valid: ${result.errors}", result.isValid)
    }

    @Test
    fun testWorld4TutorialStillPassesValidation() {
        val world4 = repository.getDialogueForWorld(4)
        assertNotNull("World 4 dialogue must still exist", world4)
        val result = RiveDialogueValidator.validate(world4!!)
        assertTrue("World 4 must remain valid: ${result.errors}", result.isValid)
    }

    @Test
    fun testWorld5TutorialStillPassesValidation() {
        val world5 = repository.getDialogueForWorld(5)
        assertNotNull("World 5 dialogue must still exist", world5)
        val result = RiveDialogueValidator.validate(world5!!)
        assertTrue("World 5 must remain valid: ${result.errors}", result.isValid)
    }

    @Test
    fun testWorld6TutorialStillPassesValidation() {
        val world6 = repository.getDialogueForWorld(6)
        assertNotNull("World 6 dialogue must still exist", world6)
        val result = RiveDialogueValidator.validate(world6!!)
        assertTrue("World 6 must remain valid: ${result.errors}", result.isValid)
    }

    @Test
    fun testWorld7TutorialStillPassesValidation() {
        val world7 = repository.getDialogueForWorld(7)
        assertNotNull("World 7 dialogue must still exist", world7)
        val result = RiveDialogueValidator.validate(world7!!)
        assertTrue("World 7 must remain valid: ${result.errors}", result.isValid)
    }

    @Test
    fun testWorld8TutorialStillPassesValidation() {
        val world8 = repository.getDialogueForWorld(8)
        assertNotNull("World 8 dialogue must still exist", world8)
        val result = RiveDialogueValidator.validate(world8!!)
        assertTrue("World 8 must remain valid: ${result.errors}", result.isValid)
    }

    @Test
    fun testAllWorldsOneThroughNineHaveDistinctWorldNumbers() {
        for (i in 1..9) {
            val tree = repository.getDialogueForWorld(i)
            assertNotNull("World $i must exist", tree)
            assertEquals("World $i number mismatch", i, tree!!.worldNumber)
        }
    }
}
