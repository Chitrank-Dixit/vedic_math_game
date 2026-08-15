package com.ankh.sutrasaga.domain.models

data class SymmetricParts(
    val wholeAverage: Fraction,
    val negativeDeviation: Fraction,
    val positiveDeviation: Fraction
)

enum class VyashtisamashtihOperationType {
    SPLIT,
    FIND_AVERAGE,
    FIND_DEVIATION,
    SQUARE_WHOLE,
    SQUARE_DEVIATION,
    SUBTRACT,
    VERIFY
}

data class VyashtisamashtihStep(
    val stepIndex: Int,
    val operationType: VyashtisamashtihOperationType,
    val formulaDisplay: String,
    val stepResult: String,
    val explanationText: String
)

enum class VyashtisamashtihClassification {
    SUPPORTED_INTEGER_PATH,
    SUPPORTED_FRACTION_PATH,
    UNSUPPORTED_SYMBOLIC_FORM,
    OVERFLOW_RISK
}

data class VyashtisamashtihSolution(
    val classification: VyashtisamashtihClassification,
    val left: Fraction,
    val right: Fraction,
    val parts: SymmetricParts,
    val wholeSquare: Fraction,
    val deviationSquare: Fraction,
    val product: Fraction,
    val steps: List<VyashtisamashtihStep>
)

data class VyashtisamashtihProblem(
    val id: String,
    val left: Fraction,
    val right: Fraction,
    val parts: SymmetricParts,
    val solution: VyashtisamashtihSolution,
    val difficultyTier: DifficultyTier,
    val distractors: List<Long>
)
