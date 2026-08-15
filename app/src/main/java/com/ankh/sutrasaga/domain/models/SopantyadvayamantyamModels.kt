package com.ankh.sutrasaga.domain.models

data class SandwichedDigits(
    val original: List<Int>,
    val sandwiched: List<Int>
)

data class RawPositionValue(
    val index: Int,
    val digit: Int,
    val previousDigit: Int,
    val n: Int,
    val rawValue: Int
)

data class CarryStep(
    val index: Int,
    val incomingRaw: Int,
    val incomingCarry: Int,
    val outputDigit: Int,
    val outgoingCarry: Int
)

data class SopantyadvayamantyamSolution(
    val multiplicand: Long,
    val multiplier: Int,
    val n: Int,
    val sandwichedDigits: SandwichedDigits,
    val rawValues: List<RawPositionValue>,
    val carrySteps: List<CarryStep>,
    val finalDigits: List<Int>,
    val product: Long
)

data class SopantyadvayamantyamProblem(
    val id: String,
    val multiplicand: Long,
    val multiplier: Int,
    val solution: SopantyadvayamantyamSolution,
    val difficultyTier: DifficultyTier,
    val distractors: List<Long>
)
