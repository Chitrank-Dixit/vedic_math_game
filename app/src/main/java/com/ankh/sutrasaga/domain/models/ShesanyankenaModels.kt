package com.ankh.sutrasaga.domain.models

data class DecimalExpansion(
    val integerPart: Long,
    val nonRepeatingDigits: List<Int>,
    val repeatingDigits: List<Int>,
    val isTerminating: Boolean,
    val sourceFraction: Fraction
) {
    fun toFormattedString(): String {
        val intStr = integerPart.toString()
        val nonRepStr = nonRepeatingDigits.joinToString("")
        val repStr = repeatingDigits.joinToString("")

        return when {
            isTerminating -> {
                if (nonRepStr.isEmpty()) intStr else "$intStr.$nonRepStr"
            }
            repeatingDigits.isNotEmpty() -> {
                if (nonRepStr.isEmpty()) "$intStr.($repStr)" else "$intStr.$nonRepStr($repStr)"
            }
            else -> intStr
        }
    }
}

data class RemainderStep(
    val stepIndex: Int,
    val incomingRemainder: Long,
    val multipliedRemainder: Long,
    val emittedDigit: Int,
    val nextRemainder: Long,
    val isCycleStart: Boolean,
    val isCycleEnd: Boolean
)

data class ShesanyankenaStep(
    val remainder: Long,
    val divisorLastDigit: Int,
    val product: Long,
    val extractedLastDigit: Int,
    val matchesReference: Boolean
)

enum class DecimalClassification {
    TERMINATING,
    PURE_RECURRING,
    MIXED_RECURRING,
    ZERO,
    NEGATIVE,
    UNSUPPORTED,
    OUTPUT_LIMIT_REACHED
}

data class ShesanyankenaSolution(
    val classification: DecimalClassification,
    val expansion: DecimalExpansion,
    val remainderSteps: List<RemainderStep>,
    val vedicSteps: List<ShesanyankenaStep>
)

data class ShesanyankenaProblem(
    val id: String,
    val numerator: Long,
    val denominator: Long,
    val fraction: Fraction,
    val solution: ShesanyankenaSolution,
    val difficultyTier: DifficultyTier,
    val distractors: List<Long>
)
