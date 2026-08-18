package com.ankh.sutrasaga.domain.models

/**
 * Pre-verified divisors with their unique osculator constants and operations.
 */
enum class SupportedDivisor(val divisor: Long, val osculator: Long, val isNegative: Boolean) {
    SEVEN(divisor = 7L, osculator = 2L, isNegative = true),
    THIRTEEN(divisor = 13L, osculator = 4L, isNegative = false),
    NINETEEN(divisor = 19L, osculator = 2L, isNegative = false)
}

/**
 * Classification states for Vestanam divisibility checking.
 */
enum class VestanamClassification {
    DIVISIBLE,
    NOT_DIVISIBLE,
    UNSUPPORTED_DIVISOR,
    OVERFLOW_RISK
}

/**
 * Single step in an osculation sequence.
 */
data class OsculationStep(
    val stepNumber: Int,
    val inputValue: Long,
    val restValue: Long,
    val lastDigit: Long,
    val osculatedProduct: Long,
    val resultValue: Long,
    val explanation: String
)

/**
 * Complete mathematical solution for Vestanam divisibility testing.
 */
data class OsculationSolution(
    val number: Long,
    val divisor: SupportedDivisor,
    val isDivisible: Boolean,
    val finalValue: Long,
    val steps: List<OsculationStep>,
    val classification: VestanamClassification
)
