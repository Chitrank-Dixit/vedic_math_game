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
 * P1 World 2 Tutorial Tests — Nikhilam Navatashcaramam Dashatah (Refined)
 *
 * Covers:
 * - Four-beat dialogue tree structure and schema compliance
 * - Sutra name and Sanskrit meaning correctness
 * - 1000 - 364 demonstration produces 636 (9-3=6, 9-6=3, 10-4=6)
 * - Additional math verification: 100-37=63, 1000-346=654, 10000-3468=6532
 * - 1000 - 427 handshake: expected answer is "3" (final digit 10-7=3), not "573" or "7"
 * - Handshake expression does NOT reveal complete answer 573
 * - P0.1 regression: null/blank expectedAnswer fails closed
 * - P1.1 regression: submitting 573 or 7 fails when expected is 3
 * - Controller: incorrect answer does NOT advance tutorial
 * - Controller: correct answer "3" completes tutorial
 * - Other world generator sutra names are unchanged
 */
@RunWith(RobolectricTestRunner::class)
class NikhilamWorld2TutorialTest {

    private lateinit var generator: NikhilamGenerator
    private lateinit var world2Tree: RiveSutraDialogueTree
    private val repository = RiveDialogueRepository()

    @Before
    fun setUp() {
        generator = NikhilamGenerator(Random(42))

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
        val loaded = repository.getDialogueForWorld(2)
        assertNotNull("World 2 dialogue tree must exist in asset file", loaded)
        world2Tree = loaded!!
    }

    // ── 1. Sutra identity ──────────────────────────────────────────────────────

    @Test
    fun testSutraNameIsNikhilamNavatashcaramamDashatah() {
        assertEquals("Nikhilam Navatashcaramam Dashatah", generator.sutraName)
    }

    @Test
    fun testDialogueTreeSutraId() {
        assertEquals("nikhilam_navatashcaramam_dashatah", world2Tree.sutraId)
    }

    @Test
    fun testDialogueTreeEnglishMeaningRefersToAllFrom9AndLastFrom10() {
        assertTrue(
            "English meaning must reference 'All from 9 and the Last from 10': '${world2Tree.englishMeaning}'",
            world2Tree.englishMeaning.contains("All from 9", ignoreCase = true) &&
                world2Tree.englishMeaning.contains("Last from 10", ignoreCase = true)
        )
    }

    @Test
    fun testDialogueTreeWorldNumber() {
        assertEquals(2, world2Tree.worldNumber)
    }

    // ── 2. Mathematical correctness ────────────────────────────────────────────

    @Test
    fun testCanonicalTutorialProblem1000Minus364Produces636() {
        val problem = generator.generateCanonicalTutorialProblem()
        assertEquals(364L, problem.operand)
        assertEquals(1000L, problem.prefixPart)
        assertEquals(636L, problem.correctAnswer)

        // Verify independent arithmetic
        assertEquals(636L, 1000L - 364L)
        assertTrue(
            "VedicMathValidator must verify base subtraction",
            VedicMathValidator.verifyBaseSubtraction(1000L, 364L, 636L)
        )

        // Verify decomposition steps structure
        assertEquals(4, problem.decompositionSteps.size)
        assertEquals("Base = 1000", problem.decompositionSteps[0].stepResult)
        assertEquals("63", problem.decompositionSteps[1].stepResult)
        assertEquals("6", problem.decompositionSteps[2].stepResult)
        assertEquals("636", problem.decompositionSteps[3].stepResult)
    }

    @Test
    fun test100Minus37Produces63() {
        val p = generator.generateSpecificProblem(37L)
        assertEquals(63L, p.correctAnswer)
        assertEquals(100L - 37L, p.correctAnswer)
    }

    @Test
    fun test1000Minus346Produces654() {
        val p = generator.generateSpecificProblem(346L)
        assertEquals(654L, p.correctAnswer)
        assertEquals(1000L - 346L, p.correctAnswer)
    }

    @Test
    fun test10000Minus3468Produces6532() {
        val p = generator.generateSpecificProblem(3468L)
        assertEquals(6532L, p.correctAnswer)
        assertEquals(10000L - 3468L, p.correctAnswer)
    }

    @Test
    fun testHandshakeProblem1000Minus427Produces573() {
        val p = generator.generateSpecificProblem(427L)
        assertEquals(573L, p.correctAnswer)
        assertEquals(1000L - 427L, p.correctAnswer)
        // Leading digits complement: 9-4=5, 9-2=7 -> "57"
        // Final digit complement: 10-7=3 -> "3"
        assertEquals("57", p.decompositionSteps[1].stepResult)
        assertEquals("3", p.decompositionSteps[2].stepResult)
    }

    // ── 3. Dialogue tree structure ─────────────────────────────────────────────

    @Test
    fun testWorld2HasExactlyFourNodes() {
        assertEquals(
            "World 2 tutorial must have exactly 4 nodes",
            4,
            world2Tree.dialogueNodes.size
        )
    }

    @Test
    fun testWorld2PassesRiveDialogueValidator() {
        val result = RiveDialogueValidator.validate(world2Tree)
        assertTrue("World 2 must pass validator: ${result.errors}", result.isValid)
        assertTrue("Validation errors must be empty", result.errors.isEmpty())
    }

    @Test
    fun testAllNodeTextsNonBlank() {
        for (node in world2Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} text must not be blank", node.text.isNotBlank())
        }
    }

    @Test
    fun testAllMathOverlayExpressionsNonBlank() {
        for (node in world2Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId} math_overlay.expression must not be blank",
                node.mathOverlay.expression.isNotBlank()
            )
        }
    }

    @Test
    fun testAllNodesUseValidAnimations() {
        for (node in world2Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId}: animation '${node.riveState.animation}' must be in whitelist for ${node.riveState.character}",
                RiveAnimationConfig.isValidAnimation(node.riveState.character, node.riveState.animation)
            )
        }
    }

    @Test
    fun testSpeakerMatchesCharacterForAllNodes() {
        for (node in world2Tree.dialogueNodes) {
            assertEquals(
                "Node ${node.nodeId}: speaker must match rive_state.character",
                node.speaker,
                node.riveState.character
            )
        }
    }

    @Test
    fun testNoGuruWinkAnimation() {
        for (node in world2Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId}: Guru must never use wink animation",
                node.riveState.animation != "guru_wink"
            )
        }
    }

    // ── 4. Tutorial content alignment ─────────────────────────────────────────

    @Test
    fun testDemonstrationNodeReferences636() {
        // Node 3 (index 2) is the demonstration beat; it must show 636
        val demo = world2Tree.dialogueNodes[2]
        assertTrue(
            "Demonstration node (Node 3) must reference '636'. Text='${demo.text}', Overlay='${demo.mathOverlay.expression}'",
            demo.text.contains("636") || demo.mathOverlay.expression.contains("636")
        )
    }

    @Test
    fun testDemonstrationNodeDoesNotRevealHandshakeOperand427() {
        val demo = world2Tree.dialogueNodes[2]
        assertFalse(
            "Demonstration node must not reveal handshake operand 427",
            demo.text.contains("427") || demo.mathOverlay.expression.contains("427")
        )
    }

    @Test
    fun testDemonstrationNodeDoesNotRevealHandshakeAnswer3() {
        val demo = world2Tree.dialogueNodes[2]
        assertFalse(
            "Demonstration node must not reveal handshake answer '3'",
            demo.text.contains("10 - 7 = 3") || demo.mathOverlay.expression.contains("= 3")
        )
    }

    @Test
    fun testHandshakeNodeDoesNotRevealCompleteAnswer573() {
        // Node 4 (index 3) is the handshake beat; it must NOT pre-reveal the complete difference
        val handshakeNode = world2Tree.dialogueNodes.last()
        assertFalse(
            "Handshake node expression must NOT contain '573'. Found: '${handshakeNode.mathOverlay.expression}'",
            handshakeNode.mathOverlay.expression.contains("573")
        )
    }

    // ── 5. Handshake coherence ─────────────────────────────────────────────────

    @Test
    fun testFinalNodeRequiresUserTap() {
        assertTrue("Final node must require user tap", world2Tree.dialogueNodes.last().interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testHandshakeExpectedAnswerIsExactly3() {
        val finalNode = world2Tree.dialogueNodes.last()
        val answer = finalNode.interactiveHandshake.expectedAnswer
        assertNotNull("Handshake expectedAnswer must not be null", answer)
        assertEquals(
            "1000 - 427 handshake must expect final digit 10-7=3, not 573 or 7",
            "3",
            answer
        )
    }

    @Test
    fun testHandshakePromptIsNonBlank() {
        val finalNode = world2Tree.dialogueNodes.last()
        val prompt = finalNode.interactiveHandshake.promptText
        assertNotNull("Handshake prompt text must not be null", prompt)
        assertTrue("Handshake prompt text must not be blank", prompt!!.isNotBlank())
    }

    @Test
    fun testHandshakeTargetElementIdIsNumpadKey3() {
        val finalNode = world2Tree.dialogueNodes.last()
        val tid = finalNode.interactiveHandshake.targetElementId
        assertNotNull("Handshake targetElementId must not be null", tid)
        assertEquals("numpad_key_3", tid)
    }

    @Test
    fun testHandshakeExpressionContainsQuestionMark() {
        val finalNode = world2Tree.dialogueNodes.last()
        assertTrue(
            "Handshake math_overlay must contain '?' to indicate missing value. Got: '${finalNode.mathOverlay.expression}'",
            finalNode.mathOverlay.expression.contains("?")
        )
    }

    @Test
    fun testGenerateHandshakeChoicesContainsExpectedAnswer3() {
        val choices = generateHandshakeChoices("3")
        assertEquals(5, choices.size)
        assertTrue("Choices must contain expected answer 3: $choices", choices.contains("3"))
        assertEquals(listOf("1", "2", "3", "4", "5"), choices)
    }

    // ── 6. P0.1 regression — fail-closed ──────────────────────────────────────

    @Test
    fun testP01_NullExpectedAnswerAlwaysFails() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_3",
            promptText = "Some prompt",
            expectedAnswer = null
        )
        assertFalse("P0.1: null expectedAnswer must fail for '3'", VedicMathValidator.evaluateHandshake(handshake, "3"))
        assertFalse("P0.1: null expectedAnswer must fail for ''", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("P0.1: null expectedAnswer must fail for any input", VedicMathValidator.evaluateHandshake(handshake, "anything"))
    }

    @Test
    fun testP01_BlankExpectedAnswerAlwaysFails() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_3",
            promptText = "Some prompt",
            expectedAnswer = "   "
        )
        assertFalse("P0.1: blank expectedAnswer must fail for '3'", VedicMathValidator.evaluateHandshake(handshake, "3"))
    }

    // ── 7. P1.1 regression — expectedAnswer is the authority ──────────────────

    @Test
    fun testP11_ExpectedAnswerIsTheAuthority() {
        val finalNode = world2Tree.dialogueNodes.last()
        val handshake = finalNode.interactiveHandshake

        assertTrue(
            "Submitting expected answer '3' must pass",
            VedicMathValidator.evaluateHandshake(handshake, "3")
        )
        assertTrue(
            "Whitespace-padded ' 3 ' must pass",
            VedicMathValidator.evaluateHandshake(handshake, " 3 ")
        )
        assertFalse(
            "Submitting complete difference '573' must fail because target is final digit '3'",
            VedicMathValidator.evaluateHandshake(handshake, "573")
        )
        assertFalse(
            "Submitting subtrahend final digit '7' must fail",
            VedicMathValidator.evaluateHandshake(handshake, "7")
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
        val controller = RiveDialogueController(world2Tree, adapter)

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

        // Submit correct answer "3"
        val success = controller.submitHandshake("3")
        assertTrue("Correct answer must succeed", success)
        assertTrue("Tutorial must be completed", controller.isCompleted.value)
    }

    @Test
    fun testHandshakeFailureStatePreservesNodeAndGeneratesCorrectiveError() {
        val controller = RiveDialogueController(world2Tree, DefaultRiveAdapter())
        controller.advanceNode() // to node 2
        controller.advanceNode() // to node 3
        controller.advanceNode() // to node 4 (handshake)

        val wrongInputs = listOf("573", "7", "0", "42", "invalid")
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
        val controller = RiveDialogueController(world2Tree, DefaultRiveAdapter())
        assertTrue("Tree has unresolved handshake", controller.hasUnresolvedHandshake())

        // Attempting to skip without explicit opt-out must fail closed
        val skipped = controller.skip(allowBypassHandshake = false)
        assertFalse("skip() must return false when handshake is unresolved", skipped)
        assertFalse("isCompleted must remain false", controller.isCompleted.value)
    }

    @Test
    fun testProgressionPolicy_ExplicitOptOutBypassesHandshake() {
        val controller = RiveDialogueController(world2Tree, DefaultRiveAdapter())
        val skipped = controller.skip(allowBypassHandshake = true)
        assertTrue("Explicit skip opt-out must succeed", skipped)
        assertTrue("isCompleted must be true", controller.isCompleted.value)
    }

    @Test
    fun testProgressionPolicy_SkipAllowedAfterHandshakeResolved() {
        val controller = RiveDialogueController(world2Tree, DefaultRiveAdapter())
        controller.advanceNode() // 1
        controller.advanceNode() // 2
        controller.advanceNode() // 3 (handshake)
        assertTrue(controller.submitHandshake("3"))
        assertTrue(controller.isCompleted.value)
        assertFalse(controller.hasUnresolvedHandshake())

        // Calling skip when already completed succeeds
        assertTrue(controller.skip())
        assertTrue(controller.isCompleted.value)
    }

    // ── 9. Isolation: other generators & World 1 unaffected ─────────────────────

    @Test
    fun testOtherGeneratorsSutraNamesUnaffected() {
        assertEquals(
            "Ekadhikena sutra name must be unchanged",
            "Ekadhikena Purvena",
            com.ankh.sutrasaga.engine.EkadhikenaPurvenaGenerator().sutraName
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

    // ── 10. Focused World 2 Tutorial Enhancements ─────────────────────────────

    @Test
    fun testDialogueAndMathOverlayConsistency() {
        val node1 = world2Tree.dialogueNodes[0]
        assertTrue("Node 1 text must mention 1000 - 364", node1.text.contains("1000 - 364"))
        assertTrue("Node 1 overlay must contain 1000 - 364", node1.mathOverlay.expression.contains("1000 - 364"))

        val node2 = world2Tree.dialogueNodes[1]
        assertFalse("Node 2 must not use absolute statement 'Never borrow across zeros!'", node2.text.contains("Never borrow"))
        assertTrue("Node 2 text must explain Nikhilam as alternative to tedious borrowing", node2.text.contains("tedious borrowing", ignoreCase = true))
        assertTrue("Node 2 text must preserve sutra rule", node2.text.contains("All from 9", ignoreCase = true))
        assertTrue("Node 2 overlay must show complement formula", node2.mathOverlay.expression.contains("(9-3)(9-6)(10-4)"))

        val node3 = world2Tree.dialogueNodes[2]
        assertTrue("Node 3 text must state 636", node3.text.contains("636"))
        assertTrue("Node 3 overlay must produce 636", node3.mathOverlay.expression.contains("636"))

        val node4 = world2Tree.dialogueNodes[3]
        assertTrue("Node 4 text must ask for 1000 - 427", node4.text.contains("1000 - 427"))
        assertTrue("Node 4 overlay must contain missing step '?'", node4.mathOverlay.expression.contains("?"))
        assertFalse("Node 4 overlay must not reveal 573", node4.mathOverlay.expression.contains("573"))
    }

    @Test
    fun testMathematicalInvariantForNikhilamBaseSubtraction() {
        // For non-zero-ending numbers S < Base:
        // Base - S = (9-d_k)...(9-d_1)(10-d_0)
        val testCases = listOf(
            Pair(100L, 37L),
            Pair(100L, 82L),
            Pair(100L, 19L),
            Pair(1000L, 364L),
            Pair(1000L, 427L),
            Pair(1000L, 781L),
            Pair(10000L, 3468L),
            Pair(10000L, 8912L)
        )

        for ((base, subtrahend) in testCases) {
            val sStr = subtrahend.toString()
            val initial = sStr.dropLast(1).map { (9 - it.digitToInt()).toString() }.joinToString("")
            val last = (10 - sStr.last().digitToInt()).toString()
            val expectedComplement = (initial + last).toLong()

            val actualDifference = base - subtrahend
            assertEquals(
                "Invariant failure for base $base and subtrahend $subtrahend",
                actualDifference,
                expectedComplement
            )
            val problem = generator.generateSpecificProblem(subtrahend)
            assertEquals(actualDifference, problem.correctAnswer)
        }
    }
}
