package com.ankh.sutrasaga.domain.models

/**
 * Classification states for Kevalaih Saptakam Gunyat 7-cyclic decimal generation.
 */
enum class KevalaihClassification {
    VALID_SEVENTH_RECIPROCAL,
    UNSUPPORTED_DENOMINATOR,
    UNSUPPORTED_NUMERATOR,
    REFERENCE_MISMATCH
}

/**
 * Single step in the 143-wheel cyclic repetend generation.
 */
data class KevalaihStep(
    val stepNumber: Int,
    val label: String,
    val formulaDisplay: String,
    val stepResult: String,
    val explanation: String
)

/**
 * Complete mathematical solution for Kevalaih Saptakam Gunyat.
 */
data class KevalaihSolution(
    val numerator: Long,
    val denominator: Long,
    val cyclicBlock: Long,
    val cyclicBlockString: String,
    val decimalString: String,
    val steps: List<KevalaihStep>,
    val classification: KevalaihClassification
)
