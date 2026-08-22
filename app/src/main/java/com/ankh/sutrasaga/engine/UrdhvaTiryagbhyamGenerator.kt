package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.random.Random

/**
 * UrdhvaTiryagbhyamGenerator — "Vertically and Crosswise" (ऊर्ध्वतिर्यग्भ्याम्)
 *
 * Mathematical Shortcut Rule:
 * For 2-digit multiplication (ab × cd):
 * - Step 1 (Units × Units): b × d
 * - Step 2 (Crosswise sum + carry): (a × d) + (b × c) + carry
 * - Step 3 (Tens × Tens + carry): (a × c) + carry
 *
 * Example:
 * 23 × 14 → 3×4=12 (carry 1) → 2(4)+3(1)+1 = 12 (carry 1) → 2(1)+1 = 3 → 322
 */
class UrdhvaTiryagbhyamGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Urdhva-Tiryagbhyam"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val (multiplicand, multiplier) = when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> Pair(
                random.nextLong(11L, 100L),
                random.nextLong(11L, 100L)
            )
            DifficultyTier.TIER_2_HARD -> Pair(
                random.nextLong(101L, 1000L),
                random.nextLong(11L, 100L)
            )
        }
        return buildProblem(multiplicand, multiplier, difficultyTier)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val multiplier = 41L
        val tier = if (operand in 11L..99L) DifficultyTier.TIER_1_EASY else DifficultyTier.TIER_2_HARD
        return buildProblem(operand, multiplier, tier)
    }

    fun generateSpecificProblemPair(multiplicand: Long, multiplier: Long): SutraProblem {
        val tier = if (multiplicand <= 99L && multiplier <= 99L) DifficultyTier.TIER_1_EASY else DifficultyTier.TIER_2_HARD
        return buildProblem(multiplicand, multiplier, tier)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val pairs = mutableSetOf<Pair<Long, Long>>()
        val maxAttempts = count * 10
        var attempts = 0
        while (pairs.size < count && attempts < maxAttempts) {
            val candidate = when (difficultyTier) {
                DifficultyTier.TIER_1_EASY -> Pair(random.nextLong(11L, 100L), random.nextLong(11L, 100L))
                DifficultyTier.TIER_2_HARD -> Pair(random.nextLong(101L, 1000L), random.nextLong(11L, 100L))
            }
            pairs.add(candidate)
            attempts++
        }
        return pairs.map { (a, b) -> buildProblem(a, b, difficultyTier) }
    }

    private fun buildProblem(multiplicand: Long, multiplier: Long, tier: DifficultyTier): SutraProblem {
        val correctAnswer = multiplicand * multiplier

        val steps = if (multiplicand <= 99L && multiplier <= 99L) {
            build2x2Steps(multiplicand, multiplier, correctAnswer)
        } else {
            build3x2Steps(multiplicand, multiplier, correctAnswer)
        }

        val distractors = generateDistractors(correctAnswer, multiplicand, multiplier)

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = "Calculate $multiplicand × $multiplier using $sutraName",
            operand = multiplicand,
            correctAnswer = correctAnswer,
            prefixPart = multiplicand,
            incrementedPrefix = multiplier,
            prefixProduct = correctAnswer,
            appendedSuffix = "",
            decompositionSteps = steps,
            distractors = distractors,
            difficultyTier = tier
        )
    }

    private fun build2x2Steps(a: Long, b: Long, correctAnswer: Long): List<DecompositionStep> {
        val a1 = a / 10
        val a0 = a % 10
        val b1 = b / 10
        val b0 = b % 10

        val rawUnits = a0 * b0
        val d0 = rawUnits % 10
        val c1 = rawUnits / 10

        val rawCross = (a1 * b0) + (a0 * b1) + c1
        val d1 = rawCross % 10
        val c2 = rawCross / 10

        val rawTens = (a1 * b1) + c2

        return listOf(
            DecompositionStep(
                stepNumber = 1,
                label = "Vertical Right (Units)",
                formulaDisplay = "$a0 × $b0",
                stepResult = "$d0" + if (c1 > 0) " (carry $c1)" else "",
                explanation = "Multiply unit digits ($a0 × $b0 = $rawUnits). Write digit $d0${if (c1 > 0) " and carry $c1 to middle column." else "."}"
            ),
            DecompositionStep(
                stepNumber = 2,
                label = "Crosswise Middle (Tens)",
                formulaDisplay = "($a1 × $b0) + ($a0 × $b1)" + if (c1 > 0) " + $c1" else "",
                stepResult = "$d1" + if (c2 > 0) " (carry $c2)" else "",
                explanation = "Cross-multiply and sum (($a1×$b0) + ($a0×$b1)${if (c1 > 0) " + carry $c1" else ""} = $rawCross). Write digit $d1${if (c2 > 0) " and carry $c2 to left column." else "."}"
            ),
            DecompositionStep(
                stepNumber = 3,
                label = "Vertical Left (Hundreds)",
                formulaDisplay = "($a1 × $b1)" + if (c2 > 0) " + $c2" else "",
                stepResult = "$rawTens",
                explanation = "Multiply tens digits and add carry ($a1 × $b1${if (c2 > 0) " + $c2" else ""} = $rawTens)."
            ),
            DecompositionStep(
                stepNumber = 4,
                label = "Concatenation",
                formulaDisplay = "$rawTens || $d1 || $d0",
                stepResult = "$correctAnswer",
                explanation = "Combine column results $\\rightarrow$ $correctAnswer."
            )
        )
    }

    private fun build3x2Steps(a: Long, b: Long, correctAnswer: Long): List<DecompositionStep> {
        val a2 = a / 100
        val a1 = (a / 10) % 10
        val a0 = a % 10

        val b1 = b / 10
        val b0 = b % 10

        val rawCol1 = a0 * b0
        val d0 = rawCol1 % 10
        val c1 = rawCol1 / 10

        val rawCol2 = (a1 * b0) + (a0 * b1) + c1
        val d1 = rawCol2 % 10
        val c2 = rawCol2 / 10

        val rawCol3 = (a2 * b0) + (a1 * b1) + c2
        val d2 = rawCol3 % 10
        val c3 = rawCol3 / 10

        val rawCol4 = (a2 * b1) + c3

        return listOf(
            DecompositionStep(
                stepNumber = 1,
                label = "Column 1 (Units Vertical)",
                formulaDisplay = "$a0 × $b0",
                stepResult = "$d0" + if (c1 > 0) " (carry $c1)" else "",
                explanation = "Units column vertical product ($a0 × $b0 = $rawCol1). Digit = $d0, carry = $c1."
            ),
            DecompositionStep(
                stepNumber = 2,
                label = "Column 2 (Tens Crosswise)",
                formulaDisplay = "($a1 × $b0) + ($a0 × $b1)" + if (c1 > 0) " + $c1" else "",
                stepResult = "$d1" + if (c2 > 0) " (carry $c2)" else "",
                explanation = "Tens cross-product sum = $rawCol2. Digit = $d1, carry = $c2."
            ),
            DecompositionStep(
                stepNumber = 3,
                label = "Column 3 (Hundreds Crosswise)",
                formulaDisplay = "($a2 × $b0) + ($a1 × $b1)" + if (c2 > 0) " + $c2" else "",
                stepResult = "$d2" + if (c3 > 0) " (carry $c3)" else "",
                explanation = "Hundreds cross-product sum = $rawCol3. Digit = $d2, carry = $c3."
            ),
            DecompositionStep(
                stepNumber = 4,
                label = "Column 4 (Thousands Vertical)",
                formulaDisplay = "($a2 × $b1)" + if (c3 > 0) " + $c3" else "",
                stepResult = "$rawCol4",
                explanation = "Thousands vertical product plus carry = $rawCol4."
            ),
            DecompositionStep(
                stepNumber = 5,
                label = "Concatenation",
                formulaDisplay = "$rawCol4 || $d2 || $d1 || $d0",
                stepResult = "$correctAnswer",
                explanation = "Combine column results $\\rightarrow$ $correctAnswer."
            )
        )
    }

    private fun generateDistractors(correctAnswer: Long, a: Long, b: Long): List<Long> {
        val list = mutableSetOf<Long>()

        // Common error 1: Forgot carry from crosswise middle
        val a1 = a / 10
        val a0 = a % 10
        val b1 = b / 10
        val b0 = b % 10
        val noCarryCross = (a1 * b0) + (a0 * b1)
        val d1NoCarry = noCarryCross % 10
        val d0Units = (a0 * b0) % 10
        val d2Tens = a1 * b1
        val distractor1 = "$d2Tens$d1NoCarry$d0Units".toLongOrNull()
        if (distractor1 != null && distractor1 != correctAnswer && distractor1 > 0) list.add(distractor1)

        // Common error 2: Swapped cross-multiplication pairs (a1*b1 + a0*b0)
        val distractor2 = correctAnswer + 10
        if (distractor2 != correctAnswer && distractor2 > 0) list.add(distractor2)

        // Common error 3: Dropped tens carry
        val distractor3 = correctAnswer - 100
        if (distractor3 != correctAnswer && distractor3 > 0) list.add(distractor3)

        // Common error 4: Plausible offset
        val distractor4 = correctAnswer + 100
        if (distractor4 != correctAnswer && distractor4 > 0) list.add(distractor4)

        var delta = 10L
        while (list.size < 4) {
            val candidate = correctAnswer + delta
            if (candidate != correctAnswer && candidate > 0) {
                list.add(candidate)
            }
            delta = if (delta > 0) -delta else (-delta + 10L)
        }

        return list.take(4)
    }
}
