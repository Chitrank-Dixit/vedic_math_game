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
 * P1 World 4 Tutorial Tests — Yāvadūnam (Refined)
 *
 * Covers:
 * - Four-beat dialogue tree structure and schema compliance
 * - Sutra name and meaning correctness ("By the Deficiency")
 * - 94² demonstration produces 8836 (Deficiency 6, LHS: 94 - 6 = 88, RHS: 6² = 36)
 * - Additional math verification: 97² = 9409, 88² = 7744, 994² = 988036
 * - 97² handshake: expected answer is "94" (LHS: 97 - 3 = 94), not "9409" or "97"
 * - Handshake expression does NOT reveal complete product 9409
 * - P0.1 regression: null/blank expectedAnswer fails closed
 * - P1.1 regression: submitting 9409 or 97 fails when expected is 94
 * - Controller: incorrect answer does NOT advance tutorial
 * - Controller: correct answer "94" completes tutorial
 * - Other world generator sutra names are unchanged (Worlds 1, 2, 3)
 */
@RunWith(RobolectricTestRunner::class)
class YavadunamWorld4TutorialTest {

    private lateinit var generator: YavadunamGenerator
    private lateinit var world4Tree: RiveSutraDialogueTree
    private val repository = RiveDialogueRepository()

    @Before
    fun setUp() {
        generator = YavadunamGenerator(Random(42))

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
        val loaded = repository.getDialogueForWorld(4)
        assertNotNull("World 4 dialogue tree must exist in asset file", loaded)
        world4Tree = loaded!!
    }

    // ── 1. Sutra identity ──────────────────────────────────────────────────────

    @Test
    fun testSutraNameIsYavadunam() {
        assertEquals("Yavadunam", generator.sutraName)
    }

    @Test
    fun testDialogueTreeSutraId() {
        assertEquals("yavadunam", world4Tree.sutraId)
    }

    @Test
    fun testDialogueTreeEnglishMeaningRefersToDeficiency() {
        assertTrue(
            "English meaning must reference 'Deficiency': '${world4Tree.englishMeaning}'",
            world4Tree.englishMeaning.contains("Deficiency", ignoreCase = true)
        )
    }

    @Test
    fun testDialogueTreeWorldNumber() {
        assertEquals(4, world4Tree.worldNumber)
    }

    // ── 2. Mathematical correctness ────────────────────────────────────────────

    @Test
    fun testCanonicalTutorialProblem94SquaredProduces8836() {
        val problem = generator.generateCanonicalTutorialProblem()
        assertEquals(94L, problem.operand)
        assertEquals(8836L, problem.correctAnswer)
        assertEquals(6L, problem.prefixPart) // Deficiency
        assertEquals("36", problem.appendedSuffix) // RHS string

        // Verify independent arithmetic
        assertEquals(8836L, 94L * 94L)
        assertTrue(
            "VedicMathValidator must verify Yavadunam squaring",
            VedicMathValidator.verifyYavadunamSquaring(94L, 100L, 8836L)
        )

        // Verify decomposition steps structure (4 steps)
        assertEquals(4, problem.decompositionSteps.size)
        assertEquals("6", problem.decompositionSteps[0].stepResult) // Deficiency D = 6
        assertEquals("88", problem.decompositionSteps[1].stepResult) // LHS = 94 - 6 = 88
        assertEquals("36", problem.decompositionSteps[2].stepResult) // RHS = 6² = 36
        assertEquals("8836", problem.decompositionSteps[3].stepResult) // Concatenation
    }

    @Test
    fun test97SquaredProduces9409() {
        val p = generator.generateSpecificProblem(97L)
        assertEquals(9409L, p.correctAnswer)
        assertEquals(97L * 97L, p.correctAnswer)
        assertEquals("3", p.decompositionSteps[0].stepResult) // Deficiency
        assertEquals("94", p.decompositionSteps[1].stepResult) // LHS
        assertEquals("09", p.decompositionSteps[2].stepResult) // RHS
        assertEquals("9409", p.decompositionSteps[3].stepResult)
    }

    @Test
    fun test88SquaredWithCarryProduces7744() {
        val p = generator.generateSpecificProblem(88L)
        assertEquals(7744L, p.correctAnswer)
        assertEquals(88L * 88L, p.correctAnswer)
        assertEquals("12", p.decompositionSteps[0].stepResult)
        assertEquals("76", p.decompositionSteps[1].stepResult)
        assertEquals("44", p.decompositionSteps[2].stepResult)
        assertEquals("7744", p.decompositionSteps[3].stepResult)
    }

    @Test
    fun test994SquaredNearBase1000Produces988036() {
        val p = generator.generateSpecificProblem(994L)
        assertEquals(988036L, p.correctAnswer)
        assertEquals(994L * 994L, p.correctAnswer)
        assertEquals("6", p.decompositionSteps[0].stepResult)
        assertEquals("988", p.decompositionSteps[1].stepResult)
        assertEquals("036", p.decompositionSteps[2].stepResult)
        assertEquals("988036", p.decompositionSteps[3].stepResult)
    }

    // ── 3. Dialogue tree structure ─────────────────────────────────────────────

    @Test
    fun testWorld4HasExactlyFourNodes() {
        assertEquals(
            "World 4 tutorial must have exactly 4 nodes",
            4,
            world4Tree.dialogueNodes.size
        )
    }

    @Test
    fun testWorld4PassesRiveDialogueValidator() {
        val result = RiveDialogueValidator.validate(world4Tree)
        assertTrue("World 4 must pass validator: ${result.errors}", result.isValid)
        assertTrue("Validation errors must be empty", result.errors.isEmpty())
    }

    @Test
    fun testAllNodeTextsNonBlank() {
        for (node in world4Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} text must not be blank", node.text.isNotBlank())
        }
    }

    @Test
    fun testAllMathOverlayExpressionsNonBlank() {
        for (node in world4Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId} math_overlay.expression must not be blank",
                node.mathOverlay.expression.isNotBlank()
            )
        }
    }

    @Test
    fun testAllNodesUseValidAnimations() {
        for (node in world4Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId}: animation '${node.riveState.animation}' must be in whitelist for ${node.riveState.character}",
                RiveAnimationConfig.isValidAnimation(node.riveState.character, node.riveState.animation)
            )
        }
    }

    @Test
    fun testSpeakerMatchesCharacterForAllNodes() {
        for (node in world4Tree.dialogueNodes) {
            assertEquals(
                "Node ${node.nodeId}: speaker must match rive_state.character",
                node.speaker,
                node.riveState.character
            )
        }
    }

    @Test
    fun testNoGuruWinkAnimation() {
        for (node in world4Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId}: Guru must never use wink animation",
                node.riveState.animation != "guru_wink"
            )
        }
    }

    // ── 4. Tutorial content alignment ─────────────────────────────────────────

    @Test
    fun testDemonstrationNodeReferences8836() {
        // Node 3 (index 2) is the demonstration beat; it must show 8836
        val demo = world4Tree.dialogueNodes[2]
        assertTrue(
            "Demonstration node (Node 3) must reference '8836'. Text='${demo.text}', Overlay='${demo.mathOverlay.expression}'",
            demo.text.contains("8836") || demo.mathOverlay.expression.contains("8836")
        )
    }

    @Test
    fun testDemonstrationNodeDoesNotRevealHandshakeOperand97() {
        val demo = world4Tree.dialogueNodes[2]
        assertFalse(
            "Demonstration node must not reveal handshake operand 97²",
            demo.text.contains("97²") || demo.mathOverlay.expression.contains("97²")
        )
    }

    @Test
    fun testDemonstrationNodeDoesNotRevealHandshakeAnswer94() {
        val demo = world4Tree.dialogueNodes[2]
        assertFalse(
            "Demonstration node must not reveal handshake answer '94'",
            demo.text.contains("97 − 3 = 94") || demo.mathOverlay.expression.contains("97 − 3")
        )
    }

    @Test
    fun testHandshakeNodeDoesNotRevealCompleteProduct9409() {
        // Node 4 (index 3) is the handshake beat; it must NOT pre-reveal the complete product
        val handshakeNode = world4Tree.dialogueNodes.last()
        assertFalse(
            "Handshake node expression must NOT contain '9409'. Found: '${handshakeNode.mathOverlay.expression}'",
            handshakeNode.mathOverlay.expression.contains("9409")
        )
    }

    // ── 5. Handshake coherence ─────────────────────────────────────────────────

    @Test
    fun testFinalNodeRequiresUserTap() {
        assertTrue("Final node must require user tap", world4Tree.dialogueNodes.last().interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testHandshakeExpectedAnswerIsExactly94() {
        val finalNode = world4Tree.dialogueNodes.last()
        val answer = finalNode.interactiveHandshake.expectedAnswer
        assertNotNull("Handshake expectedAnswer must not be null", answer)
        assertEquals(
            "97² handshake must expect left part 97-3=94, not 9409 or 97",
            "94",
            answer
        )
    }

    @Test
    fun testHandshakePromptIsNonBlank() {
        val finalNode = world4Tree.dialogueNodes.last()
        val prompt = finalNode.interactiveHandshake.promptText
        assertNotNull("Handshake prompt text must not be null", prompt)
        assertTrue("Handshake prompt text must not be blank", prompt!!.isNotBlank())
    }

    @Test
    fun testHandshakeTargetElementIdIsNumpadKey94() {
        val finalNode = world4Tree.dialogueNodes.last()
        val tid = finalNode.interactiveHandshake.targetElementId
        assertNotNull("Handshake targetElementId must not be null", tid)
        assertEquals("numpad_key_94", tid)
    }

    @Test
    fun testHandshakeExpressionContainsQuestionMark() {
        val finalNode = world4Tree.dialogueNodes.last()
        assertTrue(
            "Handshake math_overlay must contain '?' to indicate missing value. Got: '${finalNode.mathOverlay.expression}'",
            finalNode.mathOverlay.expression.contains("?")
        )
    }

    @Test
    fun testGenerateHandshakeChoicesContainsExpectedAnswer94() {
        val choices = generateHandshakeChoices("94")
        assertEquals(5, choices.size)
        assertTrue("Choices must contain expected answer 94: $choices", choices.contains("94"))
        assertEquals(listOf("92", "93", "94", "95", "96"), choices)
    }

    // ── 6. P0.1 regression — fail-closed ──────────────────────────────────────

    @Test
    fun testP01_NullExpectedAnswerAlwaysFails() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_94",
            promptText = "Some prompt",
            expectedAnswer = null
        )
        assertFalse("P0.1: null expectedAnswer must fail for '94'", VedicMathValidator.evaluateHandshake(handshake, "94"))
        assertFalse("P0.1: null expectedAnswer must fail for ''", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("P0.1: null expectedAnswer must fail for any input", VedicMathValidator.evaluateHandshake(handshake, "anything"))
    }

    @Test
    fun testP01_BlankExpectedAnswerAlwaysFails() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_94",
            promptText = "Some prompt",
            expectedAnswer = "   "
        )
        assertFalse("P0.1: blank expectedAnswer must fail for '94'", VedicMathValidator.evaluateHandshake(handshake, "94"))
    }

    // ── 7. P1.1 regression — expectedAnswer is the authority ──────────────────

    @Test
    fun testP11_ExpectedAnswerIsTheAuthority() {
        val finalNode = world4Tree.dialogueNodes.last()
        val handshake = finalNode.interactiveHandshake

        assertTrue(
            "Submitting expected answer '94' must pass",
            VedicMathValidator.evaluateHandshake(handshake, "94")
        )
        assertTrue(
            "Whitespace-padded ' 94 ' must pass",
            VedicMathValidator.evaluateHandshake(handshake, " 94 ")
        )
        assertFalse(
            "Submitting complete product '9409' must fail because target is left part '94'",
            VedicMathValidator.evaluateHandshake(handshake, "9409")
        )
        assertFalse(
            "Submitting original operand '97' must fail",
            VedicMathValidator.evaluateHandshake(handshake, "97")
        )
        assertFalse(
            "Submitting deficiency '3' must fail",
            VedicMathValidator.evaluateHandshake(handshake, "3")
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
        val controller = RiveDialogueController(world4Tree, adapter)

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

        // Submit correct answer "94"
        val success = controller.submitHandshake("94")
        assertTrue("Correct answer must succeed", success)
        assertTrue("Tutorial must be completed", controller.isCompleted.value)
    }

    @Test
    fun testHandshakeFailureStatePreservesNodeAndGeneratesCorrectiveError() {
        val controller = RiveDialogueController(world4Tree, DefaultRiveAdapter())
        controller.advanceNode() // to node 2
        controller.advanceNode() // to node 3
        controller.advanceNode() // to node 4 (handshake)

        val wrongInputs = listOf("9409", "97", "3", "0", "42", "invalid")
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
        val controller = RiveDialogueController(world4Tree, DefaultRiveAdapter())
        assertTrue("Tree has unresolved handshake", controller.hasUnresolvedHandshake())

        // Attempting to skip without explicit opt-out must fail closed
        val skipped = controller.skip(allowBypassHandshake = false)
        assertFalse("skip() must return false when handshake is unresolved", skipped)
        assertFalse("isCompleted must remain false", controller.isCompleted.value)
    }

    @Test
    fun testProgressionPolicy_ExplicitOptOutBypassesHandshake() {
        val controller = RiveDialogueController(world4Tree, DefaultRiveAdapter())
        val skipped = controller.skip(allowBypassHandshake = true)
        assertTrue("Explicit skip opt-out must succeed", skipped)
        assertTrue("isCompleted must be true", controller.isCompleted.value)
    }

    @Test
    fun testProgressionPolicy_SkipAllowedAfterHandshakeResolved() {
        val controller = RiveDialogueController(world4Tree, DefaultRiveAdapter())
        controller.advanceNode() // 1
        controller.advanceNode() // 2
        controller.advanceNode() // 3 (handshake)
        assertTrue(controller.submitHandshake("94"))
        assertTrue(controller.isCompleted.value)
        assertFalse(controller.hasUnresolvedHandshake())

        // Calling skip when already completed succeeds
        assertTrue(controller.skip())
        assertTrue(controller.isCompleted.value)
    }

    // ── 9. Isolation: other generators & Worlds 1, 2, 3 unaffected ─────────────

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

    // ── 10. Focused World 4 Tutorial Enhancements ─────────────────────────────

    @Test
    fun testDialogueAndMathOverlayConsistency() {
        val node1 = world4Tree.dialogueNodes[0]
        assertTrue("Node 1 text must mention 94²", node1.text.contains("94²"))
        assertTrue("Node 1 overlay must contain 94²", node1.mathOverlay.expression.contains("94²"))

        val node2 = world4Tree.dialogueNodes[1]
        assertTrue("Node 2 text must explain Yāvadūnam", node2.text.contains("Yāvadūnam") || node2.text.contains("Yavadunam"))
        assertTrue("Node 2 text must preserve sutra rule 'By the Deficiency'", node2.text.contains("By the Deficiency", ignoreCase = true))
        assertTrue("Node 2 overlay must show deficiency formula", node2.mathOverlay.expression.contains("(94 − 6) | (6²)"))

        val node3 = world4Tree.dialogueNodes[2]
        assertTrue("Node 3 text must state 8836", node3.text.contains("8836"))
        assertTrue("Node 3 overlay must produce 8836", node3.mathOverlay.expression.contains("8836"))

        val node4 = world4Tree.dialogueNodes[3]
        assertTrue("Node 4 text must ask for 97²", node4.text.contains("97²"))
        assertTrue("Node 4 text must ask for 97 − 3", node4.text.contains("97 − 3"))
        assertTrue("Node 4 overlay must contain missing step '?'", node4.mathOverlay.expression.contains("?"))
        assertFalse("Node 4 overlay must not reveal 9409", node4.mathOverlay.expression.contains("9409"))
    }

    @Test
    fun testMathematicalInvariantForYavadunamNearBaseSquaring() {
        // For numbers near base 100: (100 - d)² = (100 - 2d) * 100 + d²
        val testCases = listOf(
            Pair(99L, 100L),
            Pair(97L, 100L),
            Pair(96L, 100L),
            Pair(94L, 100L),
            Pair(91L, 100L),
            Pair(89L, 100L),
            Pair(88L, 100L),
            Pair(998L, 1000L),
            Pair(994L, 1000L),
            Pair(985L, 1000L)
        )

        for ((operand, base) in testCases) {
            val diff = kotlin.math.abs(base - operand)
            val lhs = if (operand < base) operand - diff else operand + diff
            val rhs = diff * diff
            val expectedSquare = lhs * base + rhs
            val actualSquare = operand * operand

            assertEquals(
                "Invariant failure for operand $operand and base $base",
                actualSquare,
                expectedSquare
            )
            val problem = generator.generateSpecificProblem(operand)
            assertEquals(actualSquare, problem.correctAnswer)
        }
    }
}
