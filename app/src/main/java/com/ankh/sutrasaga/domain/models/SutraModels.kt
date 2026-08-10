package com.ankh.sutrasaga.domain.models

enum class DifficultyTier {
    TIER_1_EASY,
    TIER_2_HARD
}

data class DecompositionStep(
    val stepNumber: Int,
    val label: String,
    val formulaDisplay: String,
    val stepResult: String,
    val explanation: String
)

data class SutraProblem(
    val id: String,
    val sutraName: String,
    val questionText: String,
    val operand: Long,
    val correctAnswer: Long,
    val prefixPart: Long,
    val incrementedPrefix: Long,
    val prefixProduct: Long,
    val appendedSuffix: String = "25",
    val decompositionSteps: List<DecompositionStep>,
    val distractors: List<Long>,
    val difficultyTier: DifficultyTier
)
