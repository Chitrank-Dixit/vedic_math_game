package com.ankh.sutrasaga.domain.models

/**
 * Step in Yavadunam Remix (Upa-Sutra 7) calculations.
 */
data class YavadunamRemixStep(
    val stepNumber: Int,
    val label: String,
    val formulaDisplay: String,
    val stepResult: String,
    val explanation: String
)

/**
 * Complete mathematical solution for Yavadunam Remix.
 */
data class YavadunamRemixSolution(
    val operand: Long,
    val base: Long,
    val blockWidth: Int,
    val deficiency: Long,
    val rawLhs: Long,
    val rawRhs: Long,
    val carry: Long,
    val finalLhs: Long,
    val finalRhsString: String,
    val product: Long,
    val steps: List<YavadunamRemixStep>
)
