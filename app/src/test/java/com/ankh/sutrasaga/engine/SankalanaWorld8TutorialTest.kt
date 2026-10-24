package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.data.repository.RiveDialogueRepository
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.InteractiveHandshake
import com.ankh.sutrasaga.domain.models.LinearEquation
import com.ankh.sutrasaga.domain.models.RiveAnimationConfig
import com.ankh.sutrasaga.domain.models.RiveSpeaker
import com.ankh.sutrasaga.domain.models.RiveSutraDialogueTree
import com.ankh.sutrasaga.domain.models.SankalanaClassification
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
 * P1 World 8 Tutorial Tests — Saṅkalana-Vyavakalanābhyām (Refined)
 *
 * Covers:
 * - Four-beat dialogue tree structure and schema compliance
 * - Sutra name and meaning correctness ("By Addition and by Subtraction")
 * - 45x - 23y = 113 & 23x - 45y = 91 demonstration produces x = 2, y = -1
 * - Intermediate calculations:
 *     Addition step: 68x - 68y = 204 => x - y = 3
 *     Subtraction step: 22x + 22y = 22 => x + y = 1
 *     Recombination: 2x = 4 => x = 2, 2y = -2 => y = -1
 * - Handshake: 22(x + y) = 22 => x + y = 22 / 22 = 1
 * - Handshake expected answer is "1", not "2", "3", "4", or "22"
 * - Handshake prompt does NOT reveal the solution
 * - P0.1 regression: null/blank expectedAnswer fails closed
 * - P1.1 regression: submitting wrong answers fails when expected is "1"
 * - Controller: incorrect answer does NOT advance tutorial
 * - Controller: correct answer "1" completes tutorial
 * - Other world generator sutra names are unchanged (Worlds 1–7)
 */
@RunWith(RobolectricTestRunner::class)
class SankalanaWorld8TutorialTest {

    private lateinit var generator: SankalanaVyavakalanabhyamGenerator
    private lateinit var world8Tree: RiveSutraDialogueTree
    private val repository = RiveDialogueRepository()

    @Before
    fun setUp() {
        generator = SankalanaVyavakalanabhyamGenerator(Random(42))

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
        val loaded = repository.getDialogueForWorld(8)
        assertNotNull("World 8 dialogue tree must exist in asset file", loaded)
        world8Tree = loaded!!
    }

    // ── 1. Sutra identity ──────────────────────────────────────────────────────

    @Test
    fun testSutraNameIsSankalanaVyavakalanabhyam() {
        assertEquals("Sankalana-Vyavakalanabhyam", generator.sutraName)
    }

    @Test
    fun testDialogueTreeSutraId() {
        assertEquals("sankalana_vyavakalanabhyam", world8Tree.sutraId)
    }

    @Test
    fun testDialogueTreeEnglishMeaningRefersToAdditionAndSubtraction() {
        assertTrue(
            "English meaning must reference 'By Addition and by Subtraction': '${world8Tree.englishMeaning}'",
            world8Tree.englishMeaning.contains("By Addition and by Subtraction", ignoreCase = true)
        )
    }

    @Test
    fun testDialogueTreeWorldNumber() {
        assertEquals(8, world8Tree.worldNumber)
    }

    // ── 2. Mathematical correctness ────────────────────────────────────────────

    @Test
    fun testCanonicalTutorialProblemProducesX2YMinus1() {
        val problem = generator.generateCanonicalTutorialProblem()
        assertEquals(2L, problem.correctAnswer)
        assertEquals(-1L, problem.expectedSecondaryAnswer)

        // Verify independent linear system solving
        val eq1 = LinearEquation(45, -23, 113)
        val eq2 = LinearEquation(23, -45, 91)
        val sol = generator.solveSystem(eq1, eq2)
        assertEquals(SankalanaClassification.UNIQUE_SOLUTION, sol.classification)
        assertEquals(Fraction(2, 1), sol.xSolution.reduced)
        assertEquals(Fraction(-1, 1), sol.ySolution.reduced)
        assertEquals(Fraction(1, 1), sol.xPlusY.reduced)
        assertEquals(Fraction(3, 1), sol.xMinusY.reduced)

        // Verify with VedicMathValidator
        assertTrue(
            "VedicMathValidator must verify canonical system",
            VedicMathValidator.verifySankalanaSystem(45, -23, 113, 23, -45, 91, 2, -1)
        )
    }

    @Test
    fun testIntermediateAdditionEquationIsAccurate() {
        val a1 = 45L
        val b1 = -23L
        val c1 = 113L
        val a2 = 23L
        val b2 = -45L
        val c2 = 91L

        val sumX = a1 + a2
        val sumY = b1 + b2
        val sumC = c1 + c2

        assertEquals(68L, sumX)
        assertEquals(-68L, sumY)
        assertEquals(204L, sumC)

        // Dividing by 68 gives x - y = 3
        assertEquals(3L, sumC / sumX)
    }

    @Test
    fun testIntermediateSubtractionEquationIsAccurate() {
        val a1 = 45L
        val b1 = -23L
        val c1 = 113L
        val a2 = 23L
        val b2 = -45L
        val c2 = 91L

        val diffX = a1 - a2
        val diffY = b1 - b2
        val diffC = c1 - c2

        assertEquals(22L, diffX)
        assertEquals(22L, diffY)
        assertEquals(22L, diffC)

        // Dividing by 22 gives x + y = 1
        assertEquals(1L, diffC / diffX)
    }

    @Test
    fun testRecombinationGivesExactSolutions() {
        val xMinusY = 3L
        val xPlusY = 1L

        // Adding equations: 2x = 4 => x = 2
        val twoX = xMinusY + xPlusY
        assertEquals(4L, twoX)
        val x = twoX / 2L
        assertEquals(2L, x)

        // Subtracting equations: 2y = -2 => y = -1
        val twoY = xPlusY - xMinusY
        assertEquals(-2L, twoY)
        val y = twoY / 2L
        assertEquals(-1L, y)

        // Verify in original equations
        assertEquals(113L, 45L * x + (-23L) * y)
        assertEquals(91L, 23L * x + (-45L) * y)
    }

    @Test
    fun testHandshakeCalculation22DividedBy22Is1() {
        assertEquals(1L, 22L / 22L)
    }

    @Test
    fun test20HandVerifiedSankalanaCases() {
        val cases = listOf(
            Triple(LinearEquation(5, 3, 29), LinearEquation(3, 5, 27), Pair(4L, 3L)),
            Triple(LinearEquation(7, 2, 31), LinearEquation(2, 7, 41), Pair(3L, 5L)),
            Triple(LinearEquation(8, 5, 31), LinearEquation(5, 8, 34), Pair(2L, 3L)),
            Triple(LinearEquation(9, 4, 31), LinearEquation(4, 9, 21), Pair(3L, 1L)),
            Triple(LinearEquation(6, 1, 23), LinearEquation(1, 6, -2), Pair(4L, -1L)),
            Triple(LinearEquation(10, 3, 29), LinearEquation(3, 10, 36), Pair(2L, 3L)),
            Triple(LinearEquation(4, 1, 14), LinearEquation(1, 4, 11), Pair(3L, 2L)),
            Triple(LinearEquation(5, 2, 24), LinearEquation(2, 5, 18), Pair(4L, 2L)),
            Triple(LinearEquation(7, 3, 41), LinearEquation(3, 7, 29), Pair(5L, 2L)),
            Triple(LinearEquation(8, 1, 26), LinearEquation(1, 8, 19), Pair(3L, 2L)),

            Triple(LinearEquation(12, 5, 29), LinearEquation(5, 12, 22), Pair(2L, 1L)),
            Triple(LinearEquation(15, 4, 38), LinearEquation(4, 15, 38), Pair(2L, 2L)),
            Triple(LinearEquation(11, 3, 31), LinearEquation(3, 11, 39), Pair(2L, 3L)),
            Triple(LinearEquation(9, 2, 25), LinearEquation(2, 9, -3), Pair(3L, -1L)),
            Triple(LinearEquation(14, 3, 37), LinearEquation(3, 14, 48), Pair(2L, 3L)),
            Triple(LinearEquation(6, 5, 27), LinearEquation(5, 6, 28), Pair(2L, 3L)),
            Triple(LinearEquation(7, 4, 32), LinearEquation(4, 7, 23), Pair(4L, 1L)),
            Triple(LinearEquation(10, 7, 51), LinearEquation(7, 10, 51), Pair(3L, 3L)),
            Triple(LinearEquation(8, 3, 35), LinearEquation(3, 8, 20), Pair(4L, 1L)),
            Triple(LinearEquation(13, 2, 43), LinearEquation(2, 13, 32), Pair(3L, 2L))
        )

        for ((index, item) in cases.withIndex()) {
            val (eq1, eq2, expected) = item
            val sol = generator.solveSystem(eq1, eq2)
            assertEquals("Case $index classification", SankalanaClassification.UNIQUE_SOLUTION, sol.classification)
            assertEquals("Case $index x", expected.first, sol.xSolution.reduced.numerator)
            assertEquals("Case $index y", expected.second, sol.ySolution.reduced.numerator)
            assertTrue(
                "Case $index VedicMathValidator verification",
                VedicMathValidator.verifySankalanaSystem(eq1.a, eq1.b, eq1.c, eq2.a, eq2.b, eq2.c, expected.first, expected.second)
            )
        }
    }

    // ── 3. Dialogue tree schema & 4 beats ──────────────────────────────────────

    @Test
    fun testDialogueTreePassesValidator() {
        val result = RiveDialogueValidator.validate(world8Tree)
        assertTrue("World 8 dialogue tree must be valid: ${result.errors}", result.isValid)
        assertTrue("World 8 dialogue tree must have zero validation errors", result.errors.isEmpty())
    }

    @Test
    fun testDialogueTreeHasExactlyFourBeats() {
        assertEquals("World 8 must have exactly 4 dialogue nodes", 4, world8Tree.dialogueNodes.size)
    }

    @Test
    fun testNode1IsShishyaHook() {
        val node1 = world8Tree.dialogueNodes[0]
        assertEquals(1, node1.nodeId)
        assertEquals(RiveSpeaker.SHISHYA, node1.speaker)
        assertTrue("Node 1 text must not be blank", node1.text.isNotBlank())
        assertTrue("Node 1 text must mention 45x or large numbers", node1.text.contains("45x") || node1.text.contains("numbers"))
        assertEquals("shishya_curious", node1.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.SHISHYA, node1.riveState.animation))
        assertFalse(node1.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode2IsGuruSutraReveal() {
        val node2 = world8Tree.dialogueNodes[1]
        assertEquals(2, node2.nodeId)
        assertEquals(RiveSpeaker.GURU, node2.speaker)
        assertTrue("Node 2 text must not be blank", node2.text.isNotBlank())
        assertTrue("Node 2 text must introduce Saṅkalana-Vyavakalanābhyām", node2.text.contains("Saṅkalana-Vyavakalanābhyām") || node2.text.contains("Addition and by Subtraction"))
        assertEquals("guru_explain", node2.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node2.riveState.animation))
        assertFalse(node2.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode3IsGuruGuidedDemonstration() {
        val node3 = world8Tree.dialogueNodes[2]
        assertEquals(3, node3.nodeId)
        assertEquals(RiveSpeaker.GURU, node3.speaker)
        assertTrue("Node 3 text must not be blank", node3.text.isNotBlank())
        assertTrue("Node 3 text must demonstrate addition and subtraction steps", node3.text.contains("68") && node3.text.contains("22"))
        assertEquals("guru_explain", node3.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node3.riveState.animation))
        assertFalse(node3.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode4IsGuruActiveHandshake() {
        val node4 = world8Tree.dialogueNodes[3]
        assertEquals(4, node4.nodeId)
        assertEquals(RiveSpeaker.GURU, node4.speaker)
        assertTrue("Node 4 text must not be blank", node4.text.isNotBlank())
        assertTrue("Node 4 must ask for 22 ÷ 22 or x + y", node4.text.contains("22 ÷ 22") || node4.text.contains("x + y"))
        assertEquals("guru_magic_fx", node4.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node4.riveState.animation))
        assertTrue("Node 4 must require user tap", node4.interactiveHandshake.requiresUserTap)
        assertEquals("numpad_key_1", node4.interactiveHandshake.targetElementId)
        assertEquals("1", node4.interactiveHandshake.expectedAnswer)
    }

    @Test
    fun testGuruNeverWinks() {
        for (node in world8Tree.dialogueNodes) {
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
        for (node in world8Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} SSML must contain <speak>", node.audioCues.ssmlText.contains("<speak>"))
            assertTrue("Node ${node.nodeId} SSML must contain </speak>", node.audioCues.ssmlText.contains("</speak>"))
            assertTrue("Node ${node.nodeId} SFX must not be blank", node.audioCues.sfx.isNotBlank())
        }
    }

    @Test
    fun testAllNodesHaveNonBlankMathOverlays() {
        for (node in world8Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} expression must not be blank", node.mathOverlay.expression.isNotBlank())
            assertTrue("Node ${node.nodeId} highlight tokens must not be empty", node.mathOverlay.highlightTokens.isNotEmpty())
        }
    }

    // ── 4. Active Handshake & expected answer authority ────────────────────────

    @Test
    fun testHandshakeAuthoritativeAnswerIsOne() {
        val handshake = world8Tree.dialogueNodes.last().interactiveHandshake
        assertEquals("Authoritative expected answer must be '1'", "1", handshake.expectedAnswer)
    }

    @Test
    fun testHandshakePromptDoesNotRevealExpectedAnswer() {
        val handshake = world8Tree.dialogueNodes.last().interactiveHandshake
        val prompt = handshake.promptText
        assertNotNull("Prompt must not be null", prompt)
        assertFalse(
            "Prompt '$prompt' must not give away '= 1'",
            prompt!!.contains("= 1") || prompt.contains("is 1")
        )
    }

    @Test
    fun testHandshakeMathOverlayExpressionDoesNotRevealExpectedAnswer() {
        val finalNode = world8Tree.dialogueNodes.last()
        val expr = finalNode.mathOverlay.expression
        assertTrue("Overlay should indicate unknown: '$expr'", expr.contains("?") || expr.contains("[ ? ]"))
        assertFalse("Overlay must not leak '= 1': '$expr'", expr.contains("= 1"))
    }

    @Test
    fun testHandshakeChoicesIncludeExpectedAnswer() {
        val expected = world8Tree.dialogueNodes.last().interactiveHandshake.expectedAnswer
        val choices = generateHandshakeChoices(expected)
        assertTrue("Generated choices $choices must contain '$expected'", choices.contains(expected))
        assertEquals("Generated choices for '1' must be 5 items", 5, choices.size)
    }

    @Test
    fun testHandshakeFailsClosedOnNullOrBlankExpectedAnswer() {
        val nullAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_1",
            promptText = "Calculate x + y",
            expectedAnswer = null
        )
        assertFalse(
            "Null expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(nullAnswerHandshake, "1")
        )

        val blankAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_1",
            promptText = "Calculate x + y",
            expectedAnswer = "   "
        )
        assertFalse(
            "Blank expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(blankAnswerHandshake, "1")
        )
    }

    @Test
    fun testHandshakeEvaluatesStrictlyAgainstExpectedAnswer() {
        val handshake = world8Tree.dialogueNodes.last().interactiveHandshake

        assertTrue("Submitting '1' must pass", VedicMathValidator.evaluateHandshake(handshake, "1"))
        assertTrue("Submitting ' 1 ' with whitespace must pass", VedicMathValidator.evaluateHandshake(handshake, " 1 "))

        assertFalse("Submitting '2' (solution x) must fail", VedicMathValidator.evaluateHandshake(handshake, "2"))
        assertFalse("Submitting '3' (intermediate x - y) must fail", VedicMathValidator.evaluateHandshake(handshake, "3"))
        assertFalse("Submitting '4' (2x) must fail", VedicMathValidator.evaluateHandshake(handshake, "4"))
        assertFalse("Submitting '22' must fail", VedicMathValidator.evaluateHandshake(handshake, "22"))
        assertFalse("Submitting '-1' (solution y) must fail", VedicMathValidator.evaluateHandshake(handshake, "-1"))
        assertFalse("Submitting empty string must fail", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("Submitting whitespace must fail", VedicMathValidator.evaluateHandshake(handshake, "   "))
    }

    // ── 5. Controller & Skip behavior ──────────────────────────────────────────

    @Test
    fun testDialogueControllerAdvancesThroughFirstThreeNodes() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world8Tree, adapter)

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
        val controller = RiveDialogueController(world8Tree, adapter)

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
        val controller = RiveDialogueController(world8Tree, adapter)

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
        val controller = RiveDialogueController(world8Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("1")
        assertTrue("Correct answer '1' must return true", success)
        assertTrue("Dialogue must be marked completed", controller.isCompleted.value)
        assertEquals("shishya_celebrate", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSkipBehavior() {
        // Case 1: allowBypassHandshake = false (default student path)
        val adapter1 = DefaultRiveAdapter()
        val controller1 = RiveDialogueController(world8Tree, adapter1)

        repeat(3) { controller1.advanceNode() }
        assertEquals(3, controller1.currentNodeIndex.value)

        val skipped1 = controller1.skip(allowBypassHandshake = false)
        assertFalse("skip must return false when handshake is unresolved and bypass is disabled", skipped1)
        assertFalse("Dialogue must not be completed", controller1.isCompleted.value)

        // Case 2: allowBypassHandshake = true (debug / reviewer bypass)
        val adapter2 = DefaultRiveAdapter()
        val controller2 = RiveDialogueController(world8Tree, adapter2)

        repeat(3) { controller2.advanceNode() }
        val skipped2 = controller2.skip(allowBypassHandshake = true)
        assertTrue("skip must return true when bypass is allowed", skipped2)
        assertTrue("Dialogue must be completed when bypassed", controller2.isCompleted.value)
    }

    @Test
    fun testDialogueControllerSkipAfterHandshakeCompletion() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world8Tree, adapter)

        repeat(3) { controller.advanceNode() }
        val solved = controller.submitHandshake("1")
        assertTrue(solved)

        val skipped = controller.skip(allowBypassHandshake = false)
        assertTrue("skip must succeed once handshake is completed", skipped)
        assertTrue(controller.isCompleted.value)
    }

    // ── 6. Non-regression of Worlds 1–7 ────────────────────────────────────────

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
    fun testAllWorldsOneThroughEightHaveDistinctWorldNumbers() {
        for (i in 1..8) {
            val tree = repository.getDialogueForWorld(i)
            assertNotNull("World $i must exist", tree)
            assertEquals("World $i number mismatch", i, tree!!.worldNumber)
        }
    }
}
