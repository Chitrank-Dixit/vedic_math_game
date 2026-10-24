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
 * P1 World 5 Tutorial Tests — Ūrdhva-Tiryagbhyām (Refined)
 *
 * Covers:
 * - Four-beat dialogue tree structure and schema compliance
 * - Sutra name and meaning correctness ("Vertically and Crosswise")
 * - 23 × 41 demonstration produces 943 (Units: 3×1=3, Middle: 2×1 + 3×4=14 -> 4 carry 1, Left: 2×4 + 1=9 -> 943)
 * - Additional math verification: 31 × 21 = 651, 27 × 38 = 1026, 89 × 76 = 6764
 * - 31 × 21 handshake: expected answer is "5" ((3×1) + (1×2) = 5), not "651" or "31"
 * - Handshake expression does NOT reveal complete product 651
 * - P0.1 regression: null/blank expectedAnswer fails closed
 * - P1.1 regression: submitting 651 or 31 fails when expected is 5
 * - Controller: incorrect answer does NOT advance tutorial
 * - Controller: correct answer "5" completes tutorial
 * - Other world generator sutra names are unchanged (Worlds 1, 2, 3, 4)
 */
@RunWith(RobolectricTestRunner::class)
class UrdhvaWorld5TutorialTest {

    private lateinit var generator: UrdhvaTiryagbhyamGenerator
    private lateinit var world5Tree: RiveSutraDialogueTree
    private val repository = RiveDialogueRepository()

    @Before
    fun setUp() {
        generator = UrdhvaTiryagbhyamGenerator(Random(42))

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
        val loaded = repository.getDialogueForWorld(5)
        assertNotNull("World 5 dialogue tree must exist in asset file", loaded)
        world5Tree = loaded!!
    }

    // ── 1. Sutra identity ──────────────────────────────────────────────────────

    @Test
    fun testSutraNameIsUrdhvaTiryagbhyam() {
        assertEquals("Urdhva-Tiryagbhyam", generator.sutraName)
    }

    @Test
    fun testDialogueTreeSutraId() {
        assertEquals("urdhva_tiryagbhyam", world5Tree.sutraId)
    }

    @Test
    fun testDialogueTreeEnglishMeaningRefersToVerticallyAndCrosswise() {
        assertTrue(
            "English meaning must reference 'Vertically and Crosswise': '${world5Tree.englishMeaning}'",
            world5Tree.englishMeaning.contains("Vertically and Crosswise", ignoreCase = true)
        )
    }

    @Test
    fun testDialogueTreeWorldNumber() {
        assertEquals(5, world5Tree.worldNumber)
    }

    // ── 2. Mathematical correctness ────────────────────────────────────────────

    @Test
    fun testCanonicalTutorialProblem23Times41Produces943() {
        val problem = generator.generateCanonicalTutorialProblem()
        assertEquals(23L, problem.operand)
        assertEquals(41L, problem.incrementedPrefix)
        assertEquals(943L, problem.correctAnswer)

        // Verify independent arithmetic
        assertEquals(943L, 23L * 41L)
        assertTrue(
            "VedicMathValidator must verify Urdhva 2x2 multiplication",
            VedicMathValidator.verifyUrdhva2x2(23L, 41L, 943L)
        )

        // Verify decomposition steps structure (4 steps)
        assertEquals(4, problem.decompositionSteps.size)
        // Step 1: Right Vertical units: 3 * 1 = 3
        assertEquals("3", problem.decompositionSteps[0].stepResult)
        // Step 2: Crosswise tens: (2 * 1) + (3 * 4) = 14 -> 4 (carry 1)
        assertEquals("4 (carry 1)", problem.decompositionSteps[1].stepResult)
        // Step 3: Left Vertical hundreds: 2 * 4 = 8 (+ carry 1 = 9)
        assertEquals("9", problem.decompositionSteps[2].stepResult)
        // Step 4: Concatenation: 943
        assertEquals("943", problem.decompositionSteps[3].stepResult)
    }

    @Test
    fun test31Times21Produces651() {
        val p = generator.generateSpecificProblemPair(31L, 21L)
        assertEquals(651L, p.correctAnswer)
        assertEquals(31L * 21L, p.correctAnswer)
        assertTrue(VedicMathValidator.verifyUrdhva2x2(31L, 21L, 651L))
        assertEquals("1", p.decompositionSteps[0].stepResult) // 1 * 1 = 1
        assertEquals("5", p.decompositionSteps[1].stepResult) // (3*1) + (1*2) = 5 (carry 0)
        assertEquals("6", p.decompositionSteps[2].stepResult) // 3 * 2 = 6
        assertEquals("651", p.decompositionSteps[3].stepResult)
    }

    @Test
    fun test27Times38WithCarriesProduces1026() {
        val p = generator.generateSpecificProblemPair(27L, 38L)
        assertEquals(1026L, p.correctAnswer)
        assertEquals(27L * 38L, p.correctAnswer)
        assertTrue(VedicMathValidator.verifyUrdhva2x2(27L, 38L, 1026L))
        assertEquals("6 (carry 5)", p.decompositionSteps[0].stepResult) // 7 * 8 = 56 -> 6 (carry 5)
        assertEquals("2 (carry 4)", p.decompositionSteps[1].stepResult) // (2*8) + (7*3) + 5 = 42 -> 2 (carry 4)
        assertEquals("10", p.decompositionSteps[2].stepResult) // (2*3) + 4 = 10
        assertEquals("1026", p.decompositionSteps[3].stepResult)
    }

    @Test
    fun test89Times76Produces6764() {
        val p = generator.generateSpecificProblemPair(89L, 76L)
        assertEquals(6764L, p.correctAnswer)
        assertEquals(89L * 76L, p.correctAnswer)
        assertTrue(VedicMathValidator.verifyUrdhva2x2(89L, 76L, 6764L))
        assertEquals("6764", p.decompositionSteps[3].stepResult)
    }

    // ── 3. Dialogue tree structure ─────────────────────────────────────────────

    @Test
    fun testWorld5HasExactlyFourNodes() {
        assertEquals(
            "World 5 tutorial must have exactly 4 nodes",
            4,
            world5Tree.dialogueNodes.size
        )
    }

    @Test
    fun testWorld5PassesRiveDialogueValidator() {
        val result = RiveDialogueValidator.validate(world5Tree)
        assertTrue("World 5 must pass validator: ${result.errors}", result.isValid)
        assertTrue("Validation errors must be empty", result.errors.isEmpty())
    }

    @Test
    fun testAllNodeTextsNonBlank() {
        for (node in world5Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} text must not be blank", node.text.isNotBlank())
        }
    }

    @Test
    fun testAllMathOverlayExpressionsNonBlank() {
        for (node in world5Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId} math_overlay.expression must not be blank",
                node.mathOverlay.expression.isNotBlank()
            )
        }
    }

    @Test
    fun testAllNodesUseValidAnimations() {
        for (node in world5Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId}: animation '${node.riveState.animation}' must be in whitelist for ${node.riveState.character}",
                RiveAnimationConfig.isValidAnimation(node.riveState.character, node.riveState.animation)
            )
        }
    }

    @Test
    fun testSpeakerMatchesCharacterForAllNodes() {
        for (node in world5Tree.dialogueNodes) {
            assertEquals(
                "Node ${node.nodeId}: speaker must match rive_state.character",
                node.speaker,
                node.riveState.character
            )
        }
    }

    @Test
    fun testNoGuruWinkAnimation() {
        for (node in world5Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId}: Guru must never use wink animation",
                node.riveState.animation != "guru_wink"
            )
        }
    }

    // ── 4. Tutorial content alignment ─────────────────────────────────────────

    @Test
    fun testDemonstrationNodeReferences943() {
        // Node 3 (index 2) is the demonstration beat; it must show 943
        val demo = world5Tree.dialogueNodes[2]
        assertTrue(
            "Demonstration node (Node 3) must reference '943'. Text='${demo.text}', Overlay='${demo.mathOverlay.expression}'",
            demo.text.contains("943") || demo.mathOverlay.expression.contains("943")
        )
    }

    @Test
    fun testDemonstrationNodeDoesNotRevealHandshakeOperand31Times21() {
        val demo = world5Tree.dialogueNodes[2]
        assertFalse(
            "Demonstration node must not reveal handshake operand 31 × 21",
            demo.text.contains("31 × 21") || demo.mathOverlay.expression.contains("31 × 21")
        )
    }

    @Test
    fun testDemonstrationNodeDoesNotRevealHandshakeAnswer5() {
        val demo = world5Tree.dialogueNodes[2]
        assertFalse(
            "Demonstration node must not reveal handshake question (3 × 1) + (1 × 2)",
            demo.text.contains("(3 × 1) + (1 × 2)") || demo.mathOverlay.expression.contains("(3 × 1) + (1 × 2)")
        )
    }

    @Test
    fun testHandshakeNodeDoesNotRevealCompleteProduct651() {
        // Node 4 (index 3) is the handshake beat; it must NOT pre-reveal the complete product in expression
        val handshakeNode = world5Tree.dialogueNodes.last()
        assertFalse(
            "Handshake node expression must NOT contain '651'. Found: '${handshakeNode.mathOverlay.expression}'",
            handshakeNode.mathOverlay.expression.contains("651")
        )
    }

    // ── 5. Handshake coherence ─────────────────────────────────────────────────

    @Test
    fun testFinalNodeRequiresUserTap() {
        assertTrue("Final node must require user tap", world5Tree.dialogueNodes.last().interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testHandshakeExpectedAnswerIsExactly5() {
        val finalNode = world5Tree.dialogueNodes.last()
        val answer = finalNode.interactiveHandshake.expectedAnswer
        assertNotNull("Handshake expectedAnswer must not be null", answer)
        assertEquals(
            "31 × 21 handshake must expect middle crosswise sum (3×1)+(1×2)=5, not 651 or 31",
            "5",
            answer
        )
    }

    @Test
    fun testHandshakePromptIsNonBlank() {
        val finalNode = world5Tree.dialogueNodes.last()
        val prompt = finalNode.interactiveHandshake.promptText
        assertNotNull("Handshake prompt text must not be null", prompt)
        assertTrue("Handshake prompt text must not be blank", prompt!!.isNotBlank())
    }

    @Test
    fun testHandshakeTargetElementIdIsNumpadKey5() {
        val finalNode = world5Tree.dialogueNodes.last()
        val tid = finalNode.interactiveHandshake.targetElementId
        assertNotNull("Handshake targetElementId must not be null", tid)
        assertEquals("numpad_key_5", tid)
    }

    @Test
    fun testHandshakeExpressionContainsQuestionMark() {
        val finalNode = world5Tree.dialogueNodes.last()
        assertTrue(
            "Handshake math_overlay must contain '?' to indicate missing value. Got: '${finalNode.mathOverlay.expression}'",
            finalNode.mathOverlay.expression.contains("?")
        )
    }

    @Test
    fun testGenerateHandshakeChoicesContainsExpectedAnswer5() {
        val choices = generateHandshakeChoices("5")
        assertEquals(5, choices.size)
        assertTrue("Choices must contain expected answer 5: $choices", choices.contains("5"))
        assertEquals(listOf("3", "4", "5", "6", "7"), choices)
    }

    // ── 6. P0.1 regression — fail-closed ──────────────────────────────────────

    @Test
    fun testP01_NullExpectedAnswerAlwaysFails() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_5",
            promptText = "Some prompt",
            expectedAnswer = null
        )
        assertFalse("P0.1: null expectedAnswer must fail for '5'", VedicMathValidator.evaluateHandshake(handshake, "5"))
        assertFalse("P0.1: null expectedAnswer must fail for ''", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("P0.1: null expectedAnswer must fail for any input", VedicMathValidator.evaluateHandshake(handshake, "anything"))
    }

    @Test
    fun testP01_BlankExpectedAnswerAlwaysFails() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_5",
            promptText = "Some prompt",
            expectedAnswer = "   "
        )
        assertFalse("P0.1: blank expectedAnswer must fail for '5'", VedicMathValidator.evaluateHandshake(handshake, "5"))
    }

    // ── 7. P1.1 regression — expectedAnswer is the authority ──────────────────

    @Test
    fun testP11_ExpectedAnswerIsTheAuthority() {
        val finalNode = world5Tree.dialogueNodes.last()
        val handshake = finalNode.interactiveHandshake

        assertTrue(
            "Submitting expected answer '5' must pass",
            VedicMathValidator.evaluateHandshake(handshake, "5")
        )
        assertTrue(
            "Whitespace-padded ' 5 ' must pass",
            VedicMathValidator.evaluateHandshake(handshake, " 5 ")
        )
        assertFalse(
            "Submitting complete product '651' must fail because target is middle term '5'",
            VedicMathValidator.evaluateHandshake(handshake, "651")
        )
        assertFalse(
            "Submitting original operand '31' must fail",
            VedicMathValidator.evaluateHandshake(handshake, "31")
        )
        assertFalse(
            "Submitting second operand '21' must fail",
            VedicMathValidator.evaluateHandshake(handshake, "21")
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
        val controller = RiveDialogueController(world5Tree, adapter)

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

        // Submit correct answer "5"
        val success = controller.submitHandshake("5")
        assertTrue("Correct answer must succeed", success)
        assertTrue("Tutorial must be completed", controller.isCompleted.value)
    }

    @Test
    fun testHandshakeFailureStatePreservesNodeAndGeneratesCorrectiveError() {
        val controller = RiveDialogueController(world5Tree, DefaultRiveAdapter())
        controller.advanceNode() // to node 2
        controller.advanceNode() // to node 3
        controller.advanceNode() // to node 4 (handshake)

        val wrongInputs = listOf("651", "31", "21", "0", "14", "invalid")
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
        val controller = RiveDialogueController(world5Tree, DefaultRiveAdapter())
        assertTrue("Tree has unresolved handshake", controller.hasUnresolvedHandshake())

        // Attempting to skip without explicit opt-out must fail closed
        val skipped = controller.skip(allowBypassHandshake = false)
        assertFalse("skip() must return false when handshake is unresolved", skipped)
        assertFalse("isCompleted must remain false", controller.isCompleted.value)
    }

    @Test
    fun testProgressionPolicy_ExplicitOptOutBypassesHandshake() {
        val controller = RiveDialogueController(world5Tree, DefaultRiveAdapter())
        val skipped = controller.skip(allowBypassHandshake = true)
        assertTrue("Explicit skip opt-out must succeed", skipped)
        assertTrue("isCompleted must be true", controller.isCompleted.value)
    }

    @Test
    fun testProgressionPolicy_SkipAllowedAfterHandshakeResolved() {
        val controller = RiveDialogueController(world5Tree, DefaultRiveAdapter())
        controller.advanceNode() // 1
        controller.advanceNode() // 2
        controller.advanceNode() // 3 (handshake)
        assertTrue(controller.submitHandshake("5"))
        assertTrue(controller.isCompleted.value)
        assertFalse(controller.hasUnresolvedHandshake())

        // Calling skip when already completed succeeds
        assertTrue(controller.skip())
        assertTrue(controller.isCompleted.value)
    }

    // ── 9. Isolation: other generators & Worlds 1, 2, 3, 4 unaffected ─────────

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

    // ── 10. Focused World 5 Tutorial Enhancements ─────────────────────────────

    @Test
    fun testDialogueAndMathOverlayConsistency() {
        val node1 = world5Tree.dialogueNodes[0]
        assertTrue("Node 1 text must mention 23 × 41", node1.text.contains("23 × 41"))
        assertTrue("Node 1 overlay must contain 23 × 41", node1.mathOverlay.expression.contains("23 × 41"))

        val node2 = world5Tree.dialogueNodes[1]
        assertTrue("Node 2 text must explain Ūrdhva-Tiryagbhyām", node2.text.contains("Ūrdhva-Tiryagbhyām") || node2.text.contains("Urdhva-Tiryagbhyam"))
        assertTrue("Node 2 text must preserve sutra rule 'Vertically and Crosswise'", node2.text.contains("Vertically and Crosswise", ignoreCase = true))
        assertTrue("Node 2 overlay must show spatial ray formula", node2.mathOverlay.expression.contains("(2×4) | (2×1 + 3×4) | (3×1)"))

        val node3 = world5Tree.dialogueNodes[2]
        assertTrue("Node 3 text must state 943", node3.text.contains("943"))
        assertTrue("Node 3 overlay must produce 943", node3.mathOverlay.expression.contains("943"))

        val node4 = world5Tree.dialogueNodes[3]
        assertTrue("Node 4 text must ask for 31 × 21", node4.text.contains("31 × 21"))
        assertTrue("Node 4 text must ask for (3 × 1) + (1 × 2)", node4.text.contains("(3 × 1) + (1 × 2)"))
        assertTrue("Node 4 overlay must contain missing step '?'", node4.mathOverlay.expression.contains("?"))
        assertFalse("Node 4 overlay must not reveal 651", node4.mathOverlay.expression.contains("651"))
    }

    @Test
    fun testMathematicalInvariantForUrdhva2x2Multiplication() {
        // For two 2-digit numbers (10 a1 + a0) * (10 b1 + b0)
        // = 100(a1 * b1) + 10(a1 * b0 + a0 * b1) + (a0 * b0)
        val testCases = listOf(
            Pair(23L, 41L),
            Pair(31L, 21L),
            Pair(12L, 34L),
            Pair(27L, 38L),
            Pair(45L, 67L),
            Pair(89L, 76L),
            Pair(99L, 99L),
            Pair(11L, 11L)
        )

        for ((a, b) in testCases) {
            val a1 = a / 10
            val a0 = a % 10
            val b1 = b / 10
            val b0 = b % 10

            val right = a0 * b0
            val middle = (a1 * b0) + (a0 * b1)
            val left = a1 * b1
            val expectedProduct = left * 100 + middle * 10 + right
            val actualProduct = a * b

            assertEquals(
                "Invariant algebraic failure for $a × $b",
                actualProduct,
                expectedProduct
            )

            val p = generator.generateSpecificProblemPair(a, b)
            assertEquals(actualProduct, p.correctAnswer)
            assertTrue(VedicMathValidator.verifyUrdhva2x2(a, b, actualProduct))
        }
    }
}
