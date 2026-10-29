package com.ank.sutrasaga.engine

import com.ank.sutrasaga.data.repository.RiveDialogueRepository
import com.ank.sutrasaga.domain.models.DifficultyTier
import com.ank.sutrasaga.domain.models.InteractiveHandshake
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
 * P1 World 3 Tutorial Tests — Ekanyūnena Pūrveṇa (Refined)
 *
 * Covers:
 * - Four-beat dialogue tree structure and schema compliance
 * - Sutra name and Sanskrit meaning correctness ("By One Less Than the Previous One")
 * - 47 × 99 demonstration produces 4653 (LHS: 47 - 1 = 46, RHS: 99 - 46 = 53)
 * - Additional math verification: 46 × 99 = 4554, 743 × 999 = 742257, 53 × 99 = 5247
 * - 53 × 99 handshake: expected answer is "52" (LHS: 53 - 1 = 52), not "5247" or "53"
 * - Handshake expression does NOT reveal complete product 5247
 * - P0.1 regression: null/blank expectedAnswer fails closed
 * - P1.1 regression: submitting 5247 or 53 fails when expected is 52
 * - Controller: incorrect answer does NOT advance tutorial
 * - Controller: correct answer "52" completes tutorial
 * - Other world generator sutra names are unchanged
 */
@RunWith(RobolectricTestRunner::class)
class EkanyunenaWorld3TutorialTest {

    private lateinit var generator: EkanyunenaGenerator
    private lateinit var world3Tree: RiveSutraDialogueTree
    private val repository = RiveDialogueRepository()

    @Before
    fun setUp() {
        generator = EkanyunenaGenerator(Random(42))

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
        val loaded = repository.getDialogueForWorld(3)
        assertNotNull("World 3 dialogue tree must exist in asset file", loaded)
        world3Tree = loaded!!
    }

    // ── 1. Sutra identity ──────────────────────────────────────────────────────

    @Test
    fun testSutraNameIsEkanyunenaPurvena() {
        assertEquals("Ekanyunena Purvena", generator.sutraName)
    }

    @Test
    fun testDialogueTreeSutraId() {
        assertEquals("ekanyunena_purvena", world3Tree.sutraId)
    }

    @Test
    fun testDialogueTreeEnglishMeaningRefersToOneLess() {
        assertTrue(
            "English meaning must reference 'One Less': '${world3Tree.englishMeaning}'",
            world3Tree.englishMeaning.contains("One Less", ignoreCase = true)
        )
    }

    @Test
    fun testDialogueTreeWorldNumber() {
        assertEquals(3, world3Tree.worldNumber)
    }

    // ── 2. Mathematical correctness ────────────────────────────────────────────

    @Test
    fun testCanonicalTutorialProblem47Times99Produces4653() {
        val problem = generator.generateCanonicalTutorialProblem()
        assertEquals(47L, problem.operand)
        assertEquals(46L, problem.prefixPart)
        assertEquals("53", problem.appendedSuffix)
        assertEquals(4653L, problem.correctAnswer)

        // Verify independent arithmetic
        assertEquals(4653L, 47L * 99L)
        assertTrue(
            "VedicMathValidator must verify Ekanyunena multiplication",
            VedicMathValidator.verifyEkanyunenaMultiplication(47L, 99L, 4653L)
        )

        // Verify decomposition steps structure (3 steps)
        assertEquals(3, problem.decompositionSteps.size)
        assertEquals("46", problem.decompositionSteps[0].stepResult)
        assertEquals("53", problem.decompositionSteps[1].stepResult)
        assertEquals("4653", problem.decompositionSteps[2].stepResult)
    }

    @Test
    fun test46Times99Produces4554() {
        val p = generator.generateSpecificProblem(46L)
        assertEquals(4554L, p.correctAnswer)
        assertEquals(46L * 99L, p.correctAnswer)
        assertEquals("45", p.decompositionSteps[0].stepResult)
        assertEquals("54", p.decompositionSteps[1].stepResult)
        assertEquals("4554", p.decompositionSteps[2].stepResult)
    }

    @Test
    fun test743Times999Produces742257() {
        val p = generator.generateSpecificProblem(743L)
        assertEquals(742257L, p.correctAnswer)
        assertEquals(743L * 999L, p.correctAnswer)
        assertEquals("742", p.decompositionSteps[0].stepResult)
        assertEquals("257", p.decompositionSteps[1].stepResult)
        assertEquals("742257", p.decompositionSteps[2].stepResult)
    }

    @Test
    fun testHandshakeProblem53Times99Produces5247() {
        val p = generator.generateSpecificProblem(53L)
        assertEquals(5247L, p.correctAnswer)
        assertEquals(53L * 99L, p.correctAnswer)
        // LHS: 53 - 1 = 52
        assertEquals("52", p.decompositionSteps[0].stepResult)
        // RHS: 99 - 52 = 47
        assertEquals("47", p.decompositionSteps[1].stepResult)
        // Concatenation: 5247
        assertEquals("5247", p.decompositionSteps[2].stepResult)
    }

    // ── 3. Dialogue tree structure ─────────────────────────────────────────────

    @Test
    fun testWorld3HasExactlyFourNodes() {
        assertEquals(
            "World 3 tutorial must have exactly 4 nodes",
            4,
            world3Tree.dialogueNodes.size
        )
    }

    @Test
    fun testWorld3PassesRiveDialogueValidator() {
        val result = RiveDialogueValidator.validate(world3Tree)
        assertTrue("World 3 must pass validator: ${result.errors}", result.isValid)
        assertTrue("Validation errors must be empty", result.errors.isEmpty())
    }

    @Test
    fun testAllNodeTextsNonBlank() {
        for (node in world3Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} text must not be blank", node.text.isNotBlank())
        }
    }

    @Test
    fun testAllMathOverlayExpressionsNonBlank() {
        for (node in world3Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId} math_overlay.expression must not be blank",
                node.mathOverlay.expression.isNotBlank()
            )
        }
    }

    @Test
    fun testAllNodesUseValidAnimations() {
        for (node in world3Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId}: animation '${node.riveState.animation}' must be in whitelist for ${node.riveState.character}",
                RiveAnimationConfig.isValidAnimation(node.riveState.character, node.riveState.animation)
            )
        }
    }

    @Test
    fun testSpeakerMatchesCharacterForAllNodes() {
        for (node in world3Tree.dialogueNodes) {
            assertEquals(
                "Node ${node.nodeId}: speaker must match rive_state.character",
                node.speaker,
                node.riveState.character
            )
        }
    }

    @Test
    fun testNoGuruWinkAnimation() {
        for (node in world3Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId}: Guru must never use wink animation",
                node.riveState.animation != "guru_wink"
            )
        }
    }

    // ── 4. Tutorial content alignment ─────────────────────────────────────────

    @Test
    fun testDemonstrationNodeReferences4653() {
        // Node 3 (index 2) is the demonstration beat; it must show 4653
        val demo = world3Tree.dialogueNodes[2]
        assertTrue(
            "Demonstration node (Node 3) must reference '4653'. Text='${demo.text}', Overlay='${demo.mathOverlay.expression}'",
            demo.text.contains("4653") || demo.mathOverlay.expression.contains("4653")
        )
    }

    @Test
    fun testDemonstrationNodeDoesNotRevealHandshakeOperand53() {
        val demo = world3Tree.dialogueNodes[2]
        assertFalse(
            "Demonstration node must not reveal handshake operand 53 × 99",
            demo.text.contains("53 × 99") || demo.mathOverlay.expression.contains("53 × 99")
        )
    }

    @Test
    fun testDemonstrationNodeDoesNotRevealHandshakeAnswer52() {
        val demo = world3Tree.dialogueNodes[2]
        assertFalse(
            "Demonstration node must not reveal handshake answer '52'",
            demo.text.contains("53 − 1 = 52") || demo.mathOverlay.expression.contains("53 − 1")
        )
    }

    @Test
    fun testHandshakeNodeDoesNotRevealCompleteProduct5247() {
        // Node 4 (index 3) is the handshake beat; it must NOT pre-reveal the complete product
        val handshakeNode = world3Tree.dialogueNodes.last()
        assertFalse(
            "Handshake node expression must NOT contain '5247'. Found: '${handshakeNode.mathOverlay.expression}'",
            handshakeNode.mathOverlay.expression.contains("5247")
        )
    }

    // ── 5. Handshake coherence ─────────────────────────────────────────────────

    @Test
    fun testFinalNodeRequiresUserTap() {
        assertTrue("Final node must require user tap", world3Tree.dialogueNodes.last().interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testHandshakeExpectedAnswerIsExactly52() {
        val finalNode = world3Tree.dialogueNodes.last()
        val answer = finalNode.interactiveHandshake.expectedAnswer
        assertNotNull("Handshake expectedAnswer must not be null", answer)
        assertEquals(
            "53 × 99 handshake must expect left part 53-1=52, not 5247 or 53",
            "52",
            answer
        )
    }

    @Test
    fun testHandshakePromptIsNonBlank() {
        val finalNode = world3Tree.dialogueNodes.last()
        val prompt = finalNode.interactiveHandshake.promptText
        assertNotNull("Handshake prompt text must not be null", prompt)
        assertTrue("Handshake prompt text must not be blank", prompt!!.isNotBlank())
    }

    @Test
    fun testHandshakeTargetElementIdIsNumpadKey52() {
        val finalNode = world3Tree.dialogueNodes.last()
        val tid = finalNode.interactiveHandshake.targetElementId
        assertNotNull("Handshake targetElementId must not be null", tid)
        assertEquals("numpad_key_52", tid)
    }

    @Test
    fun testHandshakeExpressionContainsQuestionMark() {
        val finalNode = world3Tree.dialogueNodes.last()
        assertTrue(
            "Handshake math_overlay must contain '?' to indicate missing value. Got: '${finalNode.mathOverlay.expression}'",
            finalNode.mathOverlay.expression.contains("?")
        )
    }

    @Test
    fun testGenerateHandshakeChoicesContainsExpectedAnswer52() {
        val choices = generateHandshakeChoices("52")
        assertEquals(5, choices.size)
        assertTrue("Choices must contain expected answer 52: $choices", choices.contains("52"))
        assertEquals(listOf("50", "51", "52", "53", "54"), choices)
    }

    // ── 6. P0.1 regression — fail-closed ──────────────────────────────────────

    @Test
    fun testP01_NullExpectedAnswerAlwaysFails() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_52",
            promptText = "Some prompt",
            expectedAnswer = null
        )
        assertFalse("P0.1: null expectedAnswer must fail for '52'", VedicMathValidator.evaluateHandshake(handshake, "52"))
        assertFalse("P0.1: null expectedAnswer must fail for ''", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("P0.1: null expectedAnswer must fail for any input", VedicMathValidator.evaluateHandshake(handshake, "anything"))
    }

    @Test
    fun testP01_BlankExpectedAnswerAlwaysFails() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_52",
            promptText = "Some prompt",
            expectedAnswer = "   "
        )
        assertFalse("P0.1: blank expectedAnswer must fail for '52'", VedicMathValidator.evaluateHandshake(handshake, "52"))
    }

    // ── 7. P1.1 regression — expectedAnswer is the authority ──────────────────

    @Test
    fun testP11_ExpectedAnswerIsTheAuthority() {
        val finalNode = world3Tree.dialogueNodes.last()
        val handshake = finalNode.interactiveHandshake

        assertTrue(
            "Submitting expected answer '52' must pass",
            VedicMathValidator.evaluateHandshake(handshake, "52")
        )
        assertTrue(
            "Whitespace-padded ' 52 ' must pass",
            VedicMathValidator.evaluateHandshake(handshake, " 52 ")
        )
        assertFalse(
            "Submitting complete product '5247' must fail because target is left part '52'",
            VedicMathValidator.evaluateHandshake(handshake, "5247")
        )
        assertFalse(
            "Submitting multiplicand '53' must fail",
            VedicMathValidator.evaluateHandshake(handshake, "53")
        )
        assertFalse(
            "Submitting '0' must fail",
            VedicMathValidator.evaluateHandshake(handshake, "0")
        )
    }

    // ── 8. Controller interaction ─────────────────────────────────────────────

    @Test
    fun testControllerAdvancesThroughNodesAndRequiresHandshakeAtEnd() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world3Tree, adapter)

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

        // Submit correct answer "52"
        val success = controller.submitHandshake("52")
        assertTrue("Correct answer must succeed", success)
        assertTrue("Tutorial must be completed", controller.isCompleted.value)
    }

    @Test
    fun testHandshakeFailureStatePreservesNodeAndGeneratesCorrectiveError() {
        val controller = RiveDialogueController(world3Tree, DefaultRiveAdapter())
        controller.advanceNode() // to node 2
        controller.advanceNode() // to node 3
        controller.advanceNode() // to node 4 (handshake)

        val wrongInputs = listOf("5247", "53", "0", "42", "invalid")
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
        val controller = RiveDialogueController(world3Tree, DefaultRiveAdapter())
        assertTrue("Tree has unresolved handshake", controller.hasUnresolvedHandshake())

        // Attempting to skip without explicit opt-out must fail closed
        val skipped = controller.skip(allowBypassHandshake = false)
        assertFalse("skip() must return false when handshake is unresolved", skipped)
        assertFalse("isCompleted must remain false", controller.isCompleted.value)
    }

    @Test
    fun testProgressionPolicy_ExplicitOptOutBypassesHandshake() {
        val controller = RiveDialogueController(world3Tree, DefaultRiveAdapter())
        val skipped = controller.skip(allowBypassHandshake = true)
        assertTrue("Explicit skip opt-out must succeed", skipped)
        assertTrue("isCompleted must be true", controller.isCompleted.value)
    }

    @Test
    fun testProgressionPolicy_SkipAllowedAfterHandshakeResolved() {
        val controller = RiveDialogueController(world3Tree, DefaultRiveAdapter())
        controller.advanceNode() // 1
        controller.advanceNode() // 2
        controller.advanceNode() // 3 (handshake)
        assertTrue(controller.submitHandshake("52"))
        assertTrue(controller.isCompleted.value)
        assertFalse(controller.hasUnresolvedHandshake())

        // Calling skip when already completed succeeds
        assertTrue(controller.skip())
        assertTrue(controller.isCompleted.value)
    }

    // ── 9. Isolation: other generators & World 1 / World 2 unaffected ──────────

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
            "Yavadunam sutra name must be unchanged",
            "Yavadunam",
            com.ank.sutrasaga.engine.YavadunamGenerator().sutraName
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

    // ── 10. Focused World 3 Tutorial Enhancements ─────────────────────────────

    @Test
    fun testDialogueAndMathOverlayConsistency() {
        val node1 = world3Tree.dialogueNodes[0]
        assertTrue("Node 1 text must mention 47 × 99", node1.text.contains("47 × 99"))
        assertTrue("Node 1 overlay must contain 47 × 99", node1.mathOverlay.expression.contains("47 × 99"))

        val node2 = world3Tree.dialogueNodes[1]
        assertTrue("Node 2 text must explain Ekanyūnena Pūrveṇa", node2.text.contains("Ekanyūnena Pūrveṇa") || node2.text.contains("Ekanyunena"))
        assertTrue("Node 2 text must preserve sutra rule 'By One Less'", node2.text.contains("By One Less", ignoreCase = true))
        assertTrue("Node 2 overlay must show decomposition formula", node2.mathOverlay.expression.contains("(47 − 1) | (99 − 46)"))

        val node3 = world3Tree.dialogueNodes[2]
        assertTrue("Node 3 text must state 4653", node3.text.contains("4653"))
        assertTrue("Node 3 overlay must produce 4653", node3.mathOverlay.expression.contains("4653"))

        val node4 = world3Tree.dialogueNodes[3]
        assertTrue("Node 4 text must ask for 53 × 99", node4.text.contains("53 × 99"))
        assertTrue("Node 4 text must ask for 53 − 1", node4.text.contains("53 − 1"))
        assertTrue("Node 4 overlay must contain missing step '?'", node4.mathOverlay.expression.contains("?"))
        assertFalse("Node 4 overlay must not reveal 5247", node4.mathOverlay.expression.contains("5247"))
    }

    @Test
    fun testMathematicalInvariantForEkanyunenaMultiplication() {
        // For numbers M multiplied by an equal number of 9s (Nines = 10^k - 1):
        // M * Nines = (M - 1) * 10^k + (Nines - (M - 1))
        val testCases = listOf(
            Pair(12L, 99L),
            Pair(47L, 99L),
            Pair(53L, 99L),
            Pair(89L, 99L),
            Pair(125L, 999L),
            Pair(743L, 999L),
            Pair(856L, 999L),
            Pair(1234L, 9999L),
            Pair(6789L, 9999L)
        )

        for ((multiplicand, nines) in testCases) {
            val lhs = multiplicand - 1L
            val rhs = nines - lhs
            val multiplier = nines + 1L
            val expectedProduct = lhs * multiplier + rhs
            val actualProduct = multiplicand * nines

            assertEquals(
                "Invariant failure for multiplicand $multiplicand and nines $nines",
                actualProduct,
                expectedProduct
            )
            val problem = generator.generateSpecificProblem(multiplicand)
            assertEquals(actualProduct, problem.correctAnswer)
        }
    }
}
