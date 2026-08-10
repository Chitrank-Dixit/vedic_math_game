package com.ankh.sutrasaga.domain.models

import com.ankh.sutrasaga.R

enum class GuruPose(val drawableResId: Int) {
    // TODO: replace with final art — guru_base_pose.png
    BASE(R.drawable.guru_base_pose),
    // TODO: replace with final art — guru_explaining_pose.png
    EXPLAINING(R.drawable.guru_explaining_pose),
    // TODO: replace with final art — guru_talking_pose.png
    TALKING(R.drawable.guru_talking_pose)
}

enum class GuruMouth(val drawableResId: Int) {
    // TODO: replace with final art — guru_mouth_neutral.png
    NEUTRAL(R.drawable.guru_mouth_neutral),
    // TODO: replace with final art — guru_mouth_talking.png
    TALKING(R.drawable.guru_mouth_talking),
    // TODO: replace with final art — guru_mouth_smile.png
    SMILE(R.drawable.guru_mouth_smile)
}

enum class DiscipleState(val drawableResId: Int) {
    // TODO: replace with final art — disciple_neutral.png
    NEUTRAL(R.drawable.disciple_neutral),
    // TODO: replace with final art — disciple_confused.png
    CONFUSED(R.drawable.disciple_confused),
    // TODO: replace with final art — disciple_understanding.png
    UNDERSTANDING(R.drawable.disciple_understanding)
}

enum class DiscipleEyes(val drawableResId: Int) {
    // TODO: replace with final art — disciple_eyes_neutral.png
    NEUTRAL(R.drawable.disciple_eyes_neutral),
    // TODO: replace with final art — disciple_eyes_confused.png
    CONFUSED(R.drawable.disciple_eyes_confused),
    // TODO: replace with final art — disciple_eyes_happy.png
    HAPPY(R.drawable.disciple_eyes_happy)
}

data class GurukulDialogueBeat(
    val guruText: String,
    val discipleText: String? = null,
    val isTeachingStep: Boolean = false,
    val guruPose: GuruPose = GuruPose.TALKING,
    val guruMouth: GuruMouth = GuruMouth.TALKING,
    val discipleState: DiscipleState = DiscipleState.NEUTRAL,
    val discipleEyes: DiscipleEyes = DiscipleEyes.NEUTRAL,
    val slateStepIndex: Int = 0
)

data class GurukulScript(
    val worldId: Int,
    val title: String,
    val subtitle: String,
    val beats: List<GurukulDialogueBeat>
)
