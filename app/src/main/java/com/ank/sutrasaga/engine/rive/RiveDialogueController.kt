package com.ank.sutrasaga.engine.rive

import com.ank.sutrasaga.domain.models.RiveCharacterState
import com.ank.sutrasaga.domain.models.RiveDialogueNode
import com.ank.sutrasaga.domain.models.RiveEmotion
import com.ank.sutrasaga.domain.models.RiveSpeaker
import com.ank.sutrasaga.domain.models.RiveSutraDialogueTree
import com.ank.sutrasaga.domain.validation.RiveDialogueValidator
import com.ank.sutrasaga.domain.validation.VedicMathValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Observable state of an interactive student handshake within a dialogue tree.
 */
enum class HandshakeState {
    /** Initial state: handshake requires user interaction and has not been evaluated. */
    UNRESOLVED,
    /** Handshake answered correctly matching authoritative expectedAnswer. */
    CORRECT,
    /** Handshake submitted with an incorrect answer. */
    INCORRECT,
    /** Handshake cannot be evaluated because expectedAnswer is missing, blank, or invalid. */
    UNRESOLVABLE,
    /** Handshake was bypassed via explicit skip. */
    SKIPPED;

    val isSuccessful: Boolean get() = this == CORRECT
    val isResolved: Boolean get() = this == CORRECT || this == SKIPPED
}

/**
 * Observable progression state of the overall dialogue tree session.
 */
enum class TutorialProgressionState {
    /** Learner is actively progressing through dialogue nodes. */
    IN_PROGRESS,
    /** Dialogue completed via advancing through all nodes or successfully completing required handshake. */
    COMPLETED,
    /** Dialogue progression was explicitly bypassed via skip. */
    SKIPPED;

    val isFinished: Boolean get() = this == COMPLETED || this == SKIPPED
}

/**
 * RiveDialogueController — Coordinates dialogue playback, Rive character states,
 * audio triggers, and interactive student handshake evaluation with retry handling.
 */
class RiveDialogueController(
    dialogueTree: RiveSutraDialogueTree,
    val riveAdapter: RiveAdapter = DefaultRiveAdapter()
) {
    val tree: RiveSutraDialogueTree = RiveDialogueValidator.normalize(dialogueTree)
    val totalNodes: Int = tree.dialogueNodes.size

    private val _currentNodeIndex = MutableStateFlow(0)
    val currentNodeIndex: StateFlow<Int> = _currentNodeIndex.asStateFlow()

    private val _currentNode = MutableStateFlow(tree.dialogueNodes.first())
    val currentNode: StateFlow<RiveDialogueNode> = _currentNode.asStateFlow()

    private val _progressionState = MutableStateFlow(TutorialProgressionState.IN_PROGRESS)
    val progressionState: StateFlow<TutorialProgressionState> = _progressionState.asStateFlow()

    private val _handshakeState = MutableStateFlow(HandshakeState.UNRESOLVED)
    val handshakeState: StateFlow<HandshakeState> = _handshakeState.asStateFlow()

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()

    private val _handshakeError = MutableStateFlow<String?>(null)
    val handshakeError: StateFlow<String?> = _handshakeError.asStateFlow()

    val isHandshakeCompleted: Boolean get() = _handshakeState.value == HandshakeState.CORRECT
    val isSkipped: Boolean get() = _progressionState.value == TutorialProgressionState.SKIPPED

    init {
        syncNodeState(0)
    }

    private fun syncNodeState(index: Int) {
        val node = tree.dialogueNodes.getOrElse(index) { tree.dialogueNodes.last() }
        _currentNodeIndex.value = index
        _currentNode.value = node
        _handshakeError.value = null

        // Sync with Rive adapter
        riveAdapter.applyCharacterState(node.riveState)
        riveAdapter.setHighlightNumber(node.mathOverlay.highlightTokens.firstOrNull())

        if (index >= totalNodes - 2) {
            riveAdapter.triggerParticle()
        } else {
            riveAdapter.clearParticle()
        }
    }

    /**
     * Advances to the next dialogue node, or completes the dialogue if on the final non-handshake node.
     */
    fun advanceNode(): Boolean {
        val current = _currentNode.value
        if (current.interactiveHandshake.requiresUserTap && _handshakeState.value != HandshakeState.CORRECT) {
            // Must complete handshake explicitly
            return false
        }

        val nextIndex = _currentNodeIndex.value + 1
        return if (nextIndex < totalNodes) {
            syncNodeState(nextIndex)
            true
        } else {
            _progressionState.value = TutorialProgressionState.COMPLETED
            _isCompleted.value = true
            false
        }
    }

    /**
     * Evaluates a user's tap/input for the interactive handshake.
     * On success: triggers celebration, marks handshake as CORRECT, and completes dialogue progression.
     * On failure: sets corrective state (shishya_puzzled) and records INCORRECT state.
     * On invalid/missing expected answer: sets UNRESOLVABLE state without advancing or marking completion.
     * Guarded against repeated submissions after success or skip.
     */
    fun submitHandshake(userAnswer: String): Boolean {
        // Idempotent repeated submission guard: already correctly answered
        if (_handshakeState.value == HandshakeState.CORRECT) {
            return true
        }

        // Bypassed/skipped tutorial cannot accept subsequent handshake submission
        if (_progressionState.value == TutorialProgressionState.SKIPPED) {
            return false
        }

        val current = _currentNode.value
        val handshake = current.interactiveHandshake

        // Fail-closed guard: missing, blank, or unresolvable expected answer cannot be evaluated
        val expected = handshake.expectedAnswer
        if (expected == null || expected.isBlank()) {
            _handshakeState.value = HandshakeState.UNRESOLVABLE
            riveAdapter.applyCharacterState(
                RiveCharacterState(
                    character = RiveSpeaker.SHISHYA,
                    animation = "shishya_puzzled",
                    emotion = RiveEmotion.THINKING,
                    isSpeaking = false
                )
            )
            _handshakeError.value = "Handshake cannot be evaluated: expected answer is missing or invalid."
            return false
        }

        val isCorrect = VedicMathValidator.evaluateHandshake(handshake, userAnswer)
        if (isCorrect) {
            _handshakeState.value = HandshakeState.CORRECT
            _progressionState.value = TutorialProgressionState.COMPLETED
            _handshakeError.value = null
            riveAdapter.applyCharacterState(
                RiveCharacterState(
                    character = RiveSpeaker.SHISHYA,
                    animation = "shishya_celebrate",
                    emotion = RiveEmotion.HAPPY,
                    isSpeaking = false
                )
            )
            riveAdapter.triggerParticle()
            _isCompleted.value = true
            return true
        } else {
            // Corrective feedback state
            _handshakeState.value = HandshakeState.INCORRECT
            riveAdapter.applyCharacterState(
                RiveCharacterState(
                    character = RiveSpeaker.SHISHYA,
                    animation = "shishya_puzzled",
                    emotion = RiveEmotion.THINKING,
                    isSpeaking = false
                )
            )
            _handshakeError.value = "Almost! Recheck: ${handshake.promptText ?: "Try the missing step again!"}"
            return false
        }
    }

    /**
     * Moves back to the previous dialogue node.
     * Returns true if navigation succeeded, false if already on the first node.
     */
    fun previousNode(): Boolean {
        val prevIndex = _currentNodeIndex.value - 1
        return if (prevIndex >= 0) {
            _isCompleted.value = false
            _progressionState.value = TutorialProgressionState.IN_PROGRESS
            _handshakeState.value = HandshakeState.UNRESOLVED
            syncNodeState(prevIndex)
            true
        } else {
            false
        }
    }

    /**
     * Restarts dialogue from node 1.
     */
    fun restart() {
        _isCompleted.value = false
        _progressionState.value = TutorialProgressionState.IN_PROGRESS
        _handshakeState.value = HandshakeState.UNRESOLVED
        syncNodeState(0)
    }

    /**
     * Resets dialogue to initial state.
     */
    fun reset() {
        restart()
    }

    /**
     * Checks if there is a pending, unresolved interactive handshake in this dialogue tree.
     * Returns true if a required handshake exists and has neither been answered correctly nor skipped.
     */
    fun hasUnresolvedHandshake(): Boolean {
        val hasRequiredHandshake = tree.dialogueNodes.any { it.interactiveHandshake.requiresUserTap }
        if (!hasRequiredHandshake) return false
        return _handshakeState.value != HandshakeState.CORRECT && _progressionState.value != TutorialProgressionState.SKIPPED
    }

    /**
     * Skips dialogue progression.
     * By default, progression is blocked while a required interactive handshake is unresolved.
     * To explicitly bypass an unresolved handshake, [allowBypassHandshake] must be set to true.
     *
     * Successful skip transitions [progressionState] to [TutorialProgressionState.SKIPPED] and
     * [handshakeState] to [HandshakeState.SKIPPED] (never falsely marking handshake as CORRECT).
     *
     * @return true if dialogue progression succeeded, false if blocked by an unresolved handshake.
     */
    fun skip(allowBypassHandshake: Boolean = false): Boolean {
        // If already completed via correct handshake or final node, idempotent success
        if (_progressionState.value == TutorialProgressionState.COMPLETED) {
            return true
        }

        // If already skipped, idempotent success
        if (_progressionState.value == TutorialProgressionState.SKIPPED) {
            return true
        }

        if (hasUnresolvedHandshake() && !allowBypassHandshake) {
            return false
        }

        _progressionState.value = TutorialProgressionState.SKIPPED
        if (_handshakeState.value != HandshakeState.CORRECT) {
            _handshakeState.value = HandshakeState.SKIPPED
        }
        _isCompleted.value = true
        return true
    }
}
