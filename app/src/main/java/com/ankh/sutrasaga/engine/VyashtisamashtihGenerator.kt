package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.SymmetricParts
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.models.VyashtisamashtihClassification
import com.ankh.sutrasaga.domain.models.VyashtisamashtihOperationType
import com.ankh.sutrasaga.domain.models.VyashtisamashtihProblem
import com.ankh.sutrasaga.domain.models.VyashtisamashtihSolution
import com.ankh.sutrasaga.domain.models.VyashtisamashtihStep
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

class VyashtisamashtihGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Vyashtisamashtih"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val vProblem = generateSymmetricProblem(difficultyTier)
        return convertToSutraProblem(vProblem)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val vProblem = buildFromFactors(Fraction(58, 1), Fraction(62, 1), DifficultyTier.TIER_1_EASY)
        return convertToSutraProblem(vProblem)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val list = mutableListOf<SutraProblem>()
        repeat(count) {
            list.add(generateProblem(difficultyTier))
        }
        return list
    }

    fun generateSymmetricProblem(tier: DifficultyTier): VyashtisamashtihProblem {
        return when (tier) {
            DifficultyTier.TIER_1_EASY -> {
                // Integer average & integer deviation, 2-digit factors
                val avg = random.nextLong(20L, 80L)
                val dev = random.nextLong(1L, 9L)
                val p = Fraction(avg - dev, 1)
                val q = Fraction(avg + dev, 1)
                buildFromFactors(p, q, tier)
            }
            DifficultyTier.TIER_2_HARD -> {
                val isFractional = random.nextBoolean()
                if (isFractional) {
                    // Odd sum pair producing fractional average (e.g. 12 x 15 -> avg 27/2, dev 3/2)
                    val pVal = random.nextLong(10L, 30L)
                    val qVal = pVal + random.nextLong(1L, 5L) * 2L - 1L // odd difference -> odd sum
                    buildFromFactors(Fraction(pVal, 1), Fraction(qVal, 1), tier)
                } else {
                    // Larger factors near powers of 10 (e.g. 94 x 106 -> avg 100, dev 6)
                    val avg = random.nextLong(90L, 200L)
                    val dev = random.nextLong(2L, 15L)
                    val p = Fraction(avg - dev, 1)
                    val q = Fraction(avg + dev, 1)
                    buildFromFactors(p, q, tier)
                }
            }
        }
    }

    fun buildFromFactors(p: Fraction, q: Fraction, tier: DifficultyTier): VyashtisamashtihProblem {
        val sol = solveSymmetricProduct(p, q)
        val correctVal = sol.product.reduced.numerator
        val distractors = generateDistractors(correctVal, sol)

        return VyashtisamashtihProblem(
            id = UUID.randomUUID().toString(),
            left = p,
            right = q,
            parts = sol.parts,
            solution = sol,
            difficultyTier = tier,
            distractors = distractors
        )
    }

    fun solveSymmetricProduct(p: Fraction, q: Fraction): VyashtisamashtihSolution {
        val pRed = p.reduced
        val qRed = q.reduced

        // A = (p + q) / 2
        val numSum = pRed.numerator * qRed.denominator + qRed.numerator * pRed.denominator
        val denSum = pRed.denominator * qRed.denominator * 2L
        val avg = Fraction(numSum, denSum).reduced

        // d = |q - p| / 2
        val numDiff = abs(qRed.numerator * pRed.denominator - pRed.numerator * qRed.denominator)
        val dev = Fraction(numDiff, denSum).reduced

        val parts = SymmetricParts(
            wholeAverage = avg,
            negativeDeviation = Fraction(-dev.numerator, dev.denominator).reduced,
            positiveDeviation = dev
        )

        val wholeSquare = Fraction(avg.numerator * avg.numerator, avg.denominator * avg.denominator).reduced
        val devSquare = Fraction(dev.numerator * dev.numerator, dev.denominator * dev.denominator).reduced

        // Product = A^2 - d^2 = p * q
        val prodNum = wholeSquare.numerator * devSquare.denominator - devSquare.numerator * wholeSquare.denominator
        val prodDen = wholeSquare.denominator * devSquare.denominator
        val product = Fraction(prodNum, prodDen).reduced

        val isIntegerPath = (avg.denominator == 1L && dev.denominator == 1L)
        val classif = if (isIntegerPath) VyashtisamashtihClassification.SUPPORTED_INTEGER_PATH else VyashtisamashtihClassification.SUPPORTED_FRACTION_PATH

        val steps = mutableListOf<VyashtisamashtihStep>()

        // Step 1: Split
        steps.add(
            VyashtisamashtihStep(
                stepIndex = 1,
                operationType = VyashtisamashtihOperationType.SPLIT,
                formulaDisplay = "${pRed.toFormattedString()} × ${qRed.toFormattedString()}",
                stepResult = "Factors identified: ${pRed.toFormattedString()}, ${qRed.toFormattedString()}",
                explanationText = "Identify the two factors $pRed and $qRed for symmetric decomposition."
            )
        )

        // Step 2: Find Average (Whole)
        steps.add(
            VyashtisamashtihStep(
                stepIndex = 2,
                operationType = VyashtisamashtihOperationType.FIND_AVERAGE,
                formulaDisplay = "A = (${pRed.toFormattedString()} + ${qRed.toFormattedString()}) / 2 = ${avg.toFormattedString()}",
                stepResult = "Whole Average A = ${avg.toFormattedString()}",
                explanationText = "Find the midpoint/average A of the two factors."
            )
        )

        // Step 3: Find Deviation (Parts)
        steps.add(
            VyashtisamashtihStep(
                stepIndex = 3,
                operationType = VyashtisamashtihOperationType.FIND_DEVIATION,
                formulaDisplay = "d = (${qRed.toFormattedString()} - ${pRed.toFormattedString()}) / 2 = ${dev.toFormattedString()}",
                stepResult = "Half-difference d = ${dev.toFormattedString()}",
                explanationText = "Find the half-difference deviation d from the average."
            )
        )

        // Step 4: Square Whole
        steps.add(
            VyashtisamashtihStep(
                stepIndex = 4,
                operationType = VyashtisamashtihOperationType.SQUARE_WHOLE,
                formulaDisplay = "A² = (${avg.toFormattedString()})² = ${wholeSquare.toFormattedString()}",
                stepResult = "A² = ${wholeSquare.toFormattedString()}",
                explanationText = "Square the whole average A."
            )
        )

        // Step 5: Square Deviation
        steps.add(
            VyashtisamashtihStep(
                stepIndex = 5,
                operationType = VyashtisamashtihOperationType.SQUARE_DEVIATION,
                formulaDisplay = "d² = (${dev.toFormattedString()})² = ${devSquare.toFormattedString()}",
                stepResult = "d² = ${devSquare.toFormattedString()}",
                explanationText = "Square the deviation d."
            )
        )

        // Step 6: Subtract
        steps.add(
            VyashtisamashtihStep(
                stepIndex = 6,
                operationType = VyashtisamashtihOperationType.SUBTRACT,
                formulaDisplay = "A² - d² = ${wholeSquare.toFormattedString()} - ${devSquare.toFormattedString()} = ${product.toFormattedString()}",
                stepResult = "Product = ${product.toFormattedString()}",
                explanationText = "Subtract d² from A² to obtain the final product."
            )
        )

        return VyashtisamashtihSolution(
            classification = classif,
            left = pRed,
            right = qRed,
            parts = parts,
            wholeSquare = wholeSquare,
            deviationSquare = devSquare,
            product = product,
            steps = steps
        )
    }

    private fun convertToSutraProblem(v: VyashtisamashtihProblem): SutraProblem {
        val decSteps = v.solution.steps.map { s ->
            DecompositionStep(
                stepNumber = s.stepIndex,
                label = s.operationType.name,
                formulaDisplay = s.formulaDisplay,
                stepResult = s.stepResult,
                explanation = s.explanationText
            )
        }

        val ans = v.solution.product.reduced.numerator

        return SutraProblem(
            id = v.id,
            sutraName = sutraName,
            questionText = "Multiply using Vyashtisamashtih (Part & Whole): ${v.left.toFormattedString()} × ${v.right.toFormattedString()}",
            operand = ans,
            correctAnswer = ans,
            prefixPart = v.solution.parts.wholeAverage.numerator,
            incrementedPrefix = v.solution.parts.positiveDeviation.numerator,
            prefixProduct = v.solution.wholeSquare.numerator,
            appendedSuffix = v.solution.deviationSquare.numerator.toString(),
            decompositionSteps = decSteps,
            distractors = v.distractors,
            difficultyTier = v.difficultyTier,
            answerFormat = AnswerFormat.INTEGER
        )
    }

    private fun generateDistractors(correctVal: Long, sol: VyashtisamashtihSolution): List<Long> {
        val list = mutableSetOf<Long>()

        // Error 1: Adding A^2 + d^2 instead of subtracting
        val wrongAdd = sol.wholeSquare.numerator + sol.deviationSquare.numerator
        if (wrongAdd != correctVal) list.add(wrongAdd)

        // Error 2: Using full difference instead of half difference
        val fullDiff = sol.parts.positiveDeviation.numerator * 2L
        val wrongFullDiff = sol.wholeSquare.numerator - (fullDiff * fullDiff)
        if (wrongFullDiff != correctVal) list.add(wrongFullDiff)

        list.add(correctVal + 10L)
        list.add(correctVal - 10L)

        var delta = 1L
        while (list.size < 4) {
            val candidate = correctVal + delta
            if (candidate != correctVal) {
                list.add(candidate)
            }
            delta = if (delta > 0) -delta else (-delta + 2L)
        }

        return list.take(4)
    }
}
