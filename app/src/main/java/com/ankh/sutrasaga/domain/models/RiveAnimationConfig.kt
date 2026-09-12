package com.ankh.sutrasaga.domain.models

/**
 * Centralized configuration for Rive character animations, state machine inputs, and fallbacks.
 */
object RiveAnimationConfig {

    val GURU_ANIMATIONS: Set<String> = setOf(
        "guru_idle",
        "guru_explain",
        "guru_bless",
        "guru_magic_fx",
        "guru_nod"
    )

    val SHISHYA_ANIMATIONS: Set<String> = setOf(
        "shishya_idle",
        "shishya_curious",
        "shishya_puzzled",
        "shishya_aha",
        "shishya_celebrate"
    )

    const val DEFAULT_GURU_ANIMATION = "guru_idle"
    const val DEFAULT_SHISHYA_ANIMATION = "shishya_idle"

    // State machine input names
    const val INPUT_IS_SPEAKING = "isSpeaking"
    const val INPUT_TRIGGER_PARTICLE = "triggerParticle"
    const val INPUT_HIGHLIGHT_NUMBER = "highlightNumber"

    /**
     * Checks if the given animation is valid for the specified character.
     */
    fun isValidAnimation(character: RiveSpeaker, animation: String): Boolean {
        return when (character) {
            RiveSpeaker.GURU -> GURU_ANIMATIONS.contains(animation)
            RiveSpeaker.SHISHYA -> SHISHYA_ANIMATIONS.contains(animation)
        }
    }

    /**
     * Returns the safe fallback animation for the given character.
     */
    fun getFallbackAnimation(character: RiveSpeaker): String {
        return when (character) {
            RiveSpeaker.GURU -> DEFAULT_GURU_ANIMATION
            RiveSpeaker.SHISHYA -> DEFAULT_SHISHYA_ANIMATION
        }
    }

    /**
     * Validates and returns the animation if allowed, or its safe fallback if invalid.
     */
    fun sanitizeAnimation(character: RiveSpeaker, animation: String): String {
        return if (isValidAnimation(character, animation)) {
            animation
        } else {
            getFallbackAnimation(character)
        }
    }
}
