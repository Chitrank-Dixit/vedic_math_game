package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.BivariateQuadratic
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.LinearBivariateFactor
import com.ankh.sutrasaga.domain.models.LopanaClassification
import com.ankh.sutrasaga.domain.models.LopanaSolution
import com.ankh.sutrasaga.domain.models.LopanaStep
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.engine.UpaSutraGenerator
import java.util.UUID
import kotlin.random.Random

/**
 * Problem generator and solver for Upa-Sutra 11: Lopanasthapanabhyam
 * ("By Elimination and Retention" — Bivariate Quadratic Factorization).
 */
class LopanasthapanabhyamGenerator(
    private val random: Random = Random.Default
) : UpaSutraGenerator {

    override val upaSutraId: UpaSutraId = UpaSutraId.LOPANA_STHAPANABHYAM
    override val sutraName: String = "Lopanasthapanabhyam"

    fun solve(factor1: LinearBivariateFactor, factor2: LinearBivariateFactor): LopanaSolution {
        val p1 = factor1.px
        val q1 = factor1.py
        val r1 = factor1.constant

        val p2 = factor2.px
        val q2 = factor2.py
        val r2 = factor2.constant

        val a = p1 * p2
        val b = p1 * q2 + q1 * p2
        val c = q1 * q2
        val d = p1 * r2 + r1 * p2
        val e = q1 * r2 + r1 * q2
        val f = r1 * r2

        val target = BivariateQuadratic(a, b, c, d, e, f)

        val elimYPoly = "${a}x² ${if (d >= 0) "+ ${d}x" else "- ${-d}x"} ${if (f >= 0) "+ $f" else "- ${-f}"}"
        val elimXPoly = "${c}y² ${if (e >= 0) "+ ${e}y" else "- ${-e}y"} ${if (f >= 0) "+ $f" else "- ${-f}"}"

        val steps = mutableListOf<LopanaStep>()

        steps.add(
            LopanaStep(
                stepNumber = 1,
                label = "Eliminate y (y = 0)",
                formulaDisplay = "P(x, 0) = $elimYPoly",
                stepResult = "(${p1}x + $r1)(${p2}x + $r2)",
                explanation = "Setting y = 0 leaves a univariate quadratic in x with roots/factors containing constant terms $r1 and $r2."
            )
        )

        steps.add(
            LopanaStep(
                stepNumber = 2,
                label = "Eliminate x (x = 0)",
                formulaDisplay = "P(0, y) = $elimXPoly",
                stepResult = "(${q1}y + $r1)(${q2}y + $r2)",
                explanation = "Setting x = 0 leaves a univariate quadratic in y with factors containing the same constant terms $r1 and $r2."
            )
        )

        steps.add(
            LopanaStep(
                stepNumber = 3,
                label = "Retain & Recombine Factors",
                formulaDisplay = "Match constant $r1 and constant $r2",
                stepResult = "${factor1.formatDisplay()}${factor2.formatDisplay()}",
                explanation = "Pair (${p1}x + $r1) with (${q1}y + $r1) ⟹ ${factor1.formatDisplay()}; pair (${p2}x + $r2) with (${q2}y + $r2) ⟹ ${factor2.formatDisplay()}."
            )
        )

        steps.add(
            LopanaStep(
                stepNumber = 4,
                label = "Expand & Verify Mixed Cross-Term",
                formulaDisplay = "(${p1}x)(${q2}y) + (${q1}y)(${p2}x) = (${p1 * q2} + ${q1 * p2})xy",
                stepResult = "${b}xy (Matches B = $b)",
                explanation = "The mixed cross-term equals exactly ${b}xy, confirming the complete factorization ${factor1.formatDisplay()}${factor2.formatDisplay()}."
            )
        )

        // Check for plausible rejected candidate if constants are distinct
        val rejectedCandidate = if (r1 != r2) {
            val wrongB = p1 * q1 + p2 * q2
            if (wrongB != b) {
                "(${p1}x + ${q2}y + $r1)(${p2}x + ${q1}y + $r2) ⟹ xy term = ${wrongB}xy (≠ ${b}xy)"
            } else null
        } else null

        return LopanaSolution(
            target = target,
            factor1 = factor1,
            factor2 = factor2,
            eliminatedYPolynomial = elimYPoly,
            eliminatedXPolynomial = elimXPoly,
            rejectedCandidate = rejectedCandidate,
            steps = steps,
            classification = if (rejectedCandidate != null) LopanaClassification.AMBIGUOUS_RECOMBINATION else LopanaClassification.FACTORED
        )
    }

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val (f1, f2) = when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> generateTier1Factors()
            DifficultyTier.TIER_2_HARD -> generateTier2Factors()
        }
        return buildProblem(f1, f2, difficultyTier)
    }

    fun generateTier3Problem(): SutraProblem {
        val (f1, f2) = generateTier3Factors()
        return buildProblem(f1, f2, DifficultyTier.TIER_2_HARD)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        // Canonical example: (2x + y + 2)(x + 2y + 1)
        val f1 = LinearBivariateFactor(2, 1, 2)
        val f2 = LinearBivariateFactor(1, 2, 1)
        return buildProblem(f1, f2, DifficultyTier.TIER_1_EASY)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val set = mutableListOf<SutraProblem>()
        val seen = mutableSetOf<String>()
        var attempts = 0

        while (set.size < count && attempts < count * 20) {
            attempts++
            val (f1, f2) = when (difficultyTier) {
                DifficultyTier.TIER_1_EASY -> generateTier1Factors()
                DifficultyTier.TIER_2_HARD -> if (attempts % 2 == 0) generateTier2Factors() else generateTier3Factors()
            }
            val key = "${f1.formatDisplay()}${f2.formatDisplay()}"
            if (!seen.contains(key)) {
                seen.add(key)
                set.add(buildProblem(f1, f2, difficultyTier))
            }
        }

        while (set.size < count) {
            val (f1, f2) = generateTier1Factors()
            set.add(buildProblem(f1, f2, difficultyTier))
        }

        return set
    }

    private fun generateTier1Factors(): Pair<LinearBivariateFactor, LinearBivariateFactor> {
        val p1 = random.nextLong(1, 3)
        val q1 = random.nextLong(1, 3)
        val r1 = random.nextLong(1, 4)

        val p2 = random.nextLong(1, 3)
        val q2 = random.nextLong(1, 3)
        val r2 = random.nextLong(1, 4)

        return Pair(LinearBivariateFactor(p1, q1, r1), LinearBivariateFactor(p2, q2, r2))
    }

    private fun generateTier2Factors(): Pair<LinearBivariateFactor, LinearBivariateFactor> {
        val p1 = 1L
        val q1 = if (random.nextBoolean()) 1L else -1L
        val r1 = if (random.nextBoolean()) random.nextLong(1, 4) else -random.nextLong(1, 4)

        val p2 = 1L
        val q2 = if (random.nextBoolean()) 2L else -2L
        val r2 = if (random.nextBoolean()) random.nextLong(1, 4) else -random.nextLong(1, 4)

        return Pair(LinearBivariateFactor(p1, q1, r1), LinearBivariateFactor(p2, q2, r2))
    }

    private fun generateTier3Factors(): Pair<LinearBivariateFactor, LinearBivariateFactor> {
        val p1 = 2L
        val q1 = 1L
        val r1 = random.nextLong(2, 5)

        val p2 = 1L
        val q2 = 2L
        val r2 = 1L

        return Pair(LinearBivariateFactor(p1, q1, r1), LinearBivariateFactor(p2, q2, r2))
    }

    private fun buildProblem(
        factor1: LinearBivariateFactor,
        factor2: LinearBivariateFactor,
        tier: DifficultyTier
    ): SutraProblem {
        val solution = solve(factor1, factor2)
        val target = solution.target

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
            factor1.constant + 1L,
            factor1.constant - 1L,
            factor2.constant + 2L,
            -factor1.constant
        ).filter { it != factor1.constant && it != factor2.constant }.distinct().take(3)

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = "Constant term of factor in ${target.formatDisplay()}?",
            operand = target.f,
            correctAnswer = factor1.constant,
            prefixPart = factor1.px,
            incrementedPrefix = factor1.py,
            prefixProduct = factor2.px,
            appendedSuffix = solution.factoredFormDisplay,
            decompositionSteps = decompositionSteps,
            distractors = distractors,
            difficultyTier = tier,
            answerFormat = AnswerFormat.INTEGER,
            expectedRemainder = 0,
            expectedSecondaryAnswer = factor2.constant
        )
    }
}
