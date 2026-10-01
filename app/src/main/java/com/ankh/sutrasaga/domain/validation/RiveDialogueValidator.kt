package com.ankh.sutrasaga.domain.validation

import com.ankh.sutrasaga.domain.models.DialogueValidationResult
import com.ankh.sutrasaga.domain.models.RiveAnimationConfig
import com.ankh.sutrasaga.domain.models.RiveCharacterState
import com.ankh.sutrasaga.domain.models.RiveDialogueNode
import com.ankh.sutrasaga.domain.models.RiveSutraDialogueTree

/**
 * RiveDialogueValidator — Application-side validator for Vedic Math Rive dialogue trees.
 *
 * Enforces:
 * 1. Structural constraints (node count 2..4, unique sequential IDs).
 * 2. Speaker and Rive character synchronization.
 * 3. Rive animation whitelist checking and fallback sanitization.
 * 4. Interactive handshake consistency.
 * 5. Math overlay expression and token containment.
 */
object RiveDialogueValidator {

    const val MIN_NODES = 2
    const val MAX_NODES = 4

    fun validate(tree: RiveSutraDialogueTree): DialogueValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        if (tree.sutraId.isBlank()) errors.add("Tree sutra_id must not be blank.")
        if (tree.sutraName.isBlank()) errors.add("Tree sutra_name must not be blank.")
        if (tree.englishMeaning.isBlank()) errors.add("Tree english_meaning must not be blank.")
        if (tree.worldNumber < 1) errors.add("Tree world_number must be >= 1.")

        val nodes = tree.dialogueNodes
        if (nodes.size < MIN_NODES || nodes.size > MAX_NODES) {
            errors.add("Dialogue node count must be between $MIN_NODES and $MAX_NODES (found ${nodes.size}).")
        }

        val seenNodeIds = mutableSetOf<Int>()
        for ((index, node) in nodes.withIndex()) {
            val expectedId = index + 1
            if (!seenNodeIds.add(node.nodeId)) {
                errors.add("Duplicate node_id ${node.nodeId} found.")
            }
            if (node.nodeId != expectedId) {
                warnings.add("Node at index $index has node_id ${node.nodeId}, expected $expectedId.")
            }

            if (node.text.isBlank()) {
                errors.add("Node ${node.nodeId}: text must not be blank.")
            }

            // Cross-field validation: speaker == rive_state.character
            if (node.speaker != node.riveState.character) {
                errors.add("Node ${node.nodeId}: speaker '${node.speaker}' does not match rive_state.character '${node.riveState.character}'.")
            }

            // Animation whitelist validation
            if (!RiveAnimationConfig.isValidAnimation(node.riveState.character, node.riveState.animation)) {
                errors.add("Node ${node.nodeId}: animation '${node.riveState.animation}' is not permitted for character ${node.riveState.character}.")
            }

            // Math overlay validation
            if (node.mathOverlay.expression.isBlank()) {
                errors.add("Node ${node.nodeId}: math_overlay.expression must not be blank.")
            } else {
                for (token in node.mathOverlay.highlightTokens) {
                    if (token.isNotBlank() && !node.mathOverlay.expression.contains(token)) {
                        warnings.add("Node ${node.nodeId}: highlight token '$token' does not appear directly in expression '${node.mathOverlay.expression}'.")
                    }
                }
            }

            // Handshake consistency validation
            val handshake = node.interactiveHandshake
            if (handshake.requiresUserTap) {
                if (handshake.targetElementId.isNullOrBlank()) {
                    errors.add("Node ${node.nodeId}: requires_user_tap is true but target_element_id is null/blank.")
                }
                if (handshake.promptText.isNullOrBlank()) {
                    errors.add("Node ${node.nodeId}: requires_user_tap is true but prompt_text is null/blank.")
                }
                if (handshake.expectedAnswer.isNullOrBlank()) {
                    errors.add("Node ${node.nodeId}: requires_user_tap is true but expected_answer is null/blank.")
                }
            } else {
                if (handshake.targetElementId != null) {
                    warnings.add("Node ${node.nodeId}: requires_user_tap is false but target_element_id is set to '${handshake.targetElementId}'.")
                }
                if (handshake.promptText != null) {
                    warnings.add("Node ${node.nodeId}: requires_user_tap is false but prompt_text is set to '${handshake.promptText}'.")
                }
                if (handshake.expectedAnswer != null) {
                    warnings.add("Node ${node.nodeId}: requires_user_tap is false but expected_answer is set to '${handshake.expectedAnswer}'.")
                }
            }
        }

        return DialogueValidationResult(
            isValid = errors.isEmpty(),
            errors = errors,
            warnings = warnings
        )
    }

    /**
     * Sanitizes and normalizes a dialogue tree, recovering safely from invalid animations
     * or structural flaws so the UI never crashes.
     */
    fun normalize(tree: RiveSutraDialogueTree): RiveSutraDialogueTree {
        val sanitizedNodes = tree.dialogueNodes.take(MAX_NODES).mapIndexed { index, node ->
            val safeAnimation = RiveAnimationConfig.sanitizeAnimation(node.riveState.character, node.riveState.animation)
            val safeRiveState = node.riveState.copy(
                character = node.speaker,
                animation = safeAnimation
            )
            node.copy(
                nodeId = index + 1,
                riveState = safeRiveState
            )
        }

        return tree.copy(dialogueNodes = sanitizedNodes)
    }
}
