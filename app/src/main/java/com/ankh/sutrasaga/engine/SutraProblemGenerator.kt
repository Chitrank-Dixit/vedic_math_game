package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SutraProblem

interface SutraProblemGenerator {
    val sutraName: String

    /**
     * Generates a single Sutra problem of the specified difficulty tier.
     */
    fun generateProblem(difficultyTier: DifficultyTier): SutraProblem

    /**
     * Generates a specific problem for a given operand (e.g. 65 for 65²).
     */
    fun generateSpecificProblem(operand: Long): SutraProblem

    /**
     * Generates a list of unique problems for practice or boss battles.
     */
    fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem>
}
