package com.ank.sutrasaga.engine.rive

import com.ank.sutrasaga.domain.models.AudioCues
import com.ank.sutrasaga.domain.models.InteractiveHandshake
import com.ank.sutrasaga.domain.models.MathOverlay
import com.ank.sutrasaga.domain.models.RiveAnimationConfig
import com.ank.sutrasaga.domain.models.RiveCharacterState
import com.ank.sutrasaga.domain.models.RiveDialogueNode
import com.ank.sutrasaga.domain.models.RiveEmotion
import com.ank.sutrasaga.domain.models.RiveSpeaker
import com.ank.sutrasaga.domain.models.RiveSutraDialogueTree
import com.ank.sutrasaga.ui.navigation.VedicAppRoute
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Controller-level tests for interactive handshake submission and skip completion semantics.
 *
 * Verifies that:
 * 1. Successful handshake completion, skipped progression, incorrect answers, and unresolvable handshakes
 *    are distinct and observable states.
 * 2. Missing, blank, or invalid expected answers never count as successful completion.
 * 3. Incorrect answers never mark the handshake as completed.
 * 4. Skipping explicitly records SKIPPED progression and does NOT falsely record handshake success.
 * 5. Repeated submissions are idempotent and cannot corrupt terminal states.
 * 6. State does not leak across worlds or restart lifecycles.
 * 7. Observable navigation outcomes reflect the exact product contract.
 */
class HandshakeAndSkipCompletionSemanticsTest {

    private fun createTree(
        worldNumber: Int = 1,
        requiresHandshake: Boolean = true,
        expectedAnswer: String? = "20",
        promptText: String? = "Tap 20 to complete 4 × 5 = 20!"
    ): RiveSutraDialogueTree {
        val handshake = if (requiresHandshake) {
            InteractiveHandshake(
                requiresUserTap = true,
                targetElementId = "tile_$expectedAnswer",
                promptText = promptText,
                expectedAnswer = expectedAnswer
            )
        } else {
            InteractiveHandshake(requiresUserTap = false)
        }

        return RiveSutraDialogueTree(
            sutraId = "world_$worldNumber",
            sutraName = "Sutra $worldNumber",
            englishMeaning = "Meaning $worldNumber",
            worldNumber = worldNumber,
            dialogueNodes = listOf(
                RiveDialogueNode(
                    nodeId = 1,
                    speaker = RiveSpeaker.GURU,
                    text = "Observe the method carefully.",
                    riveState = RiveCharacterState(RiveSpeaker.GURU, "guru_explain", RiveEmotion.HAPPY, true),
                    mathOverlay = MathOverlay("45² = ?", listOf("45")),
                    audioCues = AudioCues("<speak>step 1</speak>", "sfx1"),
                    interactiveHandshake = InteractiveHandshake(false)
                ),
                RiveDialogueNode(
                    nodeId = 2,
                    speaker = RiveSpeaker.SHISHYA,
                    text = "I see the pattern!",
                    riveState = RiveCharacterState(RiveSpeaker.SHISHYA, "shishya_aha", RiveEmotion.SURPRISED, true),
                    mathOverlay = MathOverlay("4 × 5 = 20", listOf("20")),
                    audioCues = AudioCues("<speak>step 2</speak>", "sfx2"),
                    interactiveHandshake = InteractiveHandshake(false)
                ),
                RiveDialogueNode(
                    nodeId = 3,
                    speaker = RiveSpeaker.GURU,
                    text = "Now try it yourself!",
                    riveState = RiveCharacterState(RiveSpeaker.GURU, "guru_magic_fx", RiveEmotion.HAPPY, true),
                    mathOverlay = MathOverlay("45² = 2025", listOf("2025")),
                    audioCues = AudioCues("<speak>step 3</speak>", "sfx3"),
                    interactiveHandshake = handshake
                )
            )
        )
    }

    /**
     * 1. Correct Answer:
     * Submitting the authoritative expected answer transitions HandshakeState to CORRECT,
     * TutorialProgressionState to COMPLETED, triggers celebration & particles, and marks isCompleted.
     */
    @Test
    fun testCorrectAnswer_MarksHandshakeCorrectAndProgressionCompleted() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(createTree(expectedAnswer = "20"), adapter)

        // Advance to handshake node
        controller.advanceNode() // node 1 -> 2
        controller.advanceNode() // node 2 -> 3 (handshake)
        assertEquals(2, controller.currentNodeIndex.value)
        assertEquals(HandshakeState.UNRESOLVED, controller.handshakeState.value)
        assertEquals(TutorialProgressionState.IN_PROGRESS, controller.progressionState.value)
        assertFalse(controller.isCompleted.value)
        assertFalse(controller.isHandshakeCompleted)
        assertTrue(controller.hasUnresolvedHandshake())

        // Clear any transition particles to isolate handshake evaluation effect
        adapter.clearParticle()

        val result = controller.submitHandshake("20")
        assertTrue("Submitting correct answer must return true", result)

        // Observable state verification
        assertEquals(HandshakeState.CORRECT, controller.handshakeState.value)
        assertTrue("isHandshakeCompleted must be true", controller.isHandshakeCompleted)
        assertEquals(TutorialProgressionState.COMPLETED, controller.progressionState.value)
        assertTrue("isCompleted must be true for navigation", controller.isCompleted.value)
        assertFalse("hasUnresolvedHandshake must be false after correct answer", controller.hasUnresolvedHandshake())
        assertNull("handshakeError must be null upon success", controller.handshakeError.value)

        // Character and FX state verification
        assertEquals("shishya_celebrate", adapter.shishyaAnimation.value)
        assertTrue("Particle victory FX must be triggered", adapter.particleTriggered.value)
    }

    /**
     * 2. Incorrect Answer:
     * Submitting a wrong answer transitions HandshakeState to INCORRECT, keeps progression IN_PROGRESS,
     * keeps isCompleted false, sets corrective feedback error, and triggers puzzled character state.
     */
    @Test
    fun testIncorrectAnswer_SetsIncorrectStateWithoutCompleting() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(createTree(expectedAnswer = "20"), adapter)
        controller.advanceNode()
        controller.advanceNode()

        // Clear any node-advance particles to verify handshake evaluation does NOT trigger particles
        adapter.clearParticle()

        val result = controller.submitHandshake("99")
        assertFalse("Submitting incorrect answer must return false", result)

        // Observable state verification
        assertEquals(HandshakeState.INCORRECT, controller.handshakeState.value)
        assertFalse("isHandshakeCompleted must be false for incorrect answer", controller.isHandshakeCompleted)
        assertEquals(TutorialProgressionState.IN_PROGRESS, controller.progressionState.value)
        assertFalse("isCompleted must remain false on incorrect answer", controller.isCompleted.value)
        assertTrue("hasUnresolvedHandshake must remain true", controller.hasUnresolvedHandshake())
        assertNotNull("handshakeError must contain corrective feedback", controller.handshakeError.value)
        assertTrue(controller.handshakeError.value!!.contains("Almost!"))

        // Corrective character state
        assertEquals("shishya_puzzled", adapter.shishyaAnimation.value)
        assertNotEquals("shishya_celebrate", adapter.shishyaAnimation.value)
        assertFalse("Particle FX must NOT be triggered on wrong answer", adapter.particleTriggered.value)
    }

    /**
     * 3. Missing Expected Answer (null):
     * A handshake configured with null expectedAnswer must fail closed: HandshakeState is UNRESOLVABLE,
     * returns false, isCompleted remains false, and celebration is NEVER triggered.
     */
    @Test
    fun testMissingExpectedAnswer_SetsUnresolvableStateAndNeverCompletes() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(createTree(expectedAnswer = null), adapter)
        controller.advanceNode()
        controller.advanceNode()

        // Clear particles before evaluating invalid handshake
        adapter.clearParticle()

        val result = controller.submitHandshake("20")
        assertFalse("Null expected answer must return false for any answer", result)

        assertEquals(HandshakeState.UNRESOLVABLE, controller.handshakeState.value)
        assertFalse(controller.isHandshakeCompleted)
        assertEquals(TutorialProgressionState.IN_PROGRESS, controller.progressionState.value)
        assertFalse(controller.isCompleted.value)
        assertTrue(controller.hasUnresolvedHandshake())
        assertNotNull(controller.handshakeError.value)
        assertTrue(controller.handshakeError.value!!.contains("missing or invalid"))

        assertFalse("Celebration FX must never trigger for unresolvable handshake", adapter.particleTriggered.value)
        assertNotEquals("shishya_celebrate", adapter.shishyaAnimation.value)
        assertEquals("shishya_puzzled", adapter.shishyaAnimation.value)
    }

    /**
     * 4. Blank Expected Answer ("" or "   "):
     * A handshake configured with an empty or whitespace expectedAnswer must fail closed.
     */
    @Test
    fun testBlankExpectedAnswer_SetsUnresolvableStateAndNeverCompletes() {
        val adapter = DefaultRiveAdapter()
        val emptyController = RiveDialogueController(createTree(expectedAnswer = ""), adapter)
        emptyController.advanceNode()
        emptyController.advanceNode()

        assertFalse("Empty string expected answer must return false", emptyController.submitHandshake(""))
        assertEquals(HandshakeState.UNRESOLVABLE, emptyController.handshakeState.value)
        assertFalse(emptyController.isHandshakeCompleted)
        assertFalse(emptyController.isCompleted.value)

        val whitespaceController = RiveDialogueController(createTree(expectedAnswer = "   "), adapter)
        whitespaceController.advanceNode()
        whitespaceController.advanceNode()

        assertFalse("Whitespace expected answer must return false", whitespaceController.submitHandshake("20"))
        assertEquals(HandshakeState.UNRESOLVABLE, whitespaceController.handshakeState.value)
        assertFalse(whitespaceController.isHandshakeCompleted)
        assertFalse(whitespaceController.isCompleted.value)
    }

    /**
     * 5. Skip with a Valid Unanswered Handshake:
     * - Default skip(allowBypassHandshake = false) fails closed when handshake is unresolved.
     * - Explicit skip(allowBypassHandshake = true) transitions progressionState to SKIPPED,
     *   sets HandshakeState to SKIPPED, and marks isCompleted = true for navigation,
     *   BUT leaves isHandshakeCompleted = false so skipped lessons are never falsely recorded as solved.
     */
    @Test
    fun testSkipWithValidUnansweredHandshake_DefaultBlocked_ExplicitSkipDistinctFromSolved() {
        val controller = RiveDialogueController(createTree(expectedAnswer = "20"))
        assertTrue("Tree has unresolved handshake", controller.hasUnresolvedHandshake())

        // Default skip blocked
        val blocked = controller.skip(allowBypassHandshake = false)
        assertFalse("Default skip must be blocked when handshake is unresolved", blocked)
        assertEquals(TutorialProgressionState.IN_PROGRESS, controller.progressionState.value)
        assertEquals(HandshakeState.UNRESOLVED, controller.handshakeState.value)
        assertFalse(controller.isCompleted.value)
        assertFalse(controller.isHandshakeCompleted)

        // Explicit skip bypass
        val skipped = controller.skip(allowBypassHandshake = true)
        assertTrue("Explicit skip must succeed", skipped)
        assertEquals(TutorialProgressionState.SKIPPED, controller.progressionState.value)
        assertEquals(HandshakeState.SKIPPED, controller.handshakeState.value)
        assertTrue("isSkipped must be true", controller.isSkipped)
        assertTrue("isCompleted must be true to allow screen navigation", controller.isCompleted.value)
        assertFalse("CRITICAL: isHandshakeCompleted must be FALSE on skip", controller.isHandshakeCompleted)
        assertFalse("hasUnresolvedHandshake must be false after skip bypass", controller.hasUnresolvedHandshake())
    }

    /**
     * 6. Skip with an Unresolved Handshake (Missing or Invalid Expected Answer):
     * Default skip is blocked; explicit skip bypasses without falsely marking success.
     */
    @Test
    fun testSkipWithUnresolvedHandshake_MissingOrInvalidExpectedAnswer() {
        val controller = RiveDialogueController(createTree(expectedAnswer = null))
        assertFalse("Default skip blocked on invalid handshake", controller.skip(allowBypassHandshake = false))
        assertFalse(controller.isCompleted.value)

        assertTrue("Explicit skip succeeds on invalid handshake", controller.skip(allowBypassHandshake = true))
        assertEquals(TutorialProgressionState.SKIPPED, controller.progressionState.value)
        assertEquals(HandshakeState.SKIPPED, controller.handshakeState.value)
        assertFalse(controller.isHandshakeCompleted)
        assertTrue(controller.isCompleted.value)
    }

    /**
     * 7. Repeated Submission Idempotency:
     * - Once a handshake is answered correctly, subsequent calls to submitHandshake (with correct or incorrect
     *   answers) are idempotent: they return true and do NOT revert to puzzled state or re-trigger particles.
     * - Once a lesson is skipped, subsequent submitHandshake calls are rejected and cannot overwrite the skip.
     * - Calling skip repeatedly is idempotent and does not corrupt state.
     */
    @Test
    fun testRepeatedSubmission_IsIdempotentAndCannotCorruptCompletedOrSkippedState() {
        val adapter = DefaultRiveAdapter()
        val controller = RiveDialogueController(createTree(expectedAnswer = "20"), adapter)
        controller.advanceNode()
        controller.advanceNode()

        // First submission: correct
        assertTrue(controller.submitHandshake("20"))
        assertEquals(HandshakeState.CORRECT, controller.handshakeState.value)
        assertEquals("shishya_celebrate", adapter.shishyaAnimation.value)

        // Repeated submission of the SAME correct answer
        assertTrue("Repeated submission of correct answer must return true", controller.submitHandshake("20"))
        assertEquals(HandshakeState.CORRECT, controller.handshakeState.value)

        // Repeated submission of an INCORRECT answer after completion must NOT revert state
        assertTrue("Submitting wrong answer after completion must not corrupt state", controller.submitHandshake("99"))
        assertEquals("HandshakeState must remain CORRECT", HandshakeState.CORRECT, controller.handshakeState.value)
        assertEquals("shishya_celebrate", adapter.shishyaAnimation.value)
        assertNull("handshakeError must remain null", controller.handshakeError.value)

        // Test idempotency of skip() after completion: HandshakeState remains CORRECT, does NOT change to SKIPPED
        assertTrue("Calling skip after completion succeeds idempotently", controller.skip())
        assertEquals("HandshakeState must remain CORRECT", HandshakeState.CORRECT, controller.handshakeState.value)
        assertEquals(TutorialProgressionState.COMPLETED, controller.progressionState.value)

        // Test submitHandshake after explicit skip
        val controllerSkipped = RiveDialogueController(createTree(expectedAnswer = "20"))
        assertTrue(controllerSkipped.skip(allowBypassHandshake = true))
        assertEquals(TutorialProgressionState.SKIPPED, controllerSkipped.progressionState.value)

        assertFalse("Submitting handshake after skip must be rejected", controllerSkipped.submitHandshake("20"))
        assertEquals("ProgressionState must remain SKIPPED", TutorialProgressionState.SKIPPED, controllerSkipped.progressionState.value)
        assertEquals("HandshakeState must remain SKIPPED", HandshakeState.SKIPPED, controllerSkipped.handshakeState.value)
    }

    /**
     * 8. State Reset When Entering Another World:
     * - Completed state in World 1 does NOT leak into World 2.
     * - Calling restart() or reset() cleans up all StateFlows and returns to initial state.
     */
    @Test
    fun testStateReset_WhenEnteringAnotherWorldOrRestarting() {
        val adapter1 = DefaultRiveAdapter()
        val controllerWorld1 = RiveDialogueController(createTree(worldNumber = 1, expectedAnswer = "20"), adapter1)
        controllerWorld1.advanceNode()
        controllerWorld1.advanceNode()
        assertTrue(controllerWorld1.submitHandshake("20"))
        assertTrue(controllerWorld1.isCompleted.value)
        assertTrue(controllerWorld1.isHandshakeCompleted)

        // Enter World 2 with a new controller instance
        val adapter2 = DefaultRiveAdapter()
        val controllerWorld2 = RiveDialogueController(createTree(worldNumber = 2, expectedAnswer = "3"), adapter2)

        assertEquals("World 2 starts on node 0", 0, controllerWorld2.currentNodeIndex.value)
        assertFalse("World 2 must not inherit World 1 completion", controllerWorld2.isCompleted.value)
        assertFalse("World 2 must not inherit World 1 handshake success", controllerWorld2.isHandshakeCompleted)
        assertEquals(HandshakeState.UNRESOLVED, controllerWorld2.handshakeState.value)
        assertEquals(TutorialProgressionState.IN_PROGRESS, controllerWorld2.progressionState.value)
        assertTrue("World 2 has unresolved handshake", controllerWorld2.hasUnresolvedHandshake())

        // Calling restart() on World 1 resets it cleanly
        controllerWorld1.restart()
        assertEquals(0, controllerWorld1.currentNodeIndex.value)
        assertFalse(controllerWorld1.isCompleted.value)
        assertFalse(controllerWorld1.isHandshakeCompleted)
        assertEquals(HandshakeState.UNRESOLVED, controllerWorld1.handshakeState.value)
        assertEquals(TutorialProgressionState.IN_PROGRESS, controllerWorld1.progressionState.value)
        assertTrue(controllerWorld1.hasUnresolvedHandshake())
        assertNull(controllerWorld1.handshakeError.value)
    }

    /**
     * 9. Correct Navigation Outcomes After Successful Completion or Explicit Skip:
     * Simulates the UI navigation callback contract:
     * - Incorrect answer -> stays on GURUKUL_TUTORIAL (does not navigate).
     * - Blocked skip -> stays on GURUKUL_TUTORIAL.
     * - Correct handshake -> onComplete() invoked -> navigates to SUTRA_SOLVER.
     * - Explicit skip -> onComplete() invoked -> navigates to SUTRA_SOLVER.
     */
    @Test
    fun testCorrectNavigationOutcomes_AfterSuccessfulCompletionOrExplicitSkip() {
        var currentRoute = VedicAppRoute.GURUKUL_TUTORIAL

        fun simulateOnComplete() {
            currentRoute = VedicAppRoute.SUTRA_SOLVER
        }

        // Scenario A: Incorrect answer
        val controller = RiveDialogueController(createTree(expectedAnswer = "20"))
        controller.advanceNode()
        controller.advanceNode()
        val wrongAnswerResult = controller.submitHandshake("99")
        if (wrongAnswerResult) {
            simulateOnComplete()
        }
        assertEquals("Wrong answer must not trigger navigation", VedicAppRoute.GURUKUL_TUTORIAL, currentRoute)

        // Scenario B: Blocked skip attempt without bypass
        val blockedSkip = controller.skip(allowBypassHandshake = false)
        if (blockedSkip) {
            simulateOnComplete()
        }
        assertEquals("Blocked skip must not trigger navigation", VedicAppRoute.GURUKUL_TUTORIAL, currentRoute)

        // Scenario C: Explicit skip with allowBypassHandshake = true
        val explicitSkip = controller.skip(allowBypassHandshake = true)
        if (explicitSkip) {
            simulateOnComplete()
        }
        assertEquals("Explicit skip must navigate to SUTRA_SOLVER", VedicAppRoute.SUTRA_SOLVER, currentRoute)

        // Scenario D: Correct answer navigation
        currentRoute = VedicAppRoute.GURUKUL_TUTORIAL
        val controllerD = RiveDialogueController(createTree(expectedAnswer = "20"))
        controllerD.advanceNode()
        controllerD.advanceNode()
        val correctAnswerResult = controllerD.submitHandshake("20")
        assertTrue(correctAnswerResult)
        // In the UI, correct answer enables "Continue →" which triggers onComplete()
        simulateOnComplete()
        assertEquals("Successful handshake must navigate to SUTRA_SOLVER", VedicAppRoute.SUTRA_SOLVER, currentRoute)
    }
}
