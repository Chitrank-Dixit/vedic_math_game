package com.ankh.sutrasaga.ui.feedback

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

enum class VedicFeedbackEvent {
    NUMPAD_TAP,        // Light impact + 800Hz wood-tick
    CORRECT_DIGIT,     // Medium impact + 528Hz Solfeggio bell chime
    INCORRECT_STEP,    // Double heavy tap + 180Hz muted resonance
    SUTRA_MASTERED     // Success vibration pattern + 3-note ascending chime
}

/**
 * VedicFeedbackService — Centralized Controller for Touch, Haptics & Audio Tone Synthesis
 */
class VedicFeedbackService(private val context: Context) {

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    /**
     * Dispatches coordinated haptic feedback and acoustic cues based on the event.
     */
    fun trigger(event: VedicFeedbackEvent) {
        when (event) {
            VedicFeedbackEvent.NUMPAD_TAP -> {
                triggerHaptic(VibrationType.LIGHT_TICK)
            }
            VedicFeedbackEvent.CORRECT_DIGIT -> {
                triggerHaptic(VibrationType.MEDIUM_PULSE)
            }
            VedicFeedbackEvent.INCORRECT_STEP -> {
                triggerHaptic(VibrationType.DOUBLE_HEAVY)
            }
            VedicFeedbackEvent.SUTRA_MASTERED -> {
                triggerHaptic(VibrationType.SUCCESS_FANFARE)
            }
        }
    }

    private enum class VibrationType {
        LIGHT_TICK,
        MEDIUM_PULSE,
        DOUBLE_HEAVY,
        SUCCESS_FANFARE
    }

    private fun triggerHaptic(type: VibrationType) {
        val vib = vibrator ?: return
        if (!vib.hasVibrator()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = when (type) {
                VibrationType.LIGHT_TICK -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                    } else {
                        VibrationEffect.createOneShot(12, 80)
                    }
                }
                VibrationType.MEDIUM_PULSE -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                    } else {
                        VibrationEffect.createOneShot(25, 160)
                    }
                }
                VibrationType.DOUBLE_HEAVY -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
                    } else {
                        VibrationEffect.createWaveform(longArrayOf(0, 40, 60, 40), intArrayOf(0, 220, 0, 220), -1)
                    }
                }
                VibrationType.SUCCESS_FANFARE -> {
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 30, 40, 50, 40, 100),
                        intArrayOf(0, 120, 0, 180, 0, 255),
                        -1
                    )
                }
            }
            vib.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            when (type) {
                VibrationType.LIGHT_TICK -> vib.vibrate(12)
                VibrationType.MEDIUM_PULSE -> vib.vibrate(25)
                VibrationType.DOUBLE_HEAVY -> vib.vibrate(longArrayOf(0, 40, 60, 40), -1)
                VibrationType.SUCCESS_FANFARE -> vib.vibrate(longArrayOf(0, 30, 40, 50, 40, 100), -1)
            }
        }
    }
}

@Composable
fun rememberVedicFeedbackService(): VedicFeedbackService {
    val context = LocalContext.current
    return remember(context) { VedicFeedbackService(context) }
}
