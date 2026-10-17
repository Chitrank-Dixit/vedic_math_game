package com.ankh.sutrasaga.engine.rive

import com.ankh.sutrasaga.domain.models.RiveCharacterState
import com.ankh.sutrasaga.domain.models.RiveDialogueNode
import com.ankh.sutrasaga.domain.models.RiveEmotion
import com.ankh.sutrasaga.domain.models.RiveSpeaker
import com.ankh.sutrasaga.domain.models.RiveSutraDialogueTree
import com.ankh.sutrasaga.domain.validation.RiveDialogueValidator
import com.ankh.sutrasaga.domain.validation.VedicMathValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()

    private val _handshakeError = MutableStateFlow<String?>(null)
    val handshakeError: StateFlow<String?> = _handshakeError.asStateFlow()

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
        if (current.interactiveHandshake.requiresUserTap) {
            // Must complete handshake explicitly
            return false
        }

        val nextIndex = _currentNodeIndex.value + 1
        return if (nextIndex < totalNodes) {
            syncNodeState(nextIndex)
            true
        } else {
            _isCompleted.value = true
            false
        }
    }

    /**
     * Evaluates a user's tap/input for the interactive handshake.
     * On success: triggers celebration and marks completion.
     * On failure: sets corrective state (shishya_puzzled) and allows retry without reset.
     */
    fun submitHandshake(userAnswer: String): Boolean {
        val current = _currentNode.value
        val handshake = current.interactiveHandshake

        val isCorrect = VedicMathValidator.evaluateHandshake(handshake, userAnswer)
        if (isCorrect) {
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
     * Restarts dialogue from node 1.
     */
    fun restart() {
        _isCompleted.value = false
        syncNodeState(0)
    }

    /**
     * Skips directly to quiz completion.
     */
    fun skip() {
        _isCompleted.value = true
    }
}
