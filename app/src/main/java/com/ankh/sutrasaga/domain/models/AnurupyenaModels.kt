package com.ankh.sutrasaga.domain.models

/**
 * Working base classification for Anurupyena ("Proportionately").
 */
enum class AnurupyenaBaseType(val workingBase: Long, val scaleRatio: Long, val isDivision: Boolean) {
    BASE_50_HALF(workingBase = 50L, scaleRatio = 2L, isDivision = true),
    BASE_200_DOUBLE(workingBase = 200L, scaleRatio = 2L, isDivision = false)
}

/**
 * Classification states for input validation.
 */
enum class AnurupyenaClassification {
    VALID_BASE_50,
    VALID_BASE_200,
    NON_INTEGER_SCALE_EXCLUDED,
    UNSUPPORTED_BASE,
    OVERFLOW_RISK
}

/**
 * Steps in the Anurupyena calculation breakdown.
 */
data class AnurupyenaStep(
    val stepNumber: Int,
    val label: String,
    val formulaDisplay: String,
    val stepResult: String,
    val explanation: String
)

/**
 * Complete mathematical solution object for Anurupyena.
 */
data class AnurupyenaSolution(
    val num1: Long,
    val num2: Long,
    val workingBase: Long,
    val baseType: AnurupyenaBaseType,
    val dev1: Long,
    val dev2: Long,
    val crossAddResult: Long,
    val scaledLeftPart: Long,
    val rawRightPart: Long,
    val finalLeftPart: Long,
    val finalRightPartString: String,
    val product: Long,
    val steps: List<AnurupyenaStep>,
    val classification: AnurupyenaClassification
)
