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

data class InteractiveHandshake(
    val requiresUserTap: Boolean = false,
    val targetElementId: String? = null,
    val promptText: String? = null
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
