package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.OsculationSolution
import com.ankh.sutrasaga.domain.models.OsculationStep
import com.ankh.sutrasaga.domain.models.SupportedDivisor
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.domain.models.VestanamClassification
import com.ankh.sutrasaga.engine.UpaSutraGenerator
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

/**
 * Problem generator and solver for Upa-Sutra 5: Vestanam ("By Osculation" — Divisibility Testing).
 */
class VestanamGenerator(
    private val random: Random = Random.Default
) : UpaSutraGenerator {

    override val upaSutraId: UpaSutraId = UpaSutraId.VESHTANAM
    override val sutraName: String = "Vestanam"

    fun solve(number: Long, divisorVal: Long): OsculationSolution {
        val supported = when (divisorVal) {
            7L -> SupportedDivisor.SEVEN
            13L -> SupportedDivisor.THIRTEEN
            19L -> SupportedDivisor.NINETEEN
            else -> null
        }

        if (supported == null) {
            return OsculationSolution(
                number = number,
                divisor = SupportedDivisor.NINETEEN,
                isDivisible = false,
                finalValue = number,
                steps = emptyList(),
                classification = VestanamClassification.UNSUPPORTED_DIVISOR
            )
        }

        return solve(number, supported)
    }

    fun solve(number: Long, divisor: SupportedDivisor): OsculationSolution {
        if (abs(number) > 10000000L) {
            return OsculationSolution(
                number = number,
                divisor = divisor,
                isDivisible = number % divisor.divisor == 0L,
                finalValue = number,
                steps = emptyList(),
                classification = VestanamClassification.OVERFLOW_RISK
            )
        }

        val d = divisor.divisor
        val m = divisor.osculator
        val isNegative = divisor.isNegative
        val isDivisible = number % d == 0L

        val steps = mutableListOf<OsculationStep>()
        var current = number
        var stepCount = 0

        // Perform osculation iterations until number is small (< 100 or <= 2*d)
        while (current >= 100L && stepCount < 10) {
            stepCount++
            val lastDigit = current % 10L
            val rest = current / 10L
            val osculatedProd = m * lastDigit
            val nextVal = if (isNegative) rest - osculatedProd else rest + osculatedProd

            val opSign = if (isNegative) "-" else "+"
            val explanation = "Last digit $lastDigit, rest $rest. $rest $opSign ($m × $lastDigit) = $nextVal."

            steps.add(
                OsculationStep(
                    stepNumber = stepCount,
                    inputValue = current,
                    restValue = rest,
                    lastDigit = lastDigit,
                    osculatedProduct = osculatedProd,
                    resultValue = nextVal,
                    explanation = explanation
                )
            )

            current = nextVal
        }

        val classification = if (isDivisible) VestanamClassification.DIVISIBLE else VestanamClassification.NOT_DIVISIBLE

        return OsculationSolution(
            number = number,
            divisor = divisor,
            isDivisible = isDivisible,
            finalValue = current,
            steps = steps,
            classification = classification
        )
    }

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val (number, divisor) = when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> {
                // Divisor 19, 3-4 digit numbers
                val d = SupportedDivisor.NINETEEN
                val isDivisible = random.nextBoolean()
                val num = if (isDivisible) {
                    random.nextLong(11, 150) * d.divisor
                } else {
                    val base = random.nextLong(11, 150) * d.divisor
                    val offset = random.nextLong(1, d.divisor)
                    base + offset
                }
                Pair(num, d)
            }
            DifficultyTier.TIER_2_HARD -> {
                // Divisor 13, 3-4 digit numbers
                val d = SupportedDivisor.THIRTEEN
                val isDivisible = random.nextBoolean()
                val num = if (isDivisible) {
                    random.nextLong(11, 200) * d.divisor
                } else {
                    val base = random.nextLong(11, 200) * d.divisor
                    val offset = random.nextLong(1, d.divisor)
                    base + offset
                }
                Pair(num, d)
            }
        }

        return buildProblemForNumber(number, divisor, difficultyTier)
    }

    fun generateTier3Problem(): SutraProblem {
        // Tier 3: Divisor 7 (negative osculator) or mixed
        val d = SupportedDivisor.SEVEN
        val isDivisible = random.nextBoolean()
        val num = if (isDivisible) {
            random.nextLong(15, 300) * d.divisor
        } else {
            val base = random.nextLong(15, 300) * d.divisor
            val offset = random.nextLong(1, d.divisor)
            base + offset
        }
        return buildProblemForNumber(num, d, DifficultyTier.TIER_2_HARD)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        // Canonical 247 on divisor 19
        return buildProblemForNumber(247L, SupportedDivisor.NINETEEN, DifficultyTier.TIER_1_EASY)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val set = mutableListOf<SutraProblem>()
        val seen = mutableSetOf<Long>()
        var attempts = 0

        while (set.size < count && attempts < count * 20) {
            attempts++
            val prob = if (attempts % 3 == 0) generateTier3Problem() else generateProblem(difficultyTier)
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

    private fun buildProblemForNumber(
        number: Long,
        divisor: SupportedDivisor,
        tier: DifficultyTier
    ): SutraProblem {
        val solution = solve(number, divisor)
        val finalVal = solution.finalValue
        val isDivisible = solution.isDivisible
        val opText = if (divisor.isNegative) "Subtract" else "Add"

        val decompositionSteps = solution.steps.map { step ->
            DecompositionStep(
                stepNumber = step.stepNumber,
                label = "Osculate Step ${step.stepNumber}",
                formulaDisplay = "${step.restValue} ${if (divisor.isNegative) "-" else "+"} (${divisor.osculator} × ${step.lastDigit})",
                stepResult = "${step.resultValue}",
                explanation = step.explanation
            )
        }

        val distractors = listOf(
            finalVal + divisor.divisor,
            finalVal - divisor.divisor,
            finalVal + 1,
            finalVal - 1
        ).filter { it != finalVal && it > 0 }.distinct().take(3)

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = "Test $number ÷ ${divisor.divisor} ($opText ${divisor.osculator}×last digit) — Reduced value?",
            operand = number,
            correctAnswer = finalVal,
            prefixPart = divisor.divisor,
            incrementedPrefix = divisor.osculator,
            prefixProduct = if (isDivisible) 1L else 0L,
            appendedSuffix = if (isDivisible) "DIVISIBLE" else "NOT_DIVISIBLE",
            decompositionSteps = decompositionSteps,
            distractors = distractors,
            difficultyTier = tier,
            answerFormat = AnswerFormat.INTEGER,
            expectedRemainder = 0,
            expectedSecondaryAnswer = 0
        )
    }
}
