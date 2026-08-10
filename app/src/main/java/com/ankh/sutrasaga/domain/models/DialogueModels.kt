package com.ankh.sutrasaga.domain.models

enum class Speaker {
    SUTRA_MASTER,
    SIDEKICK,
    NARRATOR
}

enum class Expression(val emoji: String) {
    NEUTRAL("🙂"),
    EXCITED("🤩"),
    CONFUSED("🤨"),
    PROUD("😎")
}

data class DialoguePanel(
    val speaker: Speaker,
    val expression: Expression,
    val text: String,
    val highlightMathToken: String? = null,
    val speakerNameOverride: String? = null
)

data class DialogueScript(
    val worldId: Int,
    val title: String,
    val subtitle: String? = null,
    val panels: List<DialoguePanel>
)
