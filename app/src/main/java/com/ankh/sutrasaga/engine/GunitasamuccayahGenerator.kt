package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.CoefficientSumCheck
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.ExpansionCheck
import com.ankh.sutrasaga.domain.models.FactorizationProposal
import com.ankh.sutrasaga.domain.models.GunitasamuccayahClassification
import com.ankh.sutrasaga.domain.models.GunitasamuccayahProblem
import com.ankh.sutrasaga.domain.models.GunitasamuccayahSolution
import com.ankh.sutrasaga.domain.models.LinearFactor
import com.ankh.sutrasaga.domain.models.QuadraticPolynomial
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.random.Random

class GunitasamuccayahGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Gunitasamuccayah"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val gProblem = generateGunitaProblem(difficultyTier)
        return convertToSutraProblem(gProblem)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val gProblem = buildValidProblem(LinearFactor(1, 3), LinearFactor(1, 2), DifficultyTier.TIER_1_EASY)
        return convertToSutraProblem(gProblem)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val list = mutableListOf<SutraProblem>()
        repeat(count) {
            list.add(generateProblem(difficultyTier))
        }
        return list
    }

    fun generateGunitaProblem(tier: DifficultyTier): GunitasamuccayahProblem {
        return when (tier) {
            DifficultyTier.TIER_1_EASY -> {
                val a1 = 1L
                val b1 = random.nextLong(1, 6)
                val a2 = 1L
                val b2 = random.nextLong(1, 6)
                buildValidProblem(LinearFactor(a1, b1), LinearFactor(a2, b2), tier)
            }
            DifficultyTier.TIER_2_HARD -> {
                val isFalseProposal = random.nextBoolean()
                if (isFalseProposal) {
                    val a1 = 1L
                    val b1 = random.nextLong(1, 6)
                    val a2 = 1L
                    val b2 = random.nextLong(1, 6)
                    buildFalseProposal(LinearFactor(a1, b1), LinearFactor(a2, b2), tier)
                } else {
                    val a1 = random.nextLong(1, 3)
                    val b1 = random.nextLong(-5, 6)
                    val a2 = random.nextLong(1, 3)
                    val b2 = random.nextLong(-5, 6)
                    buildValidProblem(LinearFactor(a1, b1), LinearFactor(a2, b2), tier)
                }
            }
        }
    }

    fun buildValidProblem(f1: LinearFactor, f2: LinearFactor, tier: DifficultyTier): GunitasamuccayahProblem {
        val exactPoly = QuadraticPolynomial(
            a2 = f1.a * f2.a,
            a1 = f1.a * f2.b + f1.b * f2.a,
            a0 = f1.b * f2.b
        )
        val proposal = FactorizationProposal(listOf(f1, f2), exactPoly)
        val sol = verifyFactorization(proposal)
        val distractors = generateDistractors(sol.exactProduct.sumOfCoefficients(), sol)

        return GunitasamuccayahProblem(
            id = UUID.randomUUID().toString(),
            proposal = proposal,
            solution = sol,
            difficultyTier = tier,
            distractors = distractors
        )
    }

    fun buildFalseProposal(f1: LinearFactor, f2: LinearFactor, tier: DifficultyTier): GunitasamuccayahProblem {
        val exactPoly = QuadraticPolynomial(
            a2 = f1.a * f2.a,
            a1 = f1.a * f2.b + f1.b * f2.a,
            a0 = f1.b * f2.b
        )
        // Construct a claimed polynomial whose sum of coefficients matches exactPoly, but individual terms differ
        // e.g. add 2 to a1 and subtract 2 from a0 (sum remains unchanged)
        val claimedPoly = QuadraticPolynomial(
            a2 = exactPoly.a2,
            a1 = exactPoly.a1 + 2L,
            a0 = exactPoly.a0 - 2L
        )

        val proposal = FactorizationProposal(listOf(f1, f2), claimedPoly)
        val sol = verifyFactorization(proposal)
        val distractors = generateDistractors(sol.exactProduct.sumOfCoefficients(), sol)

        return GunitasamuccayahProblem(
            id = UUID.randomUUID().toString(),
            proposal = proposal,
            solution = sol,
            difficultyTier = tier,
            distractors = distractors
        )
    }

    fun verifyFactorization(proposal: FactorizationProposal): GunitasamuccayahSolution {
        require(proposal.factors.size == 2) { "Currently supports 2 factors only" }
        val f1 = proposal.factors[0]
        val f2 = proposal.factors[1]

        val exactPoly = QuadraticPolynomial(
            a2 = f1.a * f2.a,
            a1 = f1.a * f2.b + f1.b * f2.a,
            a0 = f1.b * f2.b
        )

        val factorSum1 = f1.sumOfCoefficients()
        val factorSum2 = f2.sumOfCoefficients()
        val factorProductSum = factorSum1 * factorSum2
        val claimedSum = proposal.claimedProduct.sumOfCoefficients()

        val sumCheck = CoefficientSumCheck(
            expectedSum = factorProductSum,
            actualSum = claimedSum,
            passed = (factorProductSum == claimedSum)
        )

        val expansionCheck = ExpansionCheck(
            expectedPolynomial = exactPoly,
            actualPolynomial = proposal.claimedProduct,
            passed = (exactPoly == proposal.claimedProduct)
        )

        val classif = when {
            sumCheck.passed && expansionCheck.passed -> GunitasamuccayahClassification.VALID_FACTORISATION
            sumCheck.passed && !expansionCheck.passed -> GunitasamuccayahClassification.INVALID_EXPANSION
            else -> GunitasamuccayahClassification.COEFFICIENT_SUM_MISMATCH
        }

        return GunitasamuccayahSolution(
            proposal = proposal,
            exactProduct = exactPoly,
            sumCheck = sumCheck,
            expansionCheck = expansionCheck,
            classification = classif
        )
    }

    private fun convertToSutraProblem(g: GunitasamuccayahProblem): SutraProblem {
        val f1 = g.proposal.factors[0]
        val f2 = g.proposal.factors[1]
        val claimedStr = g.proposal.claimedProduct.toFormattedString()

        val decSteps = listOf(
            DecompositionStep(
                stepNumber = 1,
                label = "COEFFICIENT_SUM_FACTORS",
                formulaDisplay = "SC(${f1.toFormattedString()}) × SC(${f2.toFormattedString()}) = (${f1.sumOfCoefficients()}) × (${f2.sumOfCoefficients()}) = ${g.solution.sumCheck.expectedSum}",
                stepResult = "Factor Sum Product: ${g.solution.sumCheck.expectedSum}",
                explanation = "Sum of coefficients of factors evaluated at x=1."
            ),
            DecompositionStep(
                stepNumber = 2,
                label = "COEFFICIENT_SUM_CLAIMED",
                formulaDisplay = "SC($claimedStr) = ${g.solution.sumCheck.actualSum}",
                stepResult = "Claimed Product Sum: ${g.solution.sumCheck.actualSum}",
                explanation = "Sum of coefficients of claimed polynomial evaluated at x=1."
            ),
            DecompositionStep(
                stepNumber = 3,
                label = "EXACT_EXPANSION_CHECK",
                formulaDisplay = "${f1.toFormattedString()}${f2.toFormattedString()} = ${g.solution.exactProduct.toFormattedString()}",
                stepResult = if (g.solution.expansionCheck.passed) "Matches Exact Expansion!" else "Expansion Mismatch!",
                explanation = "Exact term-by-term expansion comparison."
            )
        )

        val correctVal = g.solution.sumCheck.expectedSum

        return SutraProblem(
            id = g.id,
            sutraName = sutraName,
            questionText = "Verify factorization ${f1.toFormattedString()}${f2.toFormattedString()} = $claimedStr (Gunitasamuccayah)",
            operand = correctVal,
            correctAnswer = correctVal,
            prefixPart = g.solution.sumCheck.expectedSum,
            incrementedPrefix = g.solution.sumCheck.actualSum,
            prefixProduct = if (g.solution.classification == GunitasamuccayahClassification.VALID_FACTORISATION) 1L else 0L,
            appendedSuffix = g.solution.classification.name,
            decompositionSteps = decSteps,
            distractors = g.distractors,
            difficultyTier = g.difficultyTier,
            answerFormat = AnswerFormat.INTEGER
        )
    }

    private fun generateDistractors(correctVal: Long, sol: GunitasamuccayahSolution): List<Long> {
        val set = mutableSetOf<Long>()
        set.add(correctVal + 2L)
        set.add(correctVal - 2L)
        set.add(correctVal + 5L)
        set.add(correctVal + 10L)

        var delta = 1L
        while (set.size < 4) {
            val candidate = correctVal + delta * 3L
            if (candidate != correctVal) {
                set.add(candidate)
            }
            delta = if (delta > 0) -delta else (-delta + 2L)
        }

        return set.take(4)
    }
}
