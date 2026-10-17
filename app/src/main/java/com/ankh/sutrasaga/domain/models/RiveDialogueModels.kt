package com.ankh.sutrasaga.domain.models

/**
 * Domain models for the Rive Vedic Math Guru-Shishya Dialogue Engine.
 */

enum class RiveSpeaker {
    GURU,
    SHISHYA
}

enum class RiveEmotion {
    HAPPY,
    THINKING,
    SURPRISED,
    NEUTRAL
}

data class RiveCharacterState(
    val character: RiveSpeaker,
    val animation: String,
    val emotion: RiveEmotion,
    val isSpeaking: Boolean
)

data class MathOverlay(
    val expression: String,
    val highlightTokens: List<String> = emptyList()
)

data class AudioCues(
    val ssmlText: String,
    val sfx: String
)

/**
 * Represents the interactive student handshake node requirement.
 *
 * @property requiresUserTap Whether learner action is required to advance.
 * @property targetElementId Visual / UI targeting metadata (e.g. numpad key ID for focus/hints).
 *                           Must NEVER be used to establish mathematical or response correctness.
 * @property promptText Instructional prompt presented to the learner.
 *                      Must NEVER be parsed to infer expected correctness.
 * @property expectedAnswer Authoritative expected response for this specific handshake interaction.
 *                          Represents the correct student input required to satisfy the handshake step
 *                          (e.g., intermediate step, missing token, root magnitude, or specific digit).
 *                          It is the authoritative source of truth for handshake validation, and does
 *                          not necessarily equal the full final product/result of the overall displayed math problem.
 */
data class InteractiveHandshake(
    val requiresUserTap: Boolean = false,
    val targetElementId: String? = null,
    val promptText: String? = null,
    val expectedAnswer: String? = null
)

data class RiveDialogueNode(
    val nodeId: Int,
    val speaker: RiveSpeaker,
    val text: String,
    val riveState: RiveCharacterState,
    val mathOverlay: MathOverlay,
    val audioCues: AudioCues,
    val interactiveHandshake: InteractiveHandshake
)

data class RiveSutraDialogueTree(
    val sutraId: String,
    val sutraName: String,
    val englishMeaning: String,
    val worldNumber: Int,
    val dialogueNodes: List<RiveDialogueNode>
)

data class DialogueValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList(),
    val warnings: List<String> = emptyList()
)
