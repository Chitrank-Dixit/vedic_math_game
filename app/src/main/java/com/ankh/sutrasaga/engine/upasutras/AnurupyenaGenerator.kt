package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.AnurupyenaBaseType
import com.ankh.sutrasaga.domain.models.AnurupyenaClassification
import com.ankh.sutrasaga.domain.models.AnurupyenaSolution
import com.ankh.sutrasaga.domain.models.AnurupyenaStep
import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.engine.UpaSutraGenerator
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

/**
 * Problem generator and solver for Upa-Sutra 1: Anurupyena ("Proportionately").
 */
class AnurupyenaGenerator(
    private val random: Random = Random.Default
) : UpaSutraGenerator {

    override val upaSutraId: UpaSutraId = UpaSutraId.ANURUPYENA
    override val sutraName: String = "Anurupyena"

    fun solve(num1: Long, num2: Long, workingBase: Long): AnurupyenaSolution {
        val classification = classify(num1, num2, workingBase)
        if (classification != AnurupyenaClassification.VALID_BASE_50 &&
            classification != AnurupyenaClassification.VALID_BASE_200
        ) {
            return AnurupyenaSolution(
                num1 = num1,
                num2 = num2,
                workingBase = workingBase,
                baseType = if (workingBase == 200L) AnurupyenaBaseType.BASE_200_DOUBLE else AnurupyenaBaseType.BASE_50_HALF,
                dev1 = num1 - workingBase,
                dev2 = num2 - workingBase,
                crossAddResult = num1 + (num2 - workingBase),
                scaledLeftPart = 0L,
                rawRightPart = 0L,
                finalLeftPart = 0L,
                finalRightPartString = "00",
                product = num1 * num2,
                steps = emptyList(),
                classification = classification
            )
        }

        val baseType = if (workingBase == 200L) AnurupyenaBaseType.BASE_200_DOUBLE else AnurupyenaBaseType.BASE_50_HALF
        val dev1 = num1 - workingBase
        val dev2 = num2 - workingBase
        val crossAdd = num1 + dev2 // == num2 + dev1

        val scaledLeft = if (baseType.isDivision) {
            crossAdd / baseType.scaleRatio
        } else {
            crossAdd * baseType.scaleRatio
        }

        val rawRight = dev1 * dev2
        val (finalLeft, finalRightString) = if (rawRight >= 0) {
            val carry = rawRight / 100
            val rem = rawRight % 100
            val padRem = if (rem < 10) "0$rem" else "$rem"
            Pair(scaledLeft + carry, padRem)
        } else {
            // Negative right part: Borrow from left part
            val borrow = (abs(rawRight) + 99) / 100
            val adjustedRight = borrow * 100 + rawRight
            val padRem = if (adjustedRight < 10) "0$adjustedRight" else "$adjustedRight"
            Pair(scaledLeft - borrow, padRem)
        }

        val product = num1 * num2

        val steps = mutableListOf<AnurupyenaStep>()
        steps.add(
            AnurupyenaStep(
                stepNumber = 1,
                label = "Identify Working Base & Deviations",
                formulaDisplay = "W = $workingBase, d₁ = $num1 - $workingBase = $dev1, d₂ = $num2 - $workingBase = $dev2",
                stepResult = "d₁ = $dev1, d₂ = $dev2",
                explanation = "Numbers are close to working base $workingBase. Deviations are $dev1 and $dev2."
            )
        )
        steps.add(
            AnurupyenaStep(
                stepNumber = 2,
                label = "Cross-Add",
                formulaDisplay = "$num1 + ($dev2)",
                stepResult = "$crossAdd",
                explanation = "Cross-addition gives $num1 + ($dev2) = $crossAdd."
            )
        )
        steps.add(
            AnurupyenaStep(
                stepNumber = 3,
                label = if (baseType.isDivision) "Proportional Scaling (Halving)" else "Proportional Scaling (Doubling)",
                formulaDisplay = if (baseType.isDivision) "$crossAdd ÷ 2" else "$crossAdd × 2",
                stepResult = "$scaledLeft",
                explanation = if (baseType.isDivision) "Working base 50 is 100/2, so scale by dividing by 2: $crossAdd ÷ 2 = $scaledLeft."
                else "Working base 200 is 100×2, so scale by multiplying by 2: $crossAdd × 2 = $scaledLeft."
            )
        )
        steps.add(
            AnurupyenaStep(
                stepNumber = 4,
                label = "Compute Right Part & Combine",
                formulaDisplay = "($dev1) × ($dev2)",
                stepResult = "$product",
                explanation = if (rawRight >= 0) "Multiply deviations ($dev1) × ($dev2) = $rawRight. Combine: $scaledLeft || $finalRightString = $product."
                else "Multiply deviations ($dev1) × ($dev2) = $rawRight. Borrow 100: ($scaledLeft - 1) || (100 - ${abs(rawRight)}) = $product."
            )
        )

        return AnurupyenaSolution(
            num1 = num1,
            num2 = num2,
            workingBase = workingBase,
            baseType = baseType,
            dev1 = dev1,
            dev2 = dev2,
            crossAddResult = crossAdd,
            scaledLeftPart = scaledLeft,
            rawRightPart = rawRight,
            finalLeftPart = finalLeft,
            finalRightPartString = finalRightString,
            product = product,
            steps = steps,
            classification = classification
        )
    }

    fun classify(num1: Long, num2: Long, workingBase: Long): AnurupyenaClassification {
        if (abs(num1) > 1000000L || abs(num2) > 1000000L) return AnurupyenaClassification.OVERFLOW_RISK
        if (workingBase != 50L && workingBase != 200L) return AnurupyenaClassification.UNSUPPORTED_BASE

        val dev1 = num1 - workingBase
        val dev2 = num2 - workingBase
        val crossAdd = num1 + dev2

        if (workingBase == 50L) {
            if (crossAdd % 2L != 0L) return AnurupyenaClassification.NON_INTEGER_SCALE_EXCLUDED
            return AnurupyenaClassification.VALID_BASE_50
        }

        return AnurupyenaClassification.VALID_BASE_200
    }

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val (num1, num2, base) = when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> {
                // Base 50, both positive deviations, same parity
                val parity = random.nextInt(2) // 0 for even, 1 for odd
                val d1 = if (parity == 0) random.nextLong(1, 6) * 2 else random.nextLong(0, 5) * 2 + 1
                val d2 = if (parity == 0) random.nextLong(1, 6) * 2 else random.nextLong(0, 5) * 2 + 1
                Triple(50L + d1, 50L + d2, 50L)
            }
            DifficultyTier.TIER_2_HARD -> {
                // Alternate between Base 50 mixed-sign and Base 200
                if (random.nextBoolean()) {
                    // Base 50, mixed sign or negative, same parity
                    val parity = random.nextInt(2)
                    val d1 = if (parity == 0) -random.nextLong(1, 5) * 2 else -(random.nextLong(0, 4) * 2 + 1)
                    val d2 = if (parity == 0) -random.nextLong(1, 5) * 2 else -(random.nextLong(0, 4) * 2 + 1)
                    Triple(50L + d1, 50L + d2, 50L)
                } else {
                    // Base 200
                    val d1 = random.nextLong(-8, 9)
                    val d2 = random.nextLong(-8, 9)
                    Triple(200L + d1, 200L + d2, 200L)
                }
            }
        }

        return buildProblemForOperands(num1, num2, base, difficultyTier)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val base = if (operand > 100L) 200L else 50L
        val dev = operand - base
        val num2 = if (base == 50L) {
            // Pick a num2 with same parity deviation
            val targetDev = if (dev % 2L == 0L) 4L else 3L
            base + targetDev
        } else {
            base + 4L
        }
        val tier = if (base == 50L && operand >= 50L) DifficultyTier.TIER_1_EASY else DifficultyTier.TIER_2_HARD
        return buildProblemForOperands(operand, num2, base, tier)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val set = mutableListOf<SutraProblem>()
        val seen = mutableSetOf<Pair<Long, Long>>()
        var attempts = 0

        while (set.size < count && attempts < count * 20) {
            attempts++
            val prob = generateProblem(difficultyTier)
            val pair = Pair(prob.operand, prob.correctAnswer)
            if (!seen.contains(pair)) {
                seen.add(pair)
                set.add(prob)
            }
        }

        while (set.size < count) {
            set.add(generateProblem(difficultyTier))
        }

        return set
    }

    private fun buildProblemForOperands(
        num1: Long,
        num2: Long,
        base: Long,
        tier: DifficultyTier
    ): SutraProblem {
        val solution = solve(num1, num2, base)
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
            solution.crossAddResult * 100 + abs(solution.rawRightPart), // Forgot scaling
            answer + 10,
            answer - 10,
            (num1 * num2) + 20
        ).filter { it != answer && it > 0 }.distinct()

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = "Calculate $num1 × $num2 (Working Base $base)",
            operand = num1,
            correctAnswer = answer,
            prefixPart = solution.scaledLeftPart,
            incrementedPrefix = solution.scaledLeftPart + 1,
            prefixProduct = solution.scaledLeftPart,
            appendedSuffix = solution.finalRightPartString,
            decompositionSteps = decompositionSteps,
            distractors = distractors,
            difficultyTier = tier,
            answerFormat = AnswerFormat.INTEGER,
            expectedRemainder = 0,
            expectedSecondaryAnswer = 0
        )
    }
}
