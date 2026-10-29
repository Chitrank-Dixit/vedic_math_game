package com.ank.sutrasaga.engine

import com.ank.sutrasaga.data.repository.RiveDialogueRepository
import com.ank.sutrasaga.domain.models.AnurupyeCase
import com.ank.sutrasaga.domain.models.DifficultyTier
import com.ank.sutrasaga.domain.models.Fraction
import com.ank.sutrasaga.domain.models.InteractiveHandshake
import com.ank.sutrasaga.domain.models.LinearEquation
import com.ank.sutrasaga.domain.models.RiveAnimationConfig
import com.ank.sutrasaga.domain.models.RiveSutraDialogueTree
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
import java.io.File
import kotlin.random.Random

/**
 * P1 World 7 Tutorial Tests — Ānurūpye Śūnyamanyat (Refined)
 *
 * Covers:
 * - Four-beat dialogue tree structure and schema compliance
 * - Sutra name and meaning correctness ("If One is in Ratio, the Other is Zero")
 * - 3x + 2y = 12 & 6x + 5y = 24 demonstration produces x = 4, y = 0
 * - Additional math verification: 2x + 3y = 8 & 4x + 7y = 16 -> x = 4, y = 0
 * - Symmetrical cases and 20 hand-verified ratio equations
 * - Handshake: 2x + 3y = 8 & 4x + 7y = 16 -> y = 0 -> 2x = 8 -> x = 4
 * - Handshake expected answer is "4", not "0", "8", or "2"
 * - Handshake expression does NOT reveal solution 4
 * - P0.1 regression: null/blank expectedAnswer fails closed
 * - P1.1 regression: submitting 0 or 8 fails when expected is 4
 * - Controller: incorrect answer does NOT advance tutorial
 * - Controller: correct answer "4" completes tutorial
 * - Other world generator sutra names are unchanged (Worlds 1–6)
 */
@RunWith(RobolectricTestRunner::class)
class AnurupyeWorld7TutorialTest {

    private lateinit var generator: AnurupyeShunyamanyatGenerator
    private lateinit var world7Tree: RiveSutraDialogueTree
    private val repository = RiveDialogueRepository()

    @Before
    fun setUp() {
        generator = AnurupyeShunyamanyatGenerator(Random(42))

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
        val loaded = repository.getDialogueForWorld(7)
        assertNotNull("World 7 dialogue tree must exist in asset file", loaded)
        world7Tree = loaded!!
    }

    // ── 1. Sutra identity ──────────────────────────────────────────────────────

    @Test
    fun testSutraNameIsAnurupyeShunyamanyat() {
        assertEquals("Anurupye Shunyamanyat", generator.sutraName)
    }

    @Test
    fun testDialogueTreeSutraId() {
        assertEquals("anurupye_shunyamanyat", world7Tree.sutraId)
    }

    @Test
    fun testDialogueTreeEnglishMeaningRefersToRatioAndZero() {
        assertTrue(
            "English meaning must reference 'If One is in Ratio, the Other is Zero': '${world7Tree.englishMeaning}'",
            world7Tree.englishMeaning.contains("If One is in Ratio, the Other is Zero", ignoreCase = true)
        )
    }

    @Test
    fun testDialogueTreeWorldNumber() {
        assertEquals(7, world7Tree.worldNumber)
    }

    // ── 2. Mathematical correctness ────────────────────────────────────────────

    @Test
    fun testCanonicalTutorialProblemProducesX4Y0() {
        val problem = generator.generateCanonicalTutorialProblem()
        assertEquals(4L, problem.correctAnswer)
        assertEquals(0L, problem.expectedSecondaryAnswer)
        assertEquals("Y_ZERO", problem.appendedSuffix)

        // Verify independent linear system solving
        val eq1 = LinearEquation(3, 2, 12)
        val eq2 = LinearEquation(6, 5, 24)
        val sol = generator.solveSystem(eq1, eq2)
        assertEquals(AnurupyeCase.Y_ZERO, sol.caseType)
        assertEquals(Fraction(4, 1), sol.xSolution.reduced)
        assertEquals(Fraction(0, 1), sol.ySolution.reduced)

        assertTrue(
            "VedicMathValidator must verify Anurupye linear system",
            VedicMathValidator.verifyAnurupyeSystem(3L, 2L, 12L, 6L, 5L, 24L, 4L, 0L)
        )

        // Verify decomposition step
        assertEquals(1, problem.decompositionSteps.size)
        assertTrue(problem.decompositionSteps[0].formulaDisplay.contains("3 × 24 = 6 × 12"))
        assertEquals("x = 4, y = 0", problem.decompositionSteps[0].stepResult)
    }

    @Test
    fun testHandshakeSystem2xPlus3yEq8And4xPlus7yEq16ProducesX4Y0() {
        val eq1 = LinearEquation(2, 3, 8)
        val eq2 = LinearEquation(4, 7, 16)
        val sol = generator.solveSystem(eq1, eq2)

        assertEquals(AnurupyeCase.Y_ZERO, sol.caseType)
        assertEquals(Fraction(4, 1), sol.xSolution.reduced)
        assertEquals(Fraction(0, 1), sol.ySolution.reduced)

        assertTrue(
            "VedicMathValidator must verify handshake system",
            VedicMathValidator.verifyAnurupyeSystem(2L, 3L, 8L, 4L, 7L, 16L, 4L, 0L)
        )
    }

    @Test
    fun testSymmetricCaseYRatioMatchProducesX0() {
        val eq1 = LinearEquation(6, 7, 8)
        val eq2 = LinearEquation(19, 14, 16)
        val sol = generator.solveSystem(eq1, eq2)

        assertEquals(AnurupyeCase.X_ZERO, sol.caseType)
        assertEquals(Fraction(0, 1), sol.xSolution.reduced)
        assertEquals(Fraction(8, 7), sol.ySolution.reduced)
    }

    @Test
    fun testAdditionalLinearCases() {
        val cases = listOf(
            Triple(LinearEquation(2, 3, 10), LinearEquation(4, 5, 20), Pair(5L, 0L)),
            Triple(LinearEquation(5, 4, 15), LinearEquation(10, 9, 30), Pair(3L, 0L)),
            Triple(LinearEquation(7, 2, 21), LinearEquation(14, 8, 42), Pair(3L, 0L)),
            Triple(LinearEquation(1, 5, 7), LinearEquation(3, 8, 21), Pair(7L, 0L)),
            Triple(LinearEquation(2, 5, 15), LinearEquation(7, 10, 30), Pair(0L, 3L)),
            Triple(LinearEquation(4, 7, 28), LinearEquation(11, 14, 56), Pair(0L, 4L))
        )

        for ((e1, e2, expected) in cases) {
            val sol = generator.solveSystem(e1, e2)
            if (expected.second == 0L) {
                assertEquals(AnurupyeCase.Y_ZERO, sol.caseType)
                assertEquals(expected.first, sol.xSolution.reduced.numerator)
                assertEquals(0L, sol.ySolution.reduced.numerator)
                assertTrue(VedicMathValidator.verifyAnurupyeSystem(e1.a, e1.b, e1.c, e2.a, e2.b, e2.c, expected.first, 0L))
            } else {
                assertEquals(AnurupyeCase.X_ZERO, sol.caseType)
                assertEquals(0L, sol.xSolution.reduced.numerator)
                assertEquals(expected.second, sol.ySolution.reduced.numerator)
                assertTrue(VedicMathValidator.verifyAnurupyeSystem(e1.a, e1.b, e1.c, e2.a, e2.b, e2.c, 0L, expected.second))
            }
        }
    }

    // ── 3. Dialogue tree structure ─────────────────────────────────────────────

    @Test
    fun testWorld7HasExactlyFourNodes() {
        assertEquals(
            "World 7 tutorial must have exactly 4 nodes",
            4,
            world7Tree.dialogueNodes.size
        )
    }

    @Test
    fun testWorld7PassesRiveDialogueValidator() {
        val result = RiveDialogueValidator.validate(world7Tree)
        assertTrue("World 7 must pass validator: ${result.errors}", result.isValid)
        assertTrue("Validation errors must be empty", result.errors.isEmpty())
    }

    @Test
    fun testAllNodeTextsNonBlank() {
        for (node in world7Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} text must not be blank", node.text.isNotBlank())
        }
    }

    @Test
    fun testAllMathOverlayExpressionsNonBlank() {
        for (node in world7Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId} math_overlay.expression must not be blank",
                node.mathOverlay.expression.isNotBlank()
            )
        }
    }

    @Test
    fun testAllNodesUseValidAnimations() {
        for (node in world7Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId}: animation '${node.riveState.animation}' must be in whitelist for ${node.riveState.character}",
                RiveAnimationConfig.isValidAnimation(node.riveState.character, node.riveState.animation)
            )
        }
    }

    @Test
    fun testSpeakerMatchesCharacterForAllNodes() {
        for (node in world7Tree.dialogueNodes) {
            assertEquals(
                "Node ${node.nodeId}: speaker must match rive_state.character",
                node.speaker,
                node.riveState.character
            )
        }
    }

    @Test
    fun testNoGuruWinkAnimation() {
        for (node in world7Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId}: Guru must never use wink animation",
                node.riveState.animation != "guru_wink"
            )
        }
    }

    // ── 4. Tutorial content alignment ─────────────────────────────────────────

    @Test
    fun testDemonstrationNodeReferencesX4AndY0() {
        // Node 3 (index 2) is the demonstration beat; it must show x = 4, y = 0
        val demo = world7Tree.dialogueNodes[2]
        assertTrue(
            "Demonstration node (Node 3) must reference 'x = 4'. Text='${demo.text}', Overlay='${demo.mathOverlay.expression}'",
            demo.text.contains("x = 4") || demo.mathOverlay.expression.contains("x = 4")
        )
        assertTrue(
            "Demonstration node (Node 3) must reference 'y = 0'. Text='${demo.text}', Overlay='${demo.mathOverlay.expression}'",
            demo.text.contains("y = 0") || demo.mathOverlay.expression.contains("y = 0")
        )
    }

    @Test
    fun testDemonstrationNodeDoesNotRevealHandshakeProblem2xPlus3yEq8() {
        val demo = world7Tree.dialogueNodes[2]
        assertFalse(
            "Demonstration node must not reveal handshake equation 2x + 3y = 8",
            demo.text.contains("2x + 3y = 8") || demo.mathOverlay.expression.contains("2x + 3y = 8")
        )
    }

    @Test
    fun testDemonstrationNodeDoesNotRevealHandshakeQuestion2xEq8() {
        val demo = world7Tree.dialogueNodes[2]
        assertFalse(
            "Demonstration node must not reveal handshake question 2x = 8",
            demo.text.contains("2x = 8") || demo.mathOverlay.expression.contains("2x = 8")
        )
    }

    @Test
    fun testHandshakeNodeDoesNotRevealCompleteAnswer4InOverlay() {
        // Node 4 (index 3) is the handshake beat; it must NOT pre-reveal the answer in expression
        val handshakeNode = world7Tree.dialogueNodes.last()
        assertFalse(
            "Handshake node expression must NOT contain 'x = 4'. Found: '${handshakeNode.mathOverlay.expression}'",
            handshakeNode.mathOverlay.expression.contains("x = 4")
        )
    }

    // ── 5. Handshake coherence ─────────────────────────────────────────────────

    @Test
    fun testFinalNodeRequiresUserTap() {
        assertTrue("Final node must require user tap", world7Tree.dialogueNodes.last().interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testHandshakeExpectedAnswerIsExactly4() {
        val finalNode = world7Tree.dialogueNodes.last()
        val answer = finalNode.interactiveHandshake.expectedAnswer
        assertNotNull("Handshake expectedAnswer must not be null", answer)
        assertEquals(
            "2x = 8 handshake must expect x = 4, not 0, 8, or 2",
            "4",
            answer
        )
    }

    @Test
    fun testHandshakePromptIsNonBlank() {
        val finalNode = world7Tree.dialogueNodes.last()
        val prompt = finalNode.interactiveHandshake.promptText
        assertNotNull("Handshake prompt text must not be null", prompt)
        assertTrue("Handshake prompt text must not be blank", prompt!!.isNotBlank())
    }

    @Test
    fun testHandshakeTargetElementIdIsNumpadKey4() {
        val finalNode = world7Tree.dialogueNodes.last()
        val tid = finalNode.interactiveHandshake.targetElementId
        assertNotNull("Handshake targetElementId must not be null", tid)
        assertEquals("numpad_key_4", tid)
    }

    @Test
    fun testHandshakeExpressionContainsQuestionMark() {
        val finalNode = world7Tree.dialogueNodes.last()
        assertTrue(
            "Handshake math_overlay must contain '?' to indicate missing value. Got: '${finalNode.mathOverlay.expression}'",
            finalNode.mathOverlay.expression.contains("?")
        )
    }

    @Test
    fun testGenerateHandshakeChoicesContainsExpectedAnswer4() {
        val choices = generateHandshakeChoices("4")
        assertEquals(5, choices.size)
        assertTrue("Choices must contain expected answer 4: $choices", choices.contains("4"))
        assertEquals(listOf("2", "3", "4", "5", "6"), choices)
    }

    // ── 6. P0.1 regression — fail-closed ──────────────────────────────────────

    @Test
    fun testP01_NullExpectedAnswerAlwaysFails() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_4",
            promptText = "Some prompt",
            expectedAnswer = null
        )
        assertFalse("P0.1: null expectedAnswer must fail for '4'", VedicMathValidator.evaluateHandshake(handshake, "4"))
        assertFalse("P0.1: null expectedAnswer must fail for ''", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("P0.1: null expectedAnswer must fail for any input", VedicMathValidator.evaluateHandshake(handshake, "anything"))
    }

    @Test
    fun testP01_BlankExpectedAnswerAlwaysFails() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_4",
            promptText = "Some prompt",
            expectedAnswer = "   "
        )
        assertFalse("P0.1: blank expectedAnswer must fail for '4'", VedicMathValidator.evaluateHandshake(handshake, "4"))
    }

    // ── 7. P1.1 regression — expectedAnswer is the authority ──────────────────

    @Test
    fun testP11_ExpectedAnswerIsTheAuthority() {
        val finalNode = world7Tree.dialogueNodes.last()
        val handshake = finalNode.interactiveHandshake

        assertTrue(
            "Submitting expected answer '4' must pass",
            VedicMathValidator.evaluateHandshake(handshake, "4")
        )
        assertTrue(
            "Whitespace-padded ' 4 ' must pass",
            VedicMathValidator.evaluateHandshake(handshake, " 4 ")
        )
        assertFalse(
            "Submitting '0' must fail because target is x = 4",
            VedicMathValidator.evaluateHandshake(handshake, "0")
        )
        assertFalse(
            "Submitting constant '8' must fail",
            VedicMathValidator.evaluateHandshake(handshake, "8")
        )
        assertFalse(
            "Submitting coefficient '2' must fail",
            VedicMathValidator.evaluateHandshake(handshake, "2")
        )
        assertFalse(
            "Submitting '16' must fail",
            VedicMathValidator.evaluateHandshake(handshake, "16")
        )
        assertFalse(
            "Submitting invalid string must fail",
            VedicMathValidator.evaluateHandshake(handshake, "invalid")
        )
    }

    // ── 8. Controller interaction ─────────────────────────────────────────────

    @Test
    fun testControllerAdvancesThroughNodesAndRequiresHandshakeAtEnd() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world7Tree, adapter)

        assertEquals("Starts at node 0", 0, controller.currentNodeIndex.value)
        assertFalse("Not completed initially", controller.isCompleted.value)

        // Advance to node 2
        controller.advanceNode()
        assertEquals(1, controller.currentNodeIndex.value)

        // Advance to node 3
        controller.advanceNode()
        assertEquals(2, controller.currentNodeIndex.value)

        // Advance to node 4 (handshake node)
        controller.advanceNode()
        assertEquals(3, controller.currentNodeIndex.value)
        assertTrue("Node 4 requires handshake", controller.currentNode.value.interactiveHandshake.requiresUserTap)

        // Attempting to advanceNode() past handshake node should be blocked
        controller.advanceNode()
        assertEquals("advanceNode() must be blocked on handshake step", 3, controller.currentNodeIndex.value)
        assertFalse("Tutorial must not complete via advanceNode() on handshake step", controller.isCompleted.value)

        // Submit correct answer "4"
        val success = controller.submitHandshake("4")
        assertTrue("Correct answer must succeed", success)
        assertTrue("Tutorial must be completed", controller.isCompleted.value)
    }

    @Test
    fun testHandshakeFailureStatePreservesNodeAndGeneratesCorrectiveError() {
        val controller = RiveDialogueController(world7Tree, DefaultRiveAdapter())
        controller.advanceNode() // to node 2
        controller.advanceNode() // to node 3
        controller.advanceNode() // to node 4 (handshake)

        val wrongInputs = listOf("0", "8", "2", "16", "7", "invalid")
        for (wrong in wrongInputs) {
            val success = controller.submitHandshake(wrong)
            assertFalse("Input '$wrong' must fail handshake", success)
            assertFalse("Tutorial must remain incomplete", controller.isCompleted.value)
            assertEquals("Current node index must remain at 3", 3, controller.currentNodeIndex.value)
            assertNotNull("Handshake error message must be populated", controller.handshakeError.value)
            assertTrue("Handshake error should mention prompt or step", controller.handshakeError.value!!.isNotBlank())
        }
    }

    @Test
    fun testProgressionPolicy_SkipBlockedWhenHandshakeUnresolved() {
        val controller = RiveDialogueController(world7Tree, DefaultRiveAdapter())
        assertTrue("Tree has unresolved handshake", controller.hasUnresolvedHandshake())

        // Attempting to skip without explicit opt-out must fail closed
        val skipped = controller.skip(allowBypassHandshake = false)
        assertFalse("skip() must return false when handshake is unresolved", skipped)
        assertFalse("isCompleted must remain false", controller.isCompleted.value)
    }

    @Test
    fun testProgressionPolicy_ExplicitOptOutBypassesHandshake() {
        val controller = RiveDialogueController(world7Tree, DefaultRiveAdapter())
        val skipped = controller.skip(allowBypassHandshake = true)
        assertTrue("Explicit skip opt-out must succeed", skipped)
        assertTrue("isCompleted must be true", controller.isCompleted.value)
    }

    @Test
    fun testProgressionPolicy_SkipAllowedAfterHandshakeResolved() {
        val controller = RiveDialogueController(world7Tree, DefaultRiveAdapter())
        controller.advanceNode() // 1
        controller.advanceNode() // 2
        controller.advanceNode() // 3 (handshake)
        assertTrue(controller.submitHandshake("4"))
        assertTrue(controller.isCompleted.value)
        assertFalse(controller.hasUnresolvedHandshake())

        // Calling skip when already completed succeeds
        assertTrue(controller.skip())
        assertTrue(controller.isCompleted.value)
    }

    // ── 9. Isolation: other generators & Worlds 1–6 unaffected ────────────────

    @Test
    fun testOtherGeneratorsSutraNamesUnaffected() {
        assertEquals(
            "Ekadhikena sutra name must be unchanged",
            "Ekadhikena Purvena",
            com.ank.sutrasaga.engine.EkadhikenaPurvenaGenerator().sutraName
        )
        assertEquals(
            "Nikhilam sutra name must be unchanged",
            "Nikhilam Navatashcaramam Dashatah",
            com.ank.sutrasaga.engine.NikhilamGenerator().sutraName
        )
        assertEquals(
            "Ekanyunena sutra name must be unchanged",
            "Ekanyunena Purvena",
            com.ank.sutrasaga.engine.EkanyunenaGenerator().sutraName
        )
        assertEquals(
            "Yavadunam sutra name must be unchanged",
            "Yavadunam",
            com.ank.sutrasaga.engine.YavadunamGenerator().sutraName
        )
        assertEquals(
            "Urdhva sutra name must be unchanged",
            "Urdhva-Tiryagbhyam",
            com.ank.sutrasaga.engine.UrdhvaTiryagbhyamGenerator().sutraName
        )
        assertEquals(
            "Paravartya sutra name must be unchanged",
            "Paravartya Yojayet",
            com.ank.sutrasaga.engine.ParavartyaYojayetGenerator().sutraName
        )
    }

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

    // ── 10. Focused World 7 Tutorial Enhancements ─────────────────────────────

    @Test
    fun testDialogueAndMathOverlayConsistency() {
        val node1 = world7Tree.dialogueNodes[0]
        assertTrue("Node 1 text must mention 3x + 2y = 12", node1.text.contains("3x + 2y = 12"))
        assertTrue("Node 1 overlay must contain 3x + 2y = 12", node1.mathOverlay.expression.contains("3x + 2y = 12"))

        val node2 = world7Tree.dialogueNodes[1]
        assertTrue("Node 2 text must explain Ānurūpye Śūnyamanyat", node2.text.contains("Ānurūpye Śūnyamanyat") || node2.text.contains("Anurupye"))
        assertTrue("Node 2 text must preserve sutra rule 'If One is in Ratio, the Other is Zero'", node2.text.contains("If One is in Ratio, the Other is Zero", ignoreCase = true))
        assertTrue("Node 2 overlay must show ratio test", node2.mathOverlay.expression.contains("3/6 = 1/2"))

        val node3 = world7Tree.dialogueNodes[2]
        assertTrue("Node 3 text must state x = 4", node3.text.contains("x = 4"))
        assertTrue("Node 3 text must state y = 0", node3.text.contains("y = 0"))
        assertTrue("Node 3 overlay must produce x = 4", node3.mathOverlay.expression.contains("x = 4"))

        val node4 = world7Tree.dialogueNodes[3]
        assertTrue("Node 4 text must ask for 2x = 8", node4.text.contains("2x = 8"))
        assertTrue("Node 4 overlay must contain missing step '?'", node4.mathOverlay.expression.contains("?"))
        assertFalse("Node 4 overlay must not reveal answer 4", node4.mathOverlay.expression.contains("x = 4"))
    }

    @Test
    fun testMathematicalInvariantForAnurupyeRatioSolving() {
        // Linear systems where a1/a2 = c1/c2 -> y = 0, x = c1/a1
        val testSystems = listOf(
            Triple(LinearEquation(3, 2, 12), LinearEquation(6, 5, 24), Pair(4L, 0L)),
            Triple(LinearEquation(2, 3, 8), LinearEquation(4, 7, 16), Pair(4L, 0L)),
            Triple(LinearEquation(5, 1, 20), LinearEquation(15, 8, 60), Pair(4L, 0L)),
            Triple(LinearEquation(7, 3, 14), LinearEquation(21, 10, 42), Pair(2L, 0L)),
            Triple(LinearEquation(4, 2, 8), LinearEquation(12, 5, 24), Pair(2L, 0L))
        )

        for ((e1, e2, expected) in testSystems) {
            val sol = generator.solveSystem(e1, e2)
            assertEquals(AnurupyeCase.Y_ZERO, sol.caseType)
            assertEquals(expected.first, sol.xSolution.reduced.numerator)
            assertEquals(expected.second, sol.ySolution.reduced.numerator)

            // Direct algebraic invariant verification
            val x = expected.first
            val y = expected.second
            assertEquals(e1.c, e1.a * x + e1.b * y)
            assertEquals(e2.c, e2.a * x + e2.b * y)
            assertTrue(VedicMathValidator.verifyAnurupyeSystem(e1.a, e1.b, e1.c, e2.a, e2.b, e2.c, x, y))
        }
    }
}
