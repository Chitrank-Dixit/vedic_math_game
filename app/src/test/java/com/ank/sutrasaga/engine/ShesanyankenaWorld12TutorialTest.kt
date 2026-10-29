package com.ank.sutrasaga.engine

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ank.sutrasaga.data.repository.RiveDialogueRepository
import com.ank.sutrasaga.domain.models.InteractiveHandshake
import com.ank.sutrasaga.domain.models.RiveAnimationConfig
import com.ank.sutrasaga.domain.models.RiveSpeaker
import com.ank.sutrasaga.domain.models.RiveSutraDialogueTree
import com.ank.sutrasaga.domain.models.SutraProblem
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
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ShesanyankenaWorld12TutorialTest {

    private lateinit var context: Context
    private lateinit var repository: RiveDialogueRepository
    private lateinit var world12Tree: RiveSutraDialogueTree
    private lateinit var generator: ShesanyankenaCharamenaGenerator

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = RiveDialogueRepository(context)
        val tree = repository.getDialogueForWorld(12)
        assertNotNull("World 12 dialogue must exist in repository", tree)
        world12Tree = tree!!
        generator = ShesanyankenaCharamenaGenerator()
    }

    // ── 1. Sutra Identification & Metadata ──────────────────────────────────────

    @Test
    fun testWorld12SutraIdentification() {
        assertEquals("sheshanyankena_charamena", world12Tree.sutraId)
        assertEquals("Śeṣāṇyaṅkena Carameṇa", world12Tree.sutraName)
        assertEquals("The Remainders by the Last Digit", world12Tree.englishMeaning)
        assertEquals(12, world12Tree.worldNumber)
    }

    // ── 2. Mathematical Correctness & Independent Verification ──────────────────

    @Test
    fun testCanonicalTutorialProblemProducesSeventhExpansion() {
        val problem: SutraProblem = generator.generateCanonicalTutorialProblem()
        assertEquals(1L, problem.correctAnswer)
        assertEquals("Shesanyankena Charamena", problem.sutraName)
        assertEquals("0.(142857)", problem.appendedSuffix)
        assertEquals(6L, problem.prefixProduct) // repetend length = 6

        // Independent verification of 1/7 expansion via remainder chain
        assertTrue(
            "VedicMathValidator must verify 1/7 expansion with period 6 and repetend 142857",
            VedicMathValidator.verifyShesanyankenaExpansion(
                p = 1L,
                q = 7L,
                expectedPeriodLength = 6,
                expectedRepetend = "142857"
            )
        )
    }

    @Test
    fun testIntermediateStepRemainderTracking() {
        val q = 7L
        var r = 1L

        // Step 1: 10 * 1 = 10 -> 10 / 7 = 1 r 3
        var v = r * 10L
        assertEquals(10L, v)
        var d = v / q
        assertEquals(1L, d)
        r = v % q
        assertEquals(3L, r)

        // Step 2: 10 * 3 = 30 -> 30 / 7 = 4 r 2
        v = r * 10L
        assertEquals(30L, v)
        d = v / q
        assertEquals(4L, d)
        r = v % q
        assertEquals(2L, r)

        // Step 3 (Handshake step): 10 * 2 = 20 -> 20 / 7 = 2 r 6
        v = r * 10L
        assertEquals(20L, v)
        d = v / q
        assertEquals(2L, d)
        r = v % q
        assertEquals(6L, r) // Handshake answer!

        // Step 4: 10 * 6 = 60 -> 60 / 7 = 8 r 4
        v = r * 10L
        assertEquals(60L, v)
        d = v / q
        assertEquals(8L, d)
        r = v % q
        assertEquals(4L, r)

        // Step 5: 10 * 4 = 40 -> 40 / 7 = 5 r 5
        v = r * 10L
        assertEquals(40L, v)
        d = v / q
        assertEquals(5L, d)
        r = v % q
        assertEquals(5L, r)

        // Step 6: 10 * 5 = 50 -> 50 / 7 = 7 r 1 (cycle back to start!)
        v = r * 10L
        assertEquals(50L, v)
        d = v / q
        assertEquals(7L, d)
        r = v % q
        assertEquals(1L, r)
    }

    @Test
    fun testHandshakeCalculationTwentyModSevenYieldsSix() {
        val incomingRemainder = 2L
        val multiplied = incomingRemainder * 10L
        assertEquals(20L, multiplied)
        val remainder = multiplied % 7L
        assertEquals(6L, remainder)
    }

    @Test
    fun test20HandVerifiedRecurringDecimalCases() {
        val cases = listOf(
            TestCase(1L, 7L, 6, "142857"),
            TestCase(2L, 7L, 6, "285714"),
            TestCase(3L, 7L, 6, "428571"),
            TestCase(4L, 7L, 6, "571428"),
            TestCase(5L, 7L, 6, "714285"),
            TestCase(6L, 7L, 6, "857142"),
            TestCase(1L, 3L, 1, "3"),
            TestCase(2L, 3L, 1, "6"),
            TestCase(1L, 9L, 1, "1"),
            TestCase(2L, 9L, 1, "2"),
            TestCase(4L, 9L, 1, "4"),
            TestCase(5L, 9L, 1, "5"),
            TestCase(7L, 9L, 1, "7"),
            TestCase(8L, 9L, 1, "8"),
            TestCase(1L, 11L, 2, "09"),
            TestCase(2L, 11L, 2, "18"),
            TestCase(3L, 11L, 2, "27"),
            TestCase(4L, 11L, 2, "36"),
            TestCase(5L, 11L, 2, "45"),
            TestCase(1L, 13L, 6, "076923")
        )

        for (c in cases) {
            assertTrue(
                "VedicMathValidator check for ${c.p}/${c.q}",
                VedicMathValidator.verifyShesanyankenaExpansion(c.p, c.q, c.period, c.repetend)
            )
        }
    }

    private data class TestCase(
        val p: Long,
        val q: Long,
        val period: Int,
        val repetend: String
    )

    // ── 3. Dialogue Tree Schema & 4 Beats ──────────────────────────────────────

    @Test
    fun testDialogueTreePassesValidator() {
        val result = RiveDialogueValidator.validate(world12Tree)
        assertTrue("World 12 dialogue tree must be valid: ${result.errors}", result.isValid)
        assertTrue("World 12 dialogue tree must have zero validation errors", result.errors.isEmpty())
    }

    @Test
    fun testDialogueTreeHasExactlyFourBeats() {
        assertEquals("World 12 must have exactly 4 dialogue nodes", 4, world12Tree.dialogueNodes.size)
    }

    @Test
    fun testNode1IsShishyaHook() {
        val node1 = world12Tree.dialogueNodes[0]
        assertEquals(1, node1.nodeId)
        assertEquals(RiveSpeaker.SHISHYA, node1.speaker)
        assertTrue("Node 1 text must not be blank", node1.text.isNotBlank())
        assertTrue("Node 1 text must mention 1/7 or repeating decimals", node1.text.contains("1/7") || node1.text.contains("repeating"))
        assertEquals("shishya_curious", node1.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.SHISHYA, node1.riveState.animation))
        assertFalse(node1.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode2IsGuruSutraReveal() {
        val node2 = world12Tree.dialogueNodes[1]
        assertEquals(2, node2.nodeId)
        assertEquals(RiveSpeaker.GURU, node2.speaker)
        assertTrue("Node 2 text must not be blank", node2.text.isNotBlank())
        assertTrue("Node 2 text must introduce Śeṣāṇyaṅkena Carameṇa", node2.text.contains("Śeṣāṇyaṅkena Carameṇa") || node2.text.contains("Remainders by the Last Digit"))
        assertEquals("guru_explain", node2.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node2.riveState.animation))
        assertFalse(node2.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode3IsGuruGuidedDemonstration() {
        val node3 = world12Tree.dialogueNodes[2]
        assertEquals(3, node3.nodeId)
        assertEquals(RiveSpeaker.GURU, node3.speaker)
        assertTrue("Node 3 text must not be blank", node3.text.isNotBlank())
        assertTrue("Node 3 text must explain remainder chain 3 -> 2 -> 6 -> 4 -> 5 -> 1 producing 0.(142857)", node3.text.contains("142857"))
        assertEquals("guru_explain", node3.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node3.riveState.animation))
        assertFalse(node3.interactiveHandshake.requiresUserTap)
    }

    @Test
    fun testNode4IsGuruActiveHandshake() {
        val node4 = world12Tree.dialogueNodes[3]
        assertEquals(4, node4.nodeId)
        assertEquals(RiveSpeaker.GURU, node4.speaker)
        assertTrue("Node 4 text must not be blank", node4.text.isNotBlank())
        assertTrue("Node 4 text must ask for the remainder 20 mod 7", node4.text.contains("20 mod 7") || node4.text.contains("remainder"))
        assertEquals("guru_magic_fx", node4.riveState.animation)
        assertTrue(RiveAnimationConfig.isValidAnimation(RiveSpeaker.GURU, node4.riveState.animation))
        assertTrue("Node 4 must require user tap", node4.interactiveHandshake.requiresUserTap)
        assertEquals("numpad_key_6", node4.interactiveHandshake.targetElementId)
        assertEquals("6", node4.interactiveHandshake.expectedAnswer)
    }

    @Test
    fun testGuruNeverWinks() {
        for (node in world12Tree.dialogueNodes) {
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
        for (node in world12Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} SSML must contain <speak>", node.audioCues.ssmlText.contains("<speak>"))
            assertTrue("Node ${node.nodeId} SSML must contain </speak>", node.audioCues.ssmlText.contains("</speak>"))
            assertTrue("Node ${node.nodeId} SFX must not be blank", node.audioCues.sfx.isNotBlank())
        }
    }

    @Test
    fun testAllNodesHaveNonBlankMathOverlays() {
        for (node in world12Tree.dialogueNodes) {
            assertTrue("Node ${node.nodeId} expression must not be blank", node.mathOverlay.expression.isNotBlank())
            assertTrue("Node ${node.nodeId} highlight tokens must not be empty", node.mathOverlay.highlightTokens.isNotEmpty())
        }
    }

    // ── 4. Active Handshake & Expected Answer Authority ────────────────────────

    @Test
    fun testHandshakeAuthoritativeAnswerIsSix() {
        val handshake = world12Tree.dialogueNodes.last().interactiveHandshake
        assertEquals("Authoritative expected answer must be '6'", "6", handshake.expectedAnswer)
    }

    @Test
    fun testHandshakePromptDoesNotRevealExpectedAnswer() {
        val handshake = world12Tree.dialogueNodes.last().interactiveHandshake
        val prompt = handshake.promptText
        assertNotNull("Prompt must not be null", prompt)
        assertFalse(
            "Prompt '$prompt' must not leak '= 6' or 'is 6' or 'Tap 6'",
            prompt!!.contains("= 6") || prompt.contains("is 6") || prompt.contains("Tap 6")
        )
    }

    @Test
    fun testHandshakeMathOverlayExpressionDoesNotRevealExpectedAnswer() {
        val finalNode = world12Tree.dialogueNodes.last()
        val expr = finalNode.mathOverlay.expression
        assertTrue("Overlay should indicate unknown: '$expr'", expr.contains("?") || expr.contains("[ ? ]"))
        assertFalse("Overlay must not leak '= 6': '$expr'", expr.contains("= 6"))
    }

    @Test
    fun testHandshakeChoicesIncludeExpectedAnswer() {
        val expected = world12Tree.dialogueNodes.last().interactiveHandshake.expectedAnswer
        val choices = generateHandshakeChoices(expected)
        assertTrue("Generated choices $choices must contain '$expected'", choices.contains(expected))
        assertEquals("Generated choices for '6' must be 5 items", 5, choices.size)
        assertEquals(listOf("4", "5", "6", "7", "8"), choices)
    }

    @Test
    fun testHandshakeFailsClosedOnNullOrBlankExpectedAnswer() {
        val nullAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_6",
            promptText = "Calculate the remainder: 20 mod 7.",
            expectedAnswer = null
        )
        assertFalse(
            "Null expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(nullAnswerHandshake, "6")
        )

        val blankAnswerHandshake = InteractiveHandshake(
            requiresUserTap = true,
            targetElementId = "numpad_key_6",
            promptText = "Calculate the remainder: 20 mod 7.",
            expectedAnswer = "   "
        )
        assertFalse(
            "Blank expected answer must fail validation",
            VedicMathValidator.evaluateHandshake(blankAnswerHandshake, "6")
        )
    }

    @Test
    fun testHandshakeEvaluatesStrictlyAgainstExpectedAnswer() {
        val handshake = world12Tree.dialogueNodes.last().interactiveHandshake

        assertTrue("Submitting '6' must pass", VedicMathValidator.evaluateHandshake(handshake, "6"))
        assertTrue("Submitting ' 6 ' with whitespace must pass", VedicMathValidator.evaluateHandshake(handshake, " 6 "))

        assertFalse("Submitting '4' must fail", VedicMathValidator.evaluateHandshake(handshake, "4"))
        assertFalse("Submitting '5' must fail", VedicMathValidator.evaluateHandshake(handshake, "5"))
        assertFalse("Submitting '7' must fail", VedicMathValidator.evaluateHandshake(handshake, "7"))
        assertFalse("Submitting '8' must fail", VedicMathValidator.evaluateHandshake(handshake, "8"))
        assertFalse("Submitting '2' must fail", VedicMathValidator.evaluateHandshake(handshake, "2"))
        assertFalse("Submitting '3' must fail", VedicMathValidator.evaluateHandshake(handshake, "3"))
        assertFalse("Submitting empty string must fail", VedicMathValidator.evaluateHandshake(handshake, ""))
        assertFalse("Submitting whitespace must fail", VedicMathValidator.evaluateHandshake(handshake, "   "))
    }

    // ── 5. Controller & Skip Behavior ──────────────────────────────────────────

    @Test
    fun testDialogueControllerAdvancesThroughFirstThreeNodes() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world12Tree, adapter)

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
        val controller = RiveDialogueController(world12Tree, adapter)

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
        val controller = RiveDialogueController(world12Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("5")
        assertFalse("Incorrect answer '5' must return false", success)
        assertFalse("Dialogue must not complete", controller.isCompleted.value)
        assertNotNull("Handshake error message must be set", controller.handshakeError.value)
        assertEquals("shishya_puzzled", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSubmittingCorrectAnswerCompletesDialogue() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world12Tree, adapter)

        repeat(3) { controller.advanceNode() }
        assertEquals(3, controller.currentNodeIndex.value)

        val success = controller.submitHandshake("6")
        assertTrue("Correct answer '6' must return true", success)
        assertTrue("Dialogue must be marked completed", controller.isCompleted.value)
        assertEquals("shishya_celebrate", adapter.shishyaAnimation.value)
    }

    @Test
    fun testDialogueControllerSkipBehavior() {
        // Case 1: allowBypassHandshake = false (default student path)
        val adapter1 = DefaultRiveAdapter()
        val controller1 = RiveDialogueController(world12Tree, adapter1)

        repeat(3) { controller1.advanceNode() }
        assertEquals(3, controller1.currentNodeIndex.value)

        val skipped1 = controller1.skip(allowBypassHandshake = false)
        assertFalse("skip must return false when handshake is unresolved and bypass is disabled", skipped1)
        assertFalse("Dialogue must not be completed", controller1.isCompleted.value)

        // Case 2: allowBypassHandshake = true (debug / reviewer bypass)
        val adapter2 = DefaultRiveAdapter()
        val controller2 = RiveDialogueController(world12Tree, adapter2)

        repeat(3) { controller2.advanceNode() }
        val skipped2 = controller2.skip(allowBypassHandshake = true)
        assertTrue("skip must return true when bypass is allowed", skipped2)
        assertTrue("Dialogue must be completed when bypassed", controller2.isCompleted.value)
    }

    @Test
    fun testDialogueControllerSkipAfterHandshakeCompletion() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(world12Tree, adapter)

        repeat(3) { controller.advanceNode() }
        val solved = controller.submitHandshake("6")
        assertTrue(solved)

        val skipped = controller.skip(allowBypassHandshake = false)
        assertTrue("skip must succeed once handshake is completed", skipped)
        assertTrue(controller.isCompleted.value)
    }

    // ── 6. Non-regression of Worlds 1–11 ───────────────────────────────────────

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
    fun testWorld11TutorialStillPassesValidation() {
        val tree = repository.getDialogueForWorld(11)
        assertNotNull(tree)
        assertTrue(RiveDialogueValidator.validate(tree!!).isValid)
    }

    @Test
    fun testAllWorldsOneThroughTwelveHaveDistinctWorldNumbers() {
        for (i in 1..12) {
            val tree = repository.getDialogueForWorld(i)
            assertNotNull("World $i must exist", tree)
            assertEquals("World $i number mismatch", i, tree!!.worldNumber)
        }
    }
}
