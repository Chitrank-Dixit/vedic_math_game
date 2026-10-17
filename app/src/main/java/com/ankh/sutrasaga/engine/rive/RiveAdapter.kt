package com.ankh.sutrasaga.engine.rive

import com.ankh.sutrasaga.domain.models.RiveAnimationConfig
import com.ankh.sutrasaga.domain.models.RiveCharacterState
import com.ankh.sutrasaga.domain.models.RiveEmotion
import com.ankh.sutrasaga.domain.models.RiveSpeaker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * RiveAdapter — Interface decoupling Rive runtime state machine operations
 * from UI composables and dialogue controllers.
 */
interface RiveAdapter {
    val guruAnimation: StateFlow<String>
    val shishyaAnimation: StateFlow<String>
    val isSpeaking: StateFlow<Boolean>
    val activeSpeaker: StateFlow<RiveSpeaker>
    val particleTriggered: StateFlow<Boolean>
    val highlightedToken: StateFlow<String?>

    fun applyCharacterState(state: RiveCharacterState)
    fun triggerParticle()
    fun clearParticle()
    fun setHighlightNumber(token: String?)
    fun resetState()
}

/**
 * Default implementation of RiveAdapter that manages reactive character states.
 */
class DefaultRiveAdapter : RiveAdapter {

    private val _guruAnimation = MutableStateFlow(RiveAnimationConfig.DEFAULT_GURU_ANIMATION)
    override val guruAnimation: StateFlow<String> = _guruAnimation.asStateFlow()

    private val _shishyaAnimation = MutableStateFlow(RiveAnimationConfig.DEFAULT_SHISHYA_ANIMATION)
    override val shishyaAnimation: StateFlow<String> = _shishyaAnimation.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    override val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _activeSpeaker = MutableStateFlow(RiveSpeaker.GURU)
    override val activeSpeaker: StateFlow<RiveSpeaker> = _activeSpeaker.asStateFlow()

    private val _particleTriggered = MutableStateFlow(false)
    override val particleTriggered: StateFlow<Boolean> = _particleTriggered.asStateFlow()

    private val _highlightedToken = MutableStateFlow<String?>(null)
    override val highlightedToken: StateFlow<String?> = _highlightedToken.asStateFlow()

    override fun applyCharacterState(state: RiveCharacterState) {
        val safeAnimation = RiveAnimationConfig.sanitizeAnimation(state.character, state.animation)
        _activeSpeaker.value = state.character
        _isSpeaking.value = state.isSpeaking

        when (state.character) {
            RiveSpeaker.GURU -> {
                _guruAnimation.value = safeAnimation
                _shishyaAnimation.value = RiveAnimationConfig.DEFAULT_SHISHYA_ANIMATION
            }
            RiveSpeaker.SHISHYA -> {
                _shishyaAnimation.value = safeAnimation
                _guruAnimation.value = RiveAnimationConfig.DEFAULT_GURU_ANIMATION
            }
        }
    }

    override fun triggerParticle() {
        _particleTriggered.value = true
    }

    override fun clearParticle() {
        _particleTriggered.value = false
    }

    override fun setHighlightNumber(token: String?) {
        _highlightedToken.value = token
    }

    override fun resetState() {
        _guruAnimation.value = RiveAnimationConfig.DEFAULT_GURU_ANIMATION
        _shishyaAnimation.value = RiveAnimationConfig.DEFAULT_SHISHYA_ANIMATION
        _isSpeaking.value = false
        _activeSpeaker.value = RiveSpeaker.GURU
        _particleTriggered.value = false
        _highlightedToken.value = null
    }
}
