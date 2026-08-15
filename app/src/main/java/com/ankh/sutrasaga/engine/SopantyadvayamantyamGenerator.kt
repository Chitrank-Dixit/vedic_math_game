package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.CarryStep
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.RawPositionValue
import com.ankh.sutrasaga.domain.models.SandwichedDigits
import com.ankh.sutrasaga.domain.models.SopantyadvayamantyamProblem
import com.ankh.sutrasaga.domain.models.SopantyadvayamantyamSolution
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.random.Random

class SopantyadvayamantyamGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Sopantyadvayamantyam"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val sProblem = generateSopantyaProblem(difficultyTier)
        return convertToSutraProblem(sProblem)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val sProblem = buildProblem(143, 12, DifficultyTier.TIER_1_EASY)
        return convertToSutraProblem(sProblem)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val list = mutableListOf<SutraProblem>()
        repeat(count) {
            list.add(generateProblem(difficultyTier))
        }
        return list
    }

    fun generateSopantyaProblem(tier: DifficultyTier): SopantyadvayamantyamProblem {
        return when (tier) {
            DifficultyTier.TIER_1_EASY -> {
                val mult = if (random.nextBoolean()) 12 else 13
                val multiplicand = random.nextLong(11, 999)
                buildProblem(multiplicand, mult, tier)
            }
            DifficultyTier.TIER_2_HARD -> {
                val mult = random.nextInt(14, 20)
                val multiplicand = random.nextLong(100, 9999)
                buildProblem(multiplicand, mult, tier)
            }
        }
    }

    fun buildProblem(multiplicand: Long, multiplier: Int, tier: DifficultyTier): SopantyadvayamantyamProblem {
        require(multiplier in 12..19) { "Multiplier must be between 12 and 19" }
        require(multiplicand > 0) { "Multiplicand must be positive" }

        val sol = solveMultiplication(multiplicand, multiplier)
        val distractors = generateDistractors(sol.product, multiplicand, multiplier)

        return SopantyadvayamantyamProblem(
            id = UUID.randomUUID().toString(),
            multiplicand = multiplicand,
            multiplier = multiplier,
            solution = sol,
            difficultyTier = tier,
            distractors = distractors
        )
    }

    fun solveMultiplication(multiplicand: Long, multiplier: Int): SopantyadvayamantyamSolution {
        require(multiplier in 12..19) { "Multiplier must be between 12 and 19" }
        val n = multiplier - 10

        val origDigits = multiplicand.toString().map { it.digitToInt() }
        val sandwiched = listOf(0) + origDigits + listOf(0)
        val sandwichedDigits = SandwichedDigits(origDigits, sandwiched)

        val rawValues = mutableListOf<RawPositionValue>()
        for (i in 0 until origDigits.size + 1) {
            val digit = sandwiched[i + 1]
            val prevDigit = sandwiched[i]
            val rawVal = digit + n * prevDigit
            rawValues.add(
                RawPositionValue(
                    index = i,
                    digit = digit,
                    previousDigit = prevDigit,
                    n = n,
                    rawValue = rawVal
                )
            )
        }

        val carryStepsReversed = mutableListOf<CarryStep>()
        var currentCarry = 0
        val finalDigitsReversed = mutableListOf<Int>()

        for (i in rawValues.indices.reversed()) {
            val raw = rawValues[i].rawValue
            val total = raw + currentCarry
            val outDigit = total % 10
            val outCarry = total / 10

            carryStepsReversed.add(
                0,
                CarryStep(
                    index = i,
                    incomingRaw = raw,
                    incomingCarry = currentCarry,
                    outputDigit = outDigit,
                    outgoingCarry = outCarry
                )
            )

            finalDigitsReversed.add(outDigit)
            currentCarry = outCarry
        }

        if (currentCarry > 0) {
            finalDigitsReversed.add(currentCarry)
        }

        val finalDigits = finalDigitsReversed.reversed()
        val product = multiplicand * multiplier.toLong()

        return SopantyadvayamantyamSolution(
            multiplicand = multiplicand,
            multiplier = multiplier,
            n = n,
            sandwichedDigits = sandwichedDigits,
            rawValues = rawValues,
            carrySteps = carryStepsReversed,
            finalDigits = finalDigits,
            product = product
        )
    }

    private fun convertToSutraProblem(s: SopantyadvayamantyamProblem): SutraProblem {
        val decSteps = s.solution.rawValues.map { rv ->
            DecompositionStep(
                stepNumber = rv.index + 1,
                label = "RAW_POS_${rv.index}",
                formulaDisplay = "${rv.digit} + ${rv.n} × ${rv.previousDigit} = ${rv.rawValue}",
                stepResult = "Raw Value: ${rv.rawValue}",
                explanation = "Pair digit ${rv.digit} with ${rv.n} × previous digit ${rv.previousDigit} to get raw value ${rv.rawValue}."
            )
        }

        return SutraProblem(
            id = s.id,
            sutraName = sutraName,
            questionText = "Multiply ${s.multiplicand} × ${s.multiplier} using Sopantyadvayamantyam",
            operand = s.solution.product,
            correctAnswer = s.solution.product,
            prefixPart = s.multiplicand,
            incrementedPrefix = s.multiplier.toLong(),
            prefixProduct = s.solution.n.toLong(),
            appendedSuffix = s.solution.product.toString(),
            decompositionSteps = decSteps,
            distractors = s.distractors,
            difficultyTier = s.difficultyTier,
            answerFormat = AnswerFormat.INTEGER
        )
    }

    private fun generateDistractors(correctProduct: Long, multiplicand: Long, multiplier: Int): List<Long> {
        val set = mutableSetOf<Long>()
        // Mistake 1: wrong N (e.g. using full multiplier instead of N)
        val wrongNProduct = multiplicand * (multiplier + 2L)
        set.add(wrongNProduct)

        // Mistake 2: dropping carries (off by ~10 or 100)
        set.add(correctProduct - 10L)
        set.add(correctProduct + 10L)
        set.add(correctProduct + 100L)

        var delta = 1L
        while (set.size < 4) {
            val candidate = correctProduct + delta * 5L
            if (candidate != correctProduct) {
                set.add(candidate)
            }
            delta = if (delta > 0) -delta else (-delta + 2L)
        }

        return set.take(4)
    }
}
