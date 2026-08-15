package com.ankh.sutrasaga.domain.models

data class ParavartyaStep(
    val columnIndex: Int,
    val incomingValue: Long,
    val transposedDeviationProduct: Long,
    val resultingColumnValue: Long,
    val carryBorrowAdjustment: String = "",
    val explanationText: String
)

data class DivisionResult(
    val quotient: Long,
    val normalizedRemainder: Long,
    val verificationIdentity: String,
    val steps: List<ParavartyaStep>
)

data class ParavartyaProblem(
    val id: String,
    val dividend: Long,
    val divisor: Long,
    val selectedBase: Long,
    val transposedDeviation: Long,
    val quotient: Long,
    val normalizedRemainder: Long,
    val verificationIdentity: String,
    val difficultyTier: DifficultyTier,
    val steps: List<ParavartyaStep>,
    val distractors: List<Long>
)
