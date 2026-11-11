package com.ank.sutrasaga.engine

import com.ank.sutrasaga.domain.models.DifficultyTier
import com.ank.sutrasaga.domain.models.SutraProblem
import com.ank.sutrasaga.domain.models.UpaSutraId

/**
 * Generic engine contract for all Upa-Sutra problem generators.
 */
interface UpaSutraGenerator : SutraProblemGenerator {

    /**
     * The unique UpaSutraId representing this generator.
     */
    val upaSutraId: UpaSutraId

    /**
     * Generates a standard 5-problem Guided Practice sequence.
     */
    fun generateQuestPracticeSet(): List<SutraProblem> {
        return generateProblemSet(5, DifficultyTier.TIER_1_EASY)
    }

    /**
     * Generates a standard 3-problem Challenge sequence.
     */
    fun generateQuestChallengeSet(): List<SutraProblem> {
        return generateProblemSet(3, DifficultyTier.TIER_2_HARD)
    }
}
