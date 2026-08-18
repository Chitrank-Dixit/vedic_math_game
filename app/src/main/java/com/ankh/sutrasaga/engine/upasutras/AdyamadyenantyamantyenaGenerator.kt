package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.AdyamadyaClassification
import com.ankh.sutrasaga.domain.models.AdyamadyaSolution
import com.ankh.sutrasaga.domain.models.AdyamadyaStep
import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.FirstLastCandidate
import com.ankh.sutrasaga.domain.models.NonMonicQuadratic
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.engine.UpaSutraGenerator
import java.util.UUID
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Problem generator and solver for Upa-Sutra 3: Adyamadyenantyamantyena
 * ("The first by the first and the last by the last" — Non-Monic Quadratic Factorization).
 */
class AdyamadyenantyamantyenaGenerator(
    private val random: Random = Random.Default
) : UpaSutraGenerator {

    override val upaSutraId: UpaSutraId = UpaSutraId.ADYAMADYENANTYAMANTYENA
    override val sutraName: String = "Adyamadyenantyamantyena"

    fun solve(a: Long, b: Long, c: Long): AdyamadyaSolution {
        val quad = NonMonicQuadratic(a, b, c)
        val classification = classify(a, b, c)

        if (classification != AdyamadyaClassification.FACTORABLE_NON_MONIC) {
            val steps = mutableListOf<AdyamadyaStep>()
            steps.add(
                AdyamadyaStep(
                    stepNumber = 1,
                    label = "Examine Quadratic Form",
                    formulaDisplay = quad.toFormattedString(),
                    stepResult = classification.name,
                    explanation = when (classification) {
                        AdyamadyaClassification.MONIC_DEFERRED_TO_WORLD_15 -> "Leading coefficient A=1 is monic; handled in World 15 (Gunakasamuccayah)."
                        AdyamadyaClassification.NO_INTEGER_FACTORIZATION -> "Discriminant D = $b² - 4($a)($c) = ${quad.discriminant}. No rational integer factors exist."
                        AdyamadyaClassification.OVERFLOW_RISK -> "Coefficients exceed safe calculation bounds."
                        else -> ""
                    }
                )
            )
            return AdyamadyaSolution(
                quadratic = quad,
                confirmedCandidate = null,
                attemptedCandidates = emptyList(),
                steps = steps,
                classification = classification
            )
        }

        val firstPairs = getPositiveFactorPairs(a)
        val lastPairs = getSignedFactorPairs(c)
        val attempted = mutableListOf<FirstLastCandidate>()
        var confirmed: FirstLastCandidate? = null

        val steps = mutableListOf<AdyamadyaStep>()
        steps.add(
            AdyamadyaStep(
                stepNumber = 1,
                label = "First-Term Factor Pairs (A = $a)",
                formulaDisplay = "a₁ × a₂ = $a",
                stepResult = firstPairs.joinToString(", ") { "(${it.first}, ${it.second})" },
                explanation = "Find positive factor pairs for first term $a."
            )
        )
        steps.add(
            AdyamadyaStep(
                stepNumber = 2,
                label = "Last-Term Factor Pairs (C = $c)",
                formulaDisplay = "c₁ × c₂ = $c",
                stepResult = lastPairs.take(6).joinToString(", ") { "(${it.first}, ${it.second})" },
                explanation = "Find integer factor pairs for last term $c."
            )
        )

        for ((a1, a2) in firstPairs) {
            for ((c1, c2) in lastPairs) {
                val cand1 = FirstLastCandidate(a1, a2, c1, c2)
                attempted.add(cand1)
                if (cand1.crossTerm == b && confirmed == null) {
                    confirmed = cand1
                }

                val cand2 = FirstLastCandidate(a1, a2, c2, c1)
                if (cand2 != cand1) {
                    attempted.add(cand2)
                    if (cand2.crossTerm == b && confirmed == null) {
                        confirmed = cand2
                    }
                }
            }
        }

        if (confirmed != null) {
            steps.add(
                AdyamadyaStep(
                    stepNumber = 3,
                    label = "Test Cross-Term: a₁c₂ + a₂c₁",
                    formulaDisplay = "${confirmed.a1}(${confirmed.c2}) + ${confirmed.a2}(${confirmed.c1})",
                    stepResult = "${confirmed.crossTerm} (Matches B = $b)",
                    explanation = "Cross-multiplication yields ${confirmed.a1}×(${confirmed.c2}) + ${confirmed.a2}×(${confirmed.c1}) = $b. Confirmed!"
                )
            )
            steps.add(
                AdyamadyaStep(
                    stepNumber = 4,
                    label = "Factorization Result & Expansion Verification",
                    formulaDisplay = confirmed.toFactorString(),
                    stepResult = quad.toFormattedString(),
                    explanation = "Confirmed factors: ${confirmed.toFactorString()}. Expanded: ${quad.toFormattedString()}."
                )
            )
        }

        return AdyamadyaSolution(
            quadratic = quad,
            confirmedCandidate = confirmed,
            attemptedCandidates = attempted,
            steps = steps,
            classification = classification
        )
    }

    fun classify(a: Long, b: Long, c: Long): AdyamadyaClassification {
        if (abs(a) > 100000L || abs(b) > 100000L || abs(c) > 100000L) return AdyamadyaClassification.OVERFLOW_RISK
        if (a == 1L) return AdyamadyaClassification.MONIC_DEFERRED_TO_WORLD_15
        if (a <= 0L) return AdyamadyaClassification.NO_INTEGER_FACTORIZATION

        val disc = b * b - 4 * a * c
        if (disc < 0L) return AdyamadyaClassification.NO_INTEGER_FACTORIZATION
        val rootD = sqrt(disc.toDouble()).toLong()
        if (rootD * rootD != disc) return AdyamadyaClassification.NO_INTEGER_FACTORIZATION

        // Check if integer factors exist
        val firstPairs = getPositiveFactorPairs(a)
        val lastPairs = getSignedFactorPairs(c)
        for ((a1, a2) in firstPairs) {
            for ((c1, c2) in lastPairs) {
                if (a1 * c2 + a2 * c1 == b || a1 * c1 + a2 * c2 == b) {
                    return AdyamadyaClassification.FACTORABLE_NON_MONIC
                }
            }
        }

        return AdyamadyaClassification.NO_INTEGER_FACTORIZATION
    }

    private fun getPositiveFactorPairs(n: Long): List<Pair<Long, Long>> {
        val pairs = mutableListOf<Pair<Long, Long>>()
        val absN = abs(n)
        val limit = sqrt(absN.toDouble()).toLong()
        for (i in 1..limit) {
            if (absN % i == 0L) {
                val j = absN / i
                pairs.add(Pair(j, i)) // Larger first
            }
        }
        return pairs
    }

    private fun getSignedFactorPairs(n: Long): List<Pair<Long, Long>> {
        val pairs = mutableListOf<Pair<Long, Long>>()
        if (n == 0L) {
            pairs.add(Pair(0L, 0L))
            return pairs
        }
        val absN = abs(n)
        val limit = sqrt(absN.toDouble()).toLong()
        for (i in 1..limit) {
            if (absN % i == 0L) {
                val j = absN / i
                if (n > 0) {
                    pairs.add(Pair(i, j))
                    pairs.add(Pair(-i, -j))
                    if (i != j) {
                        pairs.add(Pair(j, i))
                        pairs.add(Pair(-j, -i))
                    }
                } else {
                    pairs.add(Pair(i, -j))
                    pairs.add(Pair(-i, j))
                    if (i != j) {
                        pairs.add(Pair(j, -i))
                        pairs.add(Pair(-j, i))
                    }
                }
            }
        }
        return pairs
    }

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        return when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> {
                // Positive A, B, C with small coefficients
                val a1 = random.nextLong(2, 4)
                val a2 = 1L
                val c1 = random.nextLong(1, 6)
                val c2 = random.nextLong(1, 6)
                val a = a1 * a2
                val b = a1 * c2 + a2 * c1
                val c = c1 * c2
                buildProblemForCoefficients(a, b, c, difficultyTier)
            }
            DifficultyTier.TIER_2_HARD -> {
                // Negative B or C requiring signed search
                val a1 = random.nextLong(2, 5)
                val a2 = 1L
                val c1 = if (random.nextBoolean()) -random.nextLong(1, 6) else random.nextLong(1, 6)
                val c2 = if (c1 > 0) -random.nextLong(1, 6) else random.nextLong(1, 6)
                val a = a1 * a2
                val b = a1 * c2 + a2 * c1
                val c = c1 * c2
                buildProblemForCoefficients(a, b, c, difficultyTier)
            }
        }
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        // Generates canonical 2x^2 + 7x + 5
        return buildProblemForCoefficients(2, 7, 5, DifficultyTier.TIER_1_EASY)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val set = mutableListOf<SutraProblem>()
        val seen = mutableSetOf<String>()
        var attempts = 0

        while (set.size < count && attempts < count * 20) {
            attempts++
            val prob = generateProblem(difficultyTier)
            if (!seen.contains(prob.questionText)) {
                seen.add(prob.questionText)
                set.add(prob)
            }
        }

        while (set.size < count) {
            set.add(generateProblem(difficultyTier))
        }

        return set
    }

    private fun buildProblemForCoefficients(
        a: Long,
        b: Long,
        c: Long,
        tier: DifficultyTier
    ): SutraProblem {
        val solution = solve(a, b, c)
        val quad = solution.quadratic
        val cand = solution.confirmedCandidate ?: FirstLastCandidate(a, 1, c, 1)

        val decompositionSteps = solution.steps.map { step ->
            DecompositionStep(
                stepNumber = step.stepNumber,
                label = step.label,
                formulaDisplay = step.formulaDisplay,
                stepResult = step.stepResult,
                explanation = step.explanation
            )
        }

        // Generate distractors from failed candidate cross terms
        val failedCrossTerms = solution.attemptedCandidates
            .map { it.crossTerm }
            .filter { it != b }
            .distinct()

        val distractors = (failedCrossTerms + listOf(b + 2, b - 2, b + 4, b - 4))
            .filter { it != b }
            .distinct()
            .take(3)

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = "Factor ${quad.toFormattedString()} — Verify cross-term sum B",
            operand = b,
            correctAnswer = b,
            prefixPart = cand.a1,
            incrementedPrefix = cand.a2,
            prefixProduct = a,
            appendedSuffix = cand.toFactorString(),
            decompositionSteps = decompositionSteps,
            distractors = distractors,
            difficultyTier = tier,
            answerFormat = AnswerFormat.INTEGER,
            expectedRemainder = 0,
            expectedSecondaryAnswer = 0
        )
    }
}
