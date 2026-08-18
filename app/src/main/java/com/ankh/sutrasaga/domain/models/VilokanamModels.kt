package com.ankh.sutrasaga.domain.models

/**
 * Recognizable mathematical pattern types supported in the Vilokanam pattern sight pack.
 */
enum class ObservationPatternId {
    ENDS_IN_FIVE_SQUARE,
    NEAR_BASE_SQUARE,
    SAME_TENS_UNITS_SUM_TEN,
    SYMMETRIC_PRODUCT,
    NONE_OF_THE_ABOVE
}

/**
 * Classification states for Vilokanam pattern recognition.
 */
enum class VilokanamClassification {
    PATTERN_RECOGNIZED,
    NONE_APPLIES,
    AMBIGUOUS_PATTERN,
    UNSUPPORTED_SOURCE_PATTERN
}

/**
 * Step in the pattern observation, explanation, and verification process.
 */
data class VilokanamStep(
    val stepNumber: Int,
    val label: String,
    val formulaDisplay: String,
    val stepResult: String,
    val explanation: String
)

/**
 * Complete mathematical solution and explanation for a Vilokanam challenge.
 */
data class VilokanamSolution(
    val problemDisplay: String,
    val correctPattern: ObservationPatternId,
    val patternName: String,
    val numericalResult: Long,
    val explanation: String,
    val steps: List<VilokanamStep>,
    val classification: VilokanamClassification
)
