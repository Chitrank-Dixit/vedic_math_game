package com.ankh.sutrasaga.domain.models

enum class SankalanaClassification {
    UNIQUE_SOLUTION,
    INCONSISTENT,
    INFINITE_SOLUTIONS,
    NOT_APPLICABLE,
    DEGENERATE
}

data class SankalanaStep(
    val stepIndex: Int,
    val operationName: String,
    val formulaDisplay: String,
    val stepResult: String,
    val explanationText: String
)

data class SankalanaSolution(
    val classification: SankalanaClassification,
    val xSolution: Fraction,
    val ySolution: Fraction,
    val xPlusY: Fraction,
    val xMinusY: Fraction,
    val determinant: Long,
    val steps: List<SankalanaStep>
)

data class SankalanaProblem(
    val id: String,
    val firstEquation: LinearEquation,
    val secondEquation: LinearEquation,
    val classification: SankalanaClassification,
    val xSolution: Fraction,
    val ySolution: Fraction,
    val xPlusY: Fraction,
    val xMinusY: Fraction,
    val difficultyTier: DifficultyTier,
    val steps: List<SankalanaStep>,
    val distractors: List<Long>
)
