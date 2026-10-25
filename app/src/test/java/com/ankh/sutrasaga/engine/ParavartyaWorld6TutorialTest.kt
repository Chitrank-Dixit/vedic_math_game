package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.data.repository.RiveDialogueRepository
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.InteractiveHandshake
import com.ankh.sutrasaga.domain.models.RiveAnimationConfig
import com.ankh.sutrasaga.domain.models.RiveSutraDialogueTree
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
 * P1 World 6 Tutorial Tests — Parāvartya Yojayet (Refined)
 *
 * Covers:
 * - Four-beat dialogue tree structure and schema compliance
 * - Sutra name and meaning correctness ("Transpose and Apply")
 * - 1225 ÷ 12 demonstration produces Quotient 102, Remainder 1
 * - Additional math verification: 144 ÷ 12 = 12 R 0, 432 ÷ 11 = 39 R 3, 97 ÷ 12 = 8 R 1, 12345 ÷ 101 = 122 R 23
 * - 144 ÷ 12 handshake: expected answer is "2" (second quotient digit: 4 + (1 × −2) = 2), not "12" or "144"
 * - Handshake expression does NOT reveal complete quotient 12
 * - P0.1 regression: null/blank expectedAnswer fails closed
 * - P1.1 regression: submitting 12 or 144 fails when expected is 2
 * - Controller: incorrect answer does NOT advance tutorial
 * - Controller: correct answer "2" completes tutorial
 * - Other world generator sutra names are unchanged (Worlds 1–5)
 */
@RunWith(RobolectricTestRunner::class)
class ParavartyaWorld6TutorialTest {

    private lateinit var generator: ParavartyaYojayetGenerator
    private lateinit var world6Tree: RiveSutraDialogueTree
    private val repository = RiveDialogueRepository()

    @Before
    fun setUp() {
        generator = ParavartyaYojayetGenerator(Random(42))

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
        val loaded = repository.getDialogueForWorld(6)
        assertNotNull("World 6 dialogue tree must exist in asset file", loaded)
        world6Tree = loaded!!
    }

    // ── 1. Sutra identity ──────────────────────────────────────────────────────

    @Test
    fun testSutraNameIsParavartyaYojayet() {
        assertEquals("Paravartya Yojayet", generator.sutraName)
    }

    @Test
    fun testDialogueTreeSutraId() {
        assertEquals("paravartya_yojayet", world6Tree.sutraId)
    }

    @Test
    fun testDialogueTreeEnglishMeaningRefersToTransposeAndApply() {
        assertTrue(
            "English meaning must reference 'Transpose and Apply': '${world6Tree.englishMeaning}'",
            world6Tree.englishMeaning.contains("Transpose and Apply", ignoreCase = true)
        )
    }

    @Test
    fun testDialogueTreeWorldNumber() {
        assertEquals(6, world6Tree.worldNumber)
    }

    // ── 2. Mathematical correctness ────────────────────────────────────────────

    @Test
    fun testCanonicalTutorialProblem1225DividedBy12Produces102R1() {
        val problem = generator.generateCanonicalTutorialProblem()
        assertEquals(1225L, problem.operand)
        assertEquals(102L, problem.correctAnswer)
        assertEquals(12L, problem.prefixPart) // Divisor
        assertEquals(-2L, problem.incrementedPrefix) // Transposed deviation
        assertEquals(1L, problem.prefixProduct) // Remainder
        assertEquals("1225 = 12 × 102 + 1", problem.appendedSuffix)

        // Verify independent arithmetic
        assertEquals(102L, 1225L / 12L)
        assertEquals(1L, 1225L % 12L)
        assertTrue(
            "VedicMathValidator must verify Paravartya division",
            VedicMathValidator.verifyParavartyaDivision(1225L, 12L, 102L, 1L)
        )

        // Verify decomposition steps (4 steps: col 1, col 2, col 3, remainder col 4)
        assertEquals(4, problem.decompositionSteps.size)
        // Col 1: Bring down 1
        assertEquals("1", problem.decompositionSteps[0].stepResult)
        // Col 2: 2 + (1 * -2) = 0
        assertEquals("0", problem.decompositionSteps[1].stepResult)
        // Col 3: 2 + (0 * -2) = 2
        assertEquals("2", problem.decompositionSteps[2].stepResult)
        // Col 4 (Remainder): 5 + (2 * -2) = 1
        assertEquals("1", problem.decompositionSteps[3].stepResult)
    }

    @Test
    fun test144DividedBy12Produces12R0() {
        val res = generator.solveParavartya(144L, 12L)
        assertEquals(12L, res.quotient)
        assertEquals(0L, res.normalizedRemainder)
        assertEquals("144 = 12 × 12 + 0", res.verificationIdentity)
        assertTrue(VedicMathValidator.verifyParavartyaDivision(144L, 12L, 12L, 0L))
        // Col 1: 1
        assertEquals(1L, res.steps[0].resultingColumnValue)
        // Col 2: 4 + (1 * -2) = 2
        assertEquals(2L, res.steps[1].resultingColumnValue)
        // Remainder: 4 + (2 * -2) = 0
        assertEquals(0L, res.steps[2].resultingColumnValue)
    }

    @Test
    fun test432DividedBy11Produces39R3() {
        val res = generator.solveParavartya(432L, 11L)
        assertEquals(39L, res.quotient)
        assertEquals(3L, res.normalizedRemainder)
        assertTrue(VedicMathValidator.verifyParavartyaDivision(432L, 11L, 39L, 3L))
    }

    @Test
    fun test97DividedBy12Produces8R1() {
        val res = generator.solveParavartya(97L, 12L)
        assertEquals(8L, res.quotient)
        assertEquals(1L, res.normalizedRemainder)
        assertTrue(VedicMathValidator.verifyParavartyaDivision(97L, 12L, 8L, 1L))
    }

    @Test
    fun test12345DividedBy101Produces122R23() {
        val res = generator.solveParavartya(12345L, 101L)
        assertEquals(122L, res.quotient)
        assertEquals(23L, res.normalizedRemainder)
        assertTrue(VedicMathValidator.verifyParavartyaDivision(12345L, 101L, 122L, 23L))
    }

    // ── 3. Dialogue tree structure ─────────────────────────────────────────────

    @Test
    fun testWorld6HasExactlyFourNodes() {
        assertEquals(
            "World 6 tutorial must have exactly 4 nodes",
            4,
            world6Tree.dialogueNodes.size
        )
    }

    @Test
    fun testWorld6PassesRiveDialogueValidator() {
        val result = RiveDialogueValidator.validate(world6Tree)
        assertTrue("World 6 must pass validator: ${result.errors}", result.isValid)
        assertTrue("Validation errors must be empty", result.errors.isEmpty())
    }

    @Test
    fun testAllNodeTextsNonBlank() {
        for (node in world6Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} text must not be blank", node.text.isNotBlank())
        }
    }

    @Test
    fun testAllMathOverlayExpressionsNonBlank() {
        for (node in world6Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId} math_overlay.expression must not be blank",
                node.mathOverlay.expression.isNotBlank()
            )
        }
    }

    @Test
    fun testAllNodesUseValidAnimations() {
        for (node in world6Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId}: animation '${node.riveState.animation}' must be in whitelist for ${node.riveState.character}",
                RiveAnimationConfig.isValidAnimation(node.riveState.character, node.riveState.animation)
            )
        }
    }

    @Test
    fun testSpeakerMatchesCharacterForAllNodes() {
        for (node in world6Tree.dialogueNodes) {
            assertEquals(
                "Node ${node.nodeId}: speaker must match rive_state.character",
                node.speaker,
                node.riveState.character
            )
        }
    }

    @Test
    fun testNoGuruWinkAnimation() {
        for (node in world6Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId}: Guru must never use wink animation",
                node.riveState.animation != "guru_wink"
            )
        }
    }

    // ── 4. Tutorial content alignment ─────────────────────────────────────────

    @Test
    fun testDemonstrationNodeReferences102And1() {
        // Node 3 (index 2) is the demonstration beat; it must show 102 and 1
        val demo = world6Tree.dialogueNodes[2]
        assertTrue(
            "Demonstration node (Node 3) must reference '102'. Text='${demo.text}', Overlay='${demo.mathOverlay.expression}'",
            demo.text.contains("102") || demo.mathOverlay.expression.contains("102")
        )
        assertTrue(
            "Demonstration node (Node 3) must reference remainder '1'. Text='${demo.text}', Overlay='${demo.mathOverlay.expression}'",
            demo.text.contains("Remainder 1") || demo.mathOverlay.expression.contains("R  1")
        )
    }

    @Test
    fun testDemonstrationNodeDoesNotRevealHandshakeOperand144() {
        val demo = world6Tree.dialogueNodes[2]
        assertFalse(
            "Demonstration node must not reveal handshake operand 144 ÷ 12",
            demo.text.contains("144 ÷ 12") || demo.mathOverlay.expression.contains("144")
        )
    }

    @Test
    fun testDemonstrationNodeDoesNotRevealHandshakeAnswer2() {
        val demo = world6Tree.dialogueNodes[2]
        assertFalse(
            "Demonstration node must not reveal handshake question 4 + (1 × −2)",
            demo.text.contains("4 + (1 × −2)") || demo.mathOverlay.expression.contains("4 + (1 × −2)")
        )
    }

    @Test
    fun testHandshakeNodeDoesNotRevealCompleteQuotient12() {
        // Node 4 (index 3) is the handshake beat; it must NOT pre-reveal the complete quotient 12 in expression
        val handshakeNode = world6Tree.dialogueNodes.last()
        assertFalse(
            "Handshake node expression must NOT contain complete quotient '12'. Found: '${handshakeNode.mathOverlay.expression}'",
            handshakeNode.mathOverlay.expression.contains("= 12")
        )
    }

    // ── 5. Handshake coherence ─────────────────────────────────────────────────

    @Test
    fun testFinalNodeRequiresUserTap() {
        assertTrue("Final node must require user tap", world6Tree.dialogueNodes.last().interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testHandshakeExpectedAnswerIsExactly2() {
        val finalNode = world6Tree.dialogueNodes.last()
        val answer = finalNode.interactiveHandshake.expectedAnswer
        assertNotNull("Handshake expectedAnswer must not be null", answer)
        assertEquals(
            "144 ÷ 12 handshake must expect second quotient digit 4 + (1 × -2) = 2, not 12 or 144",
            "2",
            answer
        )
    }

    @Test
    fun testHandshakePromptIsNonBlank() {
        val finalNode = world6Tree.dialogueNodes.last()
        val prompt = finalNode.interactiveHandshake.promptText
        assertNotNull("Handshake prompt text must not be null", prompt)
        assertTrue("Handshake prompt text must not be blank", prompt!!.isNotBlank())
    }

    @Test
    fun testHandshakeTargetElementIdIsNumpadKey2() {
        val finalNode = world6Tree.dialogueNodes.last()
        val tid = finalNode.interactiveHandshake.targetElementId
        assertNotNull("Handshake targetElementId must not be null", tid)
        assertEquals("numpad_key_2", tid)
    }

    @Test
    fun testHandshakeExpressionContainsQuestionMark() {
        val finalNode = world6Tree.dialogueNodes.last()
        assertTrue(
            "Handshake math_overlay must contain '?' to indicate missing value. Got: '${finalNode.mathOverlay.expression}'",
            finalNode.mathOverlay.expression.contains("?")
        )
    }

    @Test
    fun testGenerateHandshakeChoicesContainsExpectedAnswer2() {
        val choices = generateHandshakeChoices("2")
        assertEquals(5, choices.size)
        assertTrue("Choices must contain expected answer 2: $choices", choices.contains("2"))
        assertEquals(listOf("0", "1", "2", "3", "4"), choices)
    }

    // ── 6. P0.1 regression — fail-closed ──────────────────────────────────────

    @Test
    fun testP01_NullExpectedAnswerAlwaysFails() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_2",
            promptText = "Some prompt",
            expectedAnswer = null
        )
        assertFalse("P0.1: null expectedAnswer must fail for '2'", VedicMathValidator.evaluateHandshake(handshake, "2"))
        assertFalse("P0.1: null expectedAnswer must fail for ''", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("P0.1: null expectedAnswer must fail for any input", VedicMathValidator.evaluateHandshake(handshake, "anything"))
    }

    @Test
    fun testP01_BlankExpectedAnswerAlwaysFails() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_2",
            promptText = "Some prompt",
            expectedAnswer = "   "
        )
        assertFalse("P0.1: blank expectedAnswer must fail for '2'", VedicMathValidator.evaluateHandshake(handshake, "2"))
    }

    // ── 7. P1.1 regression — expectedAnswer is the authority ──────────────────

    @Test
    fun testP11_ExpectedAnswerIsTheAuthority() {
        val finalNode = world6Tree.dialogueNodes.last()
        val handshake = finalNode.interactiveHandshake

        assertTrue(
            "Submitting expected answer '2' must pass",
            VedicMathValidator.evaluateHandshake(handshake, "2")
        )
        assertTrue(
            "Whitespace-padded ' 2 ' must pass",
            VedicMathValidator.evaluateHandshake(handshake, " 2 ")
        )
        assertFalse(
            "Submitting complete quotient '12' must fail because target is intermediate digit '2'",
            VedicMathValidator.evaluateHandshake(handshake, "12")
        )
        assertFalse(
            "Submitting original dividend '144' must fail",
            VedicMathValidator.evaluateHandshake(handshake, "144")
        )
        assertFalse(
            "Submitting divisor '12' must fail",
            VedicMathValidator.evaluateHandshake(handshake, "12")
        )
        assertFalse(
            "Submitting '0' must fail",
            VedicMathValidator.evaluateHandshake(handshake, "0")
        )
        assertFalse(
            "Submitting '-2' must fail",
            VedicMathValidator.evaluateHandshake(handshake, "-2")
        )
    }

    // ── 8. Controller interaction ─────────────────────────────────────────────

    @Test
    fun testControllerAdvancesThroughNodesAndRequiresHandshakeAtEnd() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world6Tree, adapter)

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

        // Submit correct answer "2"
        val success = controller.submitHandshake("2")
        assertTrue("Correct answer must succeed", success)
        assertTrue("Tutorial must be completed", controller.isCompleted.value)
    }

    @Test
    fun testHandshakeFailureStatePreservesNodeAndGeneratesCorrectiveError() {
        val controller = RiveDialogueController(world6Tree, DefaultRiveAdapter())
        controller.advanceNode() // to node 2
        controller.advanceNode() // to node 3
        controller.advanceNode() // to node 4 (handshake)

        val wrongInputs = listOf("12", "144", "0", "1", "4", "-2", "invalid")
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
        val controller = RiveDialogueController(world6Tree, DefaultRiveAdapter())
        assertTrue("Tree has unresolved handshake", controller.hasUnresolvedHandshake())

        // Attempting to skip without explicit opt-out must fail closed
        val skipped = controller.skip(allowBypassHandshake = false)
        assertFalse("skip() must return false when handshake is unresolved", skipped)
        assertFalse("isCompleted must remain false", controller.isCompleted.value)
    }

    @Test
    fun testProgressionPolicy_ExplicitOptOutBypassesHandshake() {
        val controller = RiveDialogueController(world6Tree, DefaultRiveAdapter())
        val skipped = controller.skip(allowBypassHandshake = true)
        assertTrue("Explicit skip opt-out must succeed", skipped)
        assertTrue("isCompleted must be true", controller.isCompleted.value)
    }

    @Test
    fun testProgressionPolicy_SkipAllowedAfterHandshakeResolved() {
        val controller = RiveDialogueController(world6Tree, DefaultRiveAdapter())
        controller.advanceNode() // 1
        controller.advanceNode() // 2
        controller.advanceNode() // 3 (handshake)
        assertTrue(controller.submitHandshake("2"))
        assertTrue(controller.isCompleted.value)
        assertFalse(controller.hasUnresolvedHandshake())

        // Calling skip when already completed succeeds
        assertTrue(controller.skip())
        assertTrue(controller.isCompleted.value)
    }

    // ── 9. Isolation: other generators & Worlds 1–5 unaffected ────────────────

    @Test
    fun testOtherGeneratorsSutraNamesUnaffected() {
        assertEquals(
            "Ekadhikena sutra name must be unchanged",
            "Ekadhikena Purvena",
            com.ankh.sutrasaga.engine.EkadhikenaPurvenaGenerator().sutraName
        )
        assertEquals(
            "Nikhilam sutra name must be unchanged",
            "Nikhilam Navatashcaramam Dashatah",
            com.ankh.sutrasaga.engine.NikhilamGenerator().sutraName
        )
        assertEquals(
            "Ekanyunena sutra name must be unchanged",
            "Ekanyunena Purvena",
            com.ankh.sutrasaga.engine.EkanyunenaGenerator().sutraName
        )
        assertEquals(
            "Yavadunam sutra name must be unchanged",
            "Yavadunam",
            com.ankh.sutrasaga.engine.YavadunamGenerator().sutraName
        )
        assertEquals(
            "Urdhva sutra name must be unchanged",
            "Urdhva-Tiryagbhyam",
            com.ankh.sutrasaga.engine.UrdhvaTiryagbhyamGenerator().sutraName
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

    // ── 10. Focused World 6 Tutorial Enhancements ─────────────────────────────

    @Test
    fun testDialogueAndMathOverlayConsistency() {
        val node1 = world6Tree.dialogueNodes[0]
        assertTrue("Node 1 text must mention 1225 ÷ 12", node1.text.contains("1225 ÷ 12"))
        assertTrue("Node 1 overlay must contain 1225 ÷ 12", node1.mathOverlay.expression.contains("1225 ÷ 12"))

        val node2 = world6Tree.dialogueNodes[1]
        assertTrue("Node 2 text must explain Parāvartya Yojayet", node2.text.contains("Parāvartya Yojayet") || node2.text.contains("Paravartya Yojayet"))
        assertTrue("Node 2 text must preserve sutra rule 'Transpose and Apply'", node2.text.contains("Transpose and Apply", ignoreCase = true))
        assertTrue("Node 2 overlay must show transposed deviation", node2.mathOverlay.expression.contains("−2"))

        val node3 = world6Tree.dialogueNodes[2]
        assertTrue("Node 3 text must state 102", node3.text.contains("102"))
        assertTrue("Node 3 overlay must produce 102  R  1", node3.mathOverlay.expression.contains("102  R  1"))

        val node4 = world6Tree.dialogueNodes[3]
        assertTrue("Node 4 text must ask for 144 ÷ 12", node4.text.contains("144 ÷ 12"))
        assertTrue("Node 4 text must ask for 4 + (1 × −2)", node4.text.contains("4 + (1 × −2)"))
        assertTrue("Node 4 overlay must contain missing step '?'", node4.mathOverlay.expression.contains("?"))
        assertFalse("Node 4 overlay must not reveal quotient 12", node4.mathOverlay.expression.contains("= 12"))
    }

    @Test
    fun testMathematicalInvariantForParavartyaDivision() {
        // Division identity: N = D * Q + R, with 0 <= R < D
        val testCases = listOf(
            Pair(1225L, 12L),
            Pair(144L, 12L),
            Pair(432L, 11L),
            Pair(97L, 12L),
            Pair(500L, 11L),
            Pair(1000L, 13L),
            Pair(2500L, 14L),
            Pair(9999L, 19L),
            Pair(12345L, 101L),
            Pair(54321L, 102L)
        )

        for ((n, d) in testCases) {
            val res = generator.solveParavartya(n, d)
            assertEquals("Quotient mismatch for $n ÷ $d", n / d, res.quotient)
            assertEquals("Remainder mismatch for $n ÷ $d", n % d, res.normalizedRemainder)
            assertTrue("Remainder must be non-negative", res.normalizedRemainder >= 0L)
            assertTrue("Remainder must be strictly less than divisor", res.normalizedRemainder < d)
            assertEquals(n, d * res.quotient + res.normalizedRemainder)
            assertTrue(VedicMathValidator.verifyParavartyaDivision(n, d, res.quotient, res.normalizedRemainder))
        }
    }
}
