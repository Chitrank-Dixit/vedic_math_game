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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

/**
 * P1 World 1 Tutorial Tests — Ekadhikena Purvena (Refined)
 *
 * Covers:
 * - Four-beat dialogue tree structure and schema compliance
 * - Sutra name and meaning correctness
 * - 35^2 demonstration produces 1225
 * - Additional math verification: 15^2=225, 25^2=625, 45^2=2025, 105^2=11025
 * - 45^2 handshake: expected answer is "20" (product 4x5), not "5" or "2025"
 * - Handshake expression does NOT reveal complete answer 2025
 * - P0.1 regression: null/blank expectedAnswer fails closed
 * - P1.1 regression: submitting 2025 or 5 fails when expected is 20
 * - Controller: incorrect answer does NOT advance tutorial
 * - Controller: correct answer "20" completes tutorial
 * - Other world generator sutra names are unchanged
 */
@RunWith(RobolectricTestRunner::class)
class EkadhikenaPurvenaWorld1TutorialTest {

    private lateinit var generator: EkadhikenaPurvenaGenerator
    private lateinit var world1Tree: RiveSutraDialogueTree
    private val repository = RiveDialogueRepository()

    @Before
    fun setUp() {
        generator = EkadhikenaPurvenaGenerator()

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
        val loaded = repository.getDialogueForWorld(1)
        assertNotNull("World 1 dialogue tree must exist in asset file", loaded)
        world1Tree = loaded!!
    }

    // ── 1. Sutra identity ──────────────────────────────────────────────────────

    @Test
    fun testSutraNameIsEkadhikenaPurvena() {
        assertEquals("Ekadhikena Purvena", generator.sutraName)
    }

    @Test
    fun testDialogueTreeSutraId() {
        assertEquals("ekadhikena_purvena", world1Tree.sutraId)
    }

    @Test
    fun testDialogueTreeEnglishMeaningRefersToOneMore() {
        assertTrue(
            "English meaning must reference 'One More': '${world1Tree.englishMeaning}'",
            world1Tree.englishMeaning.contains("One More", ignoreCase = true)
        )
    }

    @Test
    fun testDialogueTreeWorldNumber() {
        assertEquals(1, world1Tree.worldNumber)
    }

    // ── 2. Mathematical correctness ────────────────────────────────────────────

    @Test
    fun test35SquaredProduces1225() {
        val problem = generator.generateSpecificProblem(35L)
        assertEquals(35L, problem.operand)
        assertEquals(3L, problem.prefixPart)
        assertEquals(4L, problem.incrementedPrefix)
        assertEquals(12L, problem.prefixProduct)
        assertEquals(1225L, problem.correctAnswer)
        // Independent arithmetic check
        assertEquals(1225L, 35L * 35L)
        assertEquals("Generator must agree with N^2", 35L * 35L, problem.correctAnswer)
    }

    @Test
    fun test15SquaredProduces225() {
        val p = generator.generateSpecificProblem(15L)
        assertEquals(225L, p.correctAnswer)
        assertEquals(15L * 15L, p.correctAnswer)
    }

    @Test
    fun test25SquaredProduces625() {
        val p = generator.generateSpecificProblem(25L)
        assertEquals(625L, p.correctAnswer)
        assertEquals(25L * 25L, p.correctAnswer)
    }

    @Test
    fun test45SquaredProduces2025() {
        val p = generator.generateSpecificProblem(45L)
        assertEquals(2025L, p.correctAnswer)
        assertEquals(45L * 45L, p.correctAnswer)
    }

    @Test
    fun test105SquaredProduces11025() {
        val p = generator.generateSpecificProblem(105L)
        assertEquals(11025L, p.correctAnswer)
        assertEquals(105L * 105L, p.correctAnswer)
    }

    // ── 3. Dialogue tree structure ─────────────────────────────────────────────

    @Test
    fun testWorld1HasExactlyFourNodes() {
        assertEquals(
            "World 1 tutorial must have exactly 4 nodes",
            4,
            world1Tree.dialogueNodes.size
        )
    }

    @Test
    fun testWorld1PassesRiveDialogueValidator() {
        val result = RiveDialogueValidator.validate(world1Tree)
        assertTrue("World 1 must pass validator: ${result.errors}", result.isValid)
        assertTrue("Validation errors must be empty", result.errors.isEmpty())
    }

    @Test
    fun testAllNodeTextsNonBlank() {
        for (node in world1Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} text must not be blank", node.text.isNotBlank())
        }
    }

    @Test
    fun testAllMathOverlayExpressionsNonBlank() {
        for (node in world1Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId} math_overlay.expression must not be blank",
                node.mathOverlay.expression.isNotBlank()
            )
        }
    }

    @Test
    fun testAllNodesUseValidAnimations() {
        for (node in world1Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId}: animation '${node.riveState.animation}' must be in whitelist for ${node.riveState.character}",
                RiveAnimationConfig.isValidAnimation(node.riveState.character, node.riveState.animation)
            )
        }
    }

    @Test
    fun testSpeakerMatchesCharacterForAllNodes() {
        for (node in world1Tree.dialogueNodes) {
            assertEquals(
                "Node ${node.nodeId}: speaker must match rive_state.character",
                node.speaker,
                node.riveState.character
            )
        }
    }

    @Test
    fun testNoGuruWinkAnimation() {
        for (node in world1Tree.dialogueNodes) {
            assertTrue(
                "Node ${node.nodeId}: Guru must never use wink animation",
                node.riveState.animation != "guru_wink"
            )
        }
    }

    // ── 4. Tutorial content alignment ─────────────────────────────────────────

    @Test
    fun testDemonstrationNodeReferences1225() {
        // Node 3 (index 2) is the demonstration beat; it must show 1225
        val demo = world1Tree.dialogueNodes[2]
        assertTrue(
            "Demonstration node (Node 3) must reference '1225'. Text='${demo.text}', Overlay='${demo.mathOverlay.expression}'",
            demo.text.contains("1225") || demo.mathOverlay.expression.contains("1225")
        )
    }

    @Test
    fun testDemonstrationNodeDoesNotRevealHandshakeAnswer20() {
        val demo = world1Tree.dialogueNodes[2]
        assertFalse(
            "Demonstration node must not reveal handshake answer '20'",
            demo.text.contains("= 20") || demo.mathOverlay.expression.contains("= 20")
        )
    }

    @Test
    fun testHandshakeNodeDoesNotReveal2025() {
        // Node 4 (index 3) is the handshake beat; it must NOT pre-reveal the complete square
        val handshakeNode = world1Tree.dialogueNodes.last()
        assertFalse(
            "Handshake node expression must NOT contain '2025'. Found: '${handshakeNode.mathOverlay.expression}'",
            handshakeNode.mathOverlay.expression.contains("2025")
        )
    }

    // ── 5. Handshake coherence ─────────────────────────────────────────────────

    @Test
    fun testFinalNodeRequiresUserTap() {
        assertTrue("Final node must require user tap", world1Tree.dialogueNodes.last().interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testHandshakeExpectedAnswerIsExactly20() {
        val finalNode = world1Tree.dialogueNodes.last()
        val answer = finalNode.interactiveHandshake.expectedAnswer
        assertNotNull("Handshake expectedAnswer must not be null", answer)
        assertEquals(
            "45^2 handshake must expect product 4x(4+1)=20, not 5 or 2025",
            "20",
            answer
        )
    }

    @Test
    fun testHandshakePromptIsNonBlank() {
        val finalNode = world1Tree.dialogueNodes.last()
        val prompt = finalNode.interactiveHandshake.promptText
        assertNotNull("Handshake prompt text must not be null", prompt)
        assertTrue("Handshake prompt text must not be blank", prompt!!.isNotBlank())
    }

    @Test
    fun testHandshakeTargetElementIdIsNonBlank() {
        val finalNode = world1Tree.dialogueNodes.last()
        val tid = finalNode.interactiveHandshake.targetElementId
        assertNotNull("Handshake targetElementId must not be null", tid)
        assertTrue("Handshake targetElementId must not be blank", tid!!.isNotBlank())
    }

    @Test
    fun testHandshakeExpressionContainsQuestionMark() {
        val finalNode = world1Tree.dialogueNodes.last()
        assertTrue(
            "Handshake math_overlay must contain '?' to indicate missing value. Got: '${finalNode.mathOverlay.expression}'",
            finalNode.mathOverlay.expression.contains("?")
        )
    }

    // ── 6. P0.1 regression — fail-closed ──────────────────────────────────────

    @Test
    fun testP01_NullExpectedAnswerAlwaysFails() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_1",
            promptText = "Some prompt",
            expectedAnswer = null
        )
        assertFalse("P0.1: null expectedAnswer must fail for '20'", VedicMathValidator.evaluateHandshake(handshake, "20"))
        assertFalse("P0.1: null expectedAnswer must fail for ''", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("P0.1: null expectedAnswer must fail for any input", VedicMathValidator.evaluateHandshake(handshake, "anything"))
    }

    @Test
    fun testP01_BlankExpectedAnswerAlwaysFails() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_1",
            promptText = "Some prompt",
            expectedAnswer = "   "
        )
        assertFalse("P0.1: blank expectedAnswer must fail for '20'", VedicMathValidator.evaluateHandshake(handshake, "20"))
    }

    // ── 7. P1.1 regression — expectedAnswer is the authority ──────────────────

    @Test
    fun testP11_CorrectAnswerPasses() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_20",
            promptText = "What is 4 x (4 + 1)? Enter the product.",
            expectedAnswer = "20"
        )
        assertTrue("'20' must pass when expectedAnswer is '20'", VedicMathValidator.evaluateHandshake(handshake, "20"))
    }

    @Test
    fun testP11_Submitting2025FailsWhenExpecting20() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_20",
            promptText = "What is 4 x (4 + 1)? Enter the product.",
            expectedAnswer = "20"
        )
        assertFalse(
            "P1.1: '2025' (full 45^2) must FAIL when expectedAnswer='20'",
            VedicMathValidator.evaluateHandshake(handshake, "2025")
        )
    }

    @Test
    fun testP11_Submitting5FailsWhenExpecting20() {
        val handshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_20",
            promptText = "What is 4 x (4 + 1)? Enter the product.",
            expectedAnswer = "20"
        )
        assertFalse(
            "P1.1: '5' (sutra increment) must FAIL when expectedAnswer='20'",
            VedicMathValidator.evaluateHandshake(handshake, "5")
        )
    }

    // ── 8. Controller behaviour ────────────────────────────────────────────────

    @Test
    fun testIncorrectHandshakeDoesNotAdvanceTutorial() {
        val controller = RiveDialogueController(world1Tree, DefaultRiveAdapter())

        // Advance through nodes 1 -> 2 -> 3 to arrive at Node 4 (handshake)
        assertTrue("Advance to node 2", controller.advanceNode())
        assertTrue("Advance to node 3", controller.advanceNode())
        assertTrue("Advance to node 4 (handshake)", controller.advanceNode())

        assertFalse("Dialog must not be complete before handshake", controller.isCompleted.value)
        assertEquals("Must be on node index 3 (0-based)", 3, controller.currentNodeIndex.value)

        val result = controller.submitHandshake("5")
        assertFalse("Wrong answer '5' must not pass", result)
        assertFalse("Tutorial must NOT be completed after wrong answer", controller.isCompleted.value)
        assertEquals("Must remain on node 3 after wrong answer", 3, controller.currentNodeIndex.value)
        assertNotNull("Handshake error must be set after wrong answer", controller.handshakeError.value)
    }

    @Test
    fun testCorrectHandshakeCompletesDialogue() {
        val controller = RiveDialogueController(world1Tree, DefaultRiveAdapter())

        // Advance through nodes 1 -> 2 -> 3 to arrive at Node 4 (handshake)
        assertTrue("Advance to node 2", controller.advanceNode())
        assertTrue("Advance to node 3", controller.advanceNode())
        assertTrue("Advance to node 4 (handshake)", controller.advanceNode())

        assertFalse("Dialog must not be complete before handshake", controller.isCompleted.value)

        val result = controller.submitHandshake("20")
        assertTrue("Correct answer '20' must pass the handshake", result)
        assertTrue("Tutorial must be completed after correct answer", controller.isCompleted.value)
    }

    @Test
    fun testHandshakeNodeBlocksAdvanceNode() {
        val controller = RiveDialogueController(world1Tree, DefaultRiveAdapter())
        // Advance to the handshake node
        controller.advanceNode()
        controller.advanceNode()
        controller.advanceNode()
        assertEquals("Must be on node 3", 3, controller.currentNodeIndex.value)

        // advanceNode() must return false while handshake is pending
        val advanced = controller.advanceNode()
        assertFalse("advanceNode() must block on active handshake", advanced)
        assertEquals("Index must not change", 3, controller.currentNodeIndex.value)
    }

    // ── 9. Other world generators guard ───────────────────────────────────────

    @Test
    fun testOtherWorldGeneratorsAreUnchanged() {
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
    fun testEkadhikenaPurvenaGeneratorIntactFor65Squared() {
        // Generator must still work correctly for all operands, including 65
        val p = generator.generateSpecificProblem(65L)
        assertEquals(65L, p.operand)
        assertEquals(6L, p.prefixPart)
        assertEquals(7L, p.incrementedPrefix)
        assertEquals(42L, p.prefixProduct)
        assertEquals(4225L, p.correctAnswer)
        assertEquals(65L * 65L, p.correctAnswer)
    }

    // ── 10. Focused World 1 Tutorial Enhancements ─────────────────────────────

    @Test
    fun testCanonicalTutorialProblemGeneration() {
        val canonicalProblem = generator.generateCanonicalTutorialProblem()
        assertEquals(35L, canonicalProblem.operand)
        assertEquals(3L, canonicalProblem.prefixPart)
        assertEquals(4L, canonicalProblem.incrementedPrefix)
        assertEquals(12L, canonicalProblem.prefixProduct)
        assertEquals(1225L, canonicalProblem.correctAnswer)
        assertEquals("25", canonicalProblem.appendedSuffix)
        assertEquals(4, canonicalProblem.decompositionSteps.size)
        assertEquals("3", canonicalProblem.decompositionSteps[0].stepResult)
        assertEquals("4", canonicalProblem.decompositionSteps[1].stepResult)
        assertEquals("12", canonicalProblem.decompositionSteps[2].stepResult)
        assertEquals("1225", canonicalProblem.decompositionSteps[3].stepResult)
    }

    @Test
    fun testDialogueAndMathOverlayConsistency() {
        val node1 = world1Tree.dialogueNodes[0]
        assertTrue("Node 1 text must mention 35", node1.text.contains("35"))
        assertTrue("Node 1 overlay must contain 35", node1.mathOverlay.expression.contains("35"))

        val node2 = world1Tree.dialogueNodes[1]
        assertTrue("Node 2 text must explain sutra meaning", node2.text.contains("One More", ignoreCase = true))
        assertTrue("Node 2 overlay must show prefix multiplication", node2.mathOverlay.expression.contains("35²"))

        val node3 = world1Tree.dialogueNodes[2]
        assertTrue("Node 3 text must state 1225", node3.text.contains("1225"))
        assertTrue("Node 3 overlay must produce 1225", node3.mathOverlay.expression.contains("1225"))

        val node4 = world1Tree.dialogueNodes[3]
        assertTrue("Node 4 text must reference 45²", node4.text.contains("45²"))
        assertTrue("Node 4 text must ask for prefix product 4 × (4 + 1)", node4.text.contains("4 × (4 + 1)"))
        assertTrue("Node 4 prompt must ask for prefix product 4 × (4 + 1)", node4.interactiveHandshake.promptText!!.contains("4 × (4 + 1)"))
        assertEquals("20", node4.interactiveHandshake.expectedAnswer)
        assertTrue("Node 4 overlay must contain missing step '?'", node4.mathOverlay.expression.contains("?"))
        assertFalse("Node 4 overlay must not reveal 2025", node4.mathOverlay.expression.contains("2025"))
    }

    @Test
    fun testHandshakeSemanticAgreementBetweenVisibleQuestionAndExpectedResponse() {
        val node4 = world1Tree.dialogueNodes.last()
        // Text, SSML, overlay, and prompt must all consistently ask for 4 × (4 + 1)
        assertTrue("Visible text asks for 4 × (4 + 1)", node4.text.contains("4 × (4 + 1)"))
        assertTrue("SSML asks for 4 times open-parenthesis 4 plus 1 close-parenthesis", node4.audioCues.ssmlText.contains("4 times open-parenthesis 4 plus 1 close-parenthesis"))
        assertTrue("Math overlay contains 4 × (4 + 1)", node4.mathOverlay.expression.contains("4 × (4 + 1)"))
        assertTrue("Prompt asks for 4 × (4 + 1)", node4.interactiveHandshake.promptText!!.contains("4 × (4 + 1)"))
        // Authoritative expected answer must remain 20 (4 * 5 = 20), not 2025
        assertEquals("20", node4.interactiveHandshake.expectedAnswer)
        // Mathematical truth check
        assertEquals(20L, 4L * (4L + 1L))
    }

    @Test
    fun testProgressionPolicy_SkipBlockedWhenHandshakeUnresolved() {
        val controller = RiveDialogueController(world1Tree, DefaultRiveAdapter())
        assertTrue("Tree has unresolved handshake", controller.hasUnresolvedHandshake())

        // Attempting to skip without explicit opt-out must fail closed
        val skipped = controller.skip(allowBypassHandshake = false)
        assertFalse("skip() must return false when handshake is unresolved", skipped)
        assertFalse("isCompleted must remain false", controller.isCompleted.value)
    }

    @Test
    fun testProgressionPolicy_ExplicitOptOutBypassesHandshake() {
        val controller = RiveDialogueController(world1Tree, DefaultRiveAdapter())
        val skipped = controller.skip(allowBypassHandshake = true)
        assertTrue("Explicit skip opt-out must succeed", skipped)
        assertTrue("isCompleted must be true", controller.isCompleted.value)
    }

    @Test
    fun testProgressionPolicy_SkipAllowedAfterHandshakeResolved() {
        val controller = RiveDialogueController(world1Tree, DefaultRiveAdapter())
        controller.advanceNode() // 1
        controller.advanceNode() // 2
        controller.advanceNode() // 3 (handshake)
        assertTrue(controller.submitHandshake("20"))
        assertTrue(controller.isCompleted.value)
        assertFalse(controller.hasUnresolvedHandshake())

        // Calling skip when already completed succeeds
        assertTrue(controller.skip())
        assertTrue(controller.isCompleted.value)
    }

    @Test
    fun testMathematicalInvariantForSquaringEndingIn5() {
        // (10n + 5)² = 100n(n + 1) + 25
        for (n in 1L..20L) {
            val operand = n * 10L + 5L
            val expectedSquare = operand * operand
            val algebraicFormulaResult = 100L * n * (n + 1L) + 25L
            assertEquals(
                "Invariant failure for operand $operand",
                expectedSquare,
                algebraicFormulaResult
            )
            val problem = generator.generateSpecificProblem(operand)
            assertEquals(expectedSquare, problem.correctAnswer)
        }
    }

    @Test
    fun testHandshakeFailureStatePreservesNodeAndGeneratesCorrectiveError() {
        val controller = RiveDialogueController(world1Tree, DefaultRiveAdapter())
        controller.advanceNode() // to node 2
        controller.advanceNode() // to node 3
        controller.advanceNode() // to node 4 (handshake)

        val wrongInputs = listOf("2025", "5", "10", "42", "invalid")
        for (wrong in wrongInputs) {
            val success = controller.submitHandshake(wrong)
            assertFalse("Input '$wrong' must fail handshake", success)
            assertFalse("Tutorial must remain incomplete", controller.isCompleted.value)
            assertEquals("Current node index must remain at 3", 3, controller.currentNodeIndex.value)
            assertNotNull("Handshake error message must be populated", controller.handshakeError.value)
            assertTrue("Handshake error should mention prompt or step", controller.handshakeError.value!!.isNotBlank())
        }
    }
}
