package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.domain.models.YavadunamRemixSolution
import com.ankh.sutrasaga.domain.models.YavadunamRemixStep
import com.ankh.sutrasaga.engine.UpaSutraGenerator
import java.util.UUID
import kotlin.math.abs
import kotlin.math.pow
import kotlin.random.Random

/**
 * Problem generator and solver for Upa-Sutra 7: Yavadunam Tavadunikrtya Varganca Yojayet
 * (Mastery Remix with manual padding and carry overflow correction).
 */
class YavadunamRemixGenerator(
    private val random: Random = Random.Default
) : UpaSutraGenerator {

    override val upaSutraId: UpaSutraId = UpaSutraId.YAVADUNAM_TAVADUNIKRTYA_VARGANCHA_YOJAYET
    override val sutraName: String = "Yavadunam Tavadunikrtya Varganca Yojayet"

    fun solve(operand: Long, base: Long): YavadunamRemixSolution {
        val blockWidth = getBlockWidth(base)
        val deficiency = base - operand
        val rawLhs = operand - deficiency
        val rawRhs = deficiency * deficiency

        val modulus = 10.0.pow(blockWidth).toLong()
        val carry = rawRhs / modulus
        val remainder = rawRhs % modulus
        val finalLhs = rawLhs + carry
        val finalRhsString = remainder.toString().padStart(blockWidth, '0')
        val product = operand * operand

        val steps = mutableListOf<YavadunamRemixStep>()
        steps.add(
            YavadunamRemixStep(
                stepNumber = 1,
                label = "Identify Base & Deficiency",
                formulaDisplay = "Base = $base ($blockWidth digits), Deficiency d = $base - $operand = $deficiency",
                stepResult = "d = $deficiency",
                explanation = "Identify primary base $base with block width $blockWidth digits. Deficiency d = $deficiency."
            )
        )
        steps.add(
            YavadunamRemixStep(
                stepNumber = 2,
                label = "Lessen Operand by Deficiency",
                formulaDisplay = "$operand - $deficiency = $rawLhs",
                stepResult = "$rawLhs",
                explanation = "Reduce operand by its deficiency: $operand - $deficiency = $rawLhs."
            )
        )
        steps.add(
            YavadunamRemixStep(
                stepNumber = 3,
                label = "Square Deficiency & Check Overflow",
                formulaDisplay = "$deficiency² = $rawRhs",
                stepResult = if (carry > 0) "$rawRhs (Carry +$carry)" else finalRhsString,
                explanation = if (carry > 0) {
                    "Square deficiency: $deficiency² = $rawRhs. This exceeds the $blockWidth-digit block width! Keep \"$finalRhsString\" and carry +$carry to LHS."
                } else {
                    "Square deficiency: $deficiency² = $rawRhs. Pad to $blockWidth digits: \"$finalRhsString\"."
                }
            )
        )
        steps.add(
            YavadunamRemixStep(
                stepNumber = 4,
                label = "Combine / Concatenate",
                formulaDisplay = if (carry > 0) "($rawLhs + $carry) || $finalRhsString" else "$rawLhs || $finalRhsString",
                stepResult = "$product",
                explanation = if (carry > 0) {
                    "Add carry to LHS: $rawLhs + $carry = $finalLhs. Combine: $finalLhs || $finalRhsString = $product."
                } else {
                    "Combine LHS and padded RHS: $finalLhs || $finalRhsString = $product."
                }
            )
        )

        return YavadunamRemixSolution(
            operand = operand,
            base = base,
            blockWidth = blockWidth,
            deficiency = deficiency,
            rawLhs = rawLhs,
            rawRhs = rawRhs,
            carry = carry,
            finalLhs = finalLhs,
            finalRhsString = finalRhsString,
            product = product,
            steps = steps
        )
    }

    private fun getBlockWidth(base: Long): Int {
        var count = 0
        var b = base
        while (b > 1 && b % 10L == 0L) {
            count++
            b /= 10L
        }
        return if (count > 0) count else 2
    }

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val (operand, base) = when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> {
                // Base 100, no-carry (deficiency 1..9)
                val def = random.nextLong(1, 10)
                Pair(100L - def, 100L)
            }
            DifficultyTier.TIER_2_HARD -> {
                // Alternate between Base 1000 no-carry and Base 100 carry-overflow
                if (random.nextBoolean()) {
                    // Base 1000, 3-digit block width, deficiency 1..31
                    val def = random.nextLong(1, 32)
                    Pair(1000L - def, 1000L)
                } else {
                    // Base 100 carry overflow, deficiency 10..25
                    val def = random.nextLong(10, 26)
                    Pair(100L - def, 100L)
                }
            }
        }

        return buildProblemForOperand(operand, base, difficultyTier)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val base = if (operand > 500L) 1000L else 100L
        val tier = if (abs(base - operand) < 10) DifficultyTier.TIER_1_EASY else DifficultyTier.TIER_2_HARD
        return buildProblemForOperand(operand, base, tier)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val set = mutableListOf<SutraProblem>()
        val seen = mutableSetOf<Long>()
        var attempts = 0

        while (set.size < count && attempts < count * 20) {
            attempts++
            val prob = generateProblem(difficultyTier)
            if (!seen.contains(prob.operand)) {
                seen.add(prob.operand)
                set.add(prob)
            }
        }

        while (set.size < count) {
            set.add(generateProblem(difficultyTier))
        }

        return set
    }

    private fun buildProblemForOperand(
        operand: Long,
        base: Long,
        tier: DifficultyTier
    ): SutraProblem {
        val solution = solve(operand, base)
        val answer = solution.product

        val decompositionSteps = solution.steps.map { step ->
            DecompositionStep(
                stepNumber = step.stepNumber,
                label = step.label,
                formulaDisplay = step.formulaDisplay,
                stepResult = step.stepResult,
                explanation = step.explanation
            )
        }

        val distractors = listOf(
            solution.rawLhs * 100 + solution.rawRhs, // Forgot carry
            answer + 10,
            answer - 10,
            (operand * operand) + 100
        ).filter { it != answer && it > 0 }.distinct()

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = "Calculate $operand² (Base $base)",
            operand = operand,
            correctAnswer = answer,
            prefixPart = solution.finalLhs,
            incrementedPrefix = solution.finalLhs + 1,
            prefixProduct = solution.finalLhs,
            appendedSuffix = solution.finalRhsString,
            decompositionSteps = decompositionSteps,
            distractors = distractors,
            difficultyTier = tier,
            answerFormat = AnswerFormat.INTEGER,
            expectedRemainder = 0,
            expectedSecondaryAnswer = 0
        )
    }
}
