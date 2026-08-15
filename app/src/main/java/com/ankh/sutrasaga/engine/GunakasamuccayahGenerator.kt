package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.FactorPair
import com.ankh.sutrasaga.domain.models.FactorizationCandidate
import com.ankh.sutrasaga.domain.models.GunakasamuccayahClassification
import com.ankh.sutrasaga.domain.models.GunakasamuccayahOperationType
import com.ankh.sutrasaga.domain.models.GunakasamuccayahProblem
import com.ankh.sutrasaga.domain.models.GunakasamuccayahSolution
import com.ankh.sutrasaga.domain.models.GunakasamuccayahStep
import com.ankh.sutrasaga.domain.models.MonicQuadratic
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

class GunakasamuccayahGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Gunakasamuccayah"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val gProblem = generateMonicProblem(difficultyTier)
        return convertToSutraProblem(gProblem)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val gProblem = buildValidProblem(2, 5, DifficultyTier.TIER_1_EASY)
        return convertToSutraProblem(gProblem)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val list = mutableListOf<SutraProblem>()
        repeat(count) {
            list.add(generateProblem(difficultyTier))
        }
        return list
    }

    fun generateMonicProblem(tier: DifficultyTier): GunakasamuccayahProblem {
        return when (tier) {
            DifficultyTier.TIER_1_EASY -> {
                val p = random.nextLong(1, 8)
                val q = random.nextLong(1, 8)
                buildValidProblem(p, q, tier)
            }
            DifficultyTier.TIER_2_HARD -> {
                val isUnsupported = random.nextInt(10) == 0
                if (isUnsupported) {
                    // e.g. x^2 + x + 1 = 0
                    buildUnsupportedProblem(1, 1, tier)
                } else {
                    val p = random.nextLong(-8, 9)
                    val q = random.nextLong(-8, 9)
                    buildValidProblem(p, q, tier)
                }
            }
        }
    }

    fun buildValidProblem(p: Long, q: Long, tier: DifficultyTier): GunakasamuccayahProblem {
        val b = p + q
        val c = p * q
        val sol = solveQuadratic(b, c)
        val correctRoot = sol.roots.firstOrNull() ?: 0L
        val distractors = generateDistractors(correctRoot, p, q)

        return GunakasamuccayahProblem(
            id = UUID.randomUUID().toString(),
            quadratic = MonicQuadratic(b, c),
            solution = sol,
            difficultyTier = tier,
            distractors = distractors
        )
    }

    fun buildUnsupportedProblem(b: Long, c: Long, tier: DifficultyTier): GunakasamuccayahProblem {
        val sol = solveQuadratic(b, c)
        val distractors = listOf(0L, 1L, -1L, 2L)

        return GunakasamuccayahProblem(
            id = UUID.randomUUID().toString(),
            quadratic = MonicQuadratic(b, c),
            solution = sol,
            difficultyTier = tier,
            distractors = distractors
        )
    }

    fun solveQuadratic(b: Long, c: Long): GunakasamuccayahSolution {
        val quad = MonicQuadratic(b, c)
        val candidates = mutableListOf<FactorizationCandidate>()
        var matchingPair: FactorPair? = null

        val factorPairs = getFactorPairs(b, c)
        for (pair in factorPairs) {
            val prodMatch = (pair.product() == c)
            val sumMatch = (pair.sum() == b)

            candidates.add(
                FactorizationCandidate(
                    pair = pair,
                    productMatches = prodMatch,
                    sumMatches = sumMatch
                )
            )

            if (prodMatch && sumMatch && matchingPair == null) {
                matchingPair = pair
            }
        }

        val classif = when {
            matchingPair != null && matchingPair.p == matchingPair.q -> GunakasamuccayahClassification.REPEATED_FACTOR
            matchingPair != null -> GunakasamuccayahClassification.FACTORED
            else -> GunakasamuccayahClassification.NO_INTEGER_FACTOR_PAIR
        }

        val roots = if (matchingPair != null) {
            listOf(-matchingPair.p, -matchingPair.q)
        } else {
            emptyList()
        }

        val steps = mutableListOf<GunakasamuccayahStep>()
        steps.add(GunakasamuccayahStep(GunakasamuccayahOperationType.LIST_PAIRS, "List integer factor pairs of C = $c."))
        if (matchingPair != null) {
            steps.add(GunakasamuccayahStep(GunakasamuccayahOperationType.CHECK_PRODUCT, "Pair (${matchingPair.p}, ${matchingPair.q}) product: ${matchingPair.p} × ${matchingPair.q} = $c."))
            steps.add(GunakasamuccayahStep(GunakasamuccayahOperationType.CHECK_SUM, "Pair (${matchingPair.p}, ${matchingPair.q}) sum: ${matchingPair.p} + ${matchingPair.q} = $b."))
            steps.add(GunakasamuccayahStep(GunakasamuccayahOperationType.FORM_FACTORS, "Form factors: (x + ${matchingPair.p})(x + ${matchingPair.q}) = 0."))
            steps.add(GunakasamuccayahStep(GunakasamuccayahOperationType.SOLVE_ROOTS, "Roots: x = ${-matchingPair.p}, x = ${-matchingPair.q}."))
            steps.add(GunakasamuccayahStep(GunakasamuccayahOperationType.VERIFY, "Verify in ${quad.toFormattedString()}: 0 = 0."))
        } else {
            steps.add(GunakasamuccayahStep(GunakasamuccayahOperationType.CHECK_SUM, "No integer factor pair of $c sums to $b."))
        }

        return GunakasamuccayahSolution(
            quadratic = quad,
            matchingPair = matchingPair,
            candidates = candidates,
            roots = roots,
            classification = classif,
            steps = steps
        )
    }

    private fun getFactorPairs(b: Long, c: Long): List<FactorPair> {
        val pairs = mutableListOf<FactorPair>()
        if (c == 0L) {
            pairs.add(FactorPair(0, b))
            pairs.add(FactorPair(b, 0))
            return pairs
        }

        val absC = abs(c)
        val limit = sqrt(absC.toDouble()).toLong()
        for (i in 1..limit) {
            if (absC % i == 0L) {
                val j = absC / i
                if (c > 0) {
                    pairs.add(FactorPair(i, j))
                    pairs.add(FactorPair(-i, -j))
                    if (i != j) {
                        pairs.add(FactorPair(j, i))
                        pairs.add(FactorPair(-j, -i))
                    }
                } else {
                    pairs.add(FactorPair(i, -j))
                    pairs.add(FactorPair(-i, j))
                    if (i != j) {
                        pairs.add(FactorPair(j, -i))
                        pairs.add(FactorPair(-j, i))
                    }
                }
            }
        }
        return pairs
    }

    private fun convertToSutraProblem(g: GunakasamuccayahProblem): SutraProblem {
        val quadStr = g.quadratic.toFormattedString()
        val primaryRoot = g.solution.roots.firstOrNull() ?: 0L
        val secondaryRoot = g.solution.roots.lastOrNull() ?: 0L

        val decSteps = g.solution.steps.mapIndexed { idx, s ->
            DecompositionStep(
                stepNumber = idx + 1,
                label = s.type.name,
                formulaDisplay = s.description,
                stepResult = s.type.name,
                explanation = s.description
            )
        }

        return SutraProblem(
            id = g.id,
            sutraName = sutraName,
            questionText = "Factorize and solve $quadStr (Gunakasamuccayah)",
            operand = primaryRoot,
            correctAnswer = primaryRoot,
            expectedSecondaryAnswer = secondaryRoot,
            prefixPart = g.quadratic.b,
            incrementedPrefix = g.quadratic.c,
            prefixProduct = g.solution.matchingPair?.p ?: 0L,
            appendedSuffix = g.solution.matchingPair?.q?.toString() ?: "",
            decompositionSteps = decSteps,
            distractors = g.distractors,
            difficultyTier = g.difficultyTier,
            answerFormat = AnswerFormat.ORDERED_PAIR
        )
    }

    private fun generateDistractors(correctRoot: Long, p: Long, q: Long): List<Long> {
        val set = mutableSetOf<Long>()
        // Common mistake 1: reporting factors (p, q) as roots instead of (-p, -q)
        set.add(p)
        set.add(q)
        // Common mistake 2: sign flip
        set.add(-correctRoot)
        set.add(correctRoot + 1L)

        var delta = 1L
        while (set.size < 4) {
            val candidate = correctRoot + delta * 2L
            if (candidate != correctRoot) {
                set.add(candidate)
            }
            delta = if (delta > 0) -delta else (-delta + 2L)
        }

        return set.take(4)
    }
}
