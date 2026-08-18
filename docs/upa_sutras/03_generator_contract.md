# Generic Upa-Sutra Generator Contract

**Document Status:** Generator Contract Specification / Phase U1  
**Scope:** Reusable Engine & Generator Interface for all 13 Upa-Sutras  

---

## 1. Engine Interface Definition

All Upa-Sutra problem generators in *Ankh: The Sutra Saga* implement the common `UpaSutraGenerator` interface extending the core `SutraProblemGenerator`:

```kotlin
package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.models.UpaSutraId

interface UpaSutraGenerator : SutraProblemGenerator {
    /**
     * Unique stable identifier matching the canonical registry.
     */
    val upaSutraId: UpaSutraId

    /**
     * Generates a 5-problem sequence tailored for the Guided Practice segment of the quest.
     */
    fun generateQuestPracticeSet(): List<SutraProblem> =
        generateProblemSet(5, DifficultyTier.TIER_1_EASY)

    /**
     * Generates a 3-problem sequence tailored for the Challenge segment of the quest.
     */
    fun generateQuestChallengeSet(): List<SutraProblem> =
        generateProblemSet(3, DifficultyTier.TIER_2_HARD)
}
```

---

## 2. Quest Scaffolding & Scoring Rules

1. **Guided Practice Segment**:
   - Total problems: 5.
   - Difficulty: Easy / Guided (with hints & step-by-step reveals available).
   - Passing threshold: 3 out of 5 correct awards the `PRACTICED` state.
2. **Challenge Segment**:
   - Total problems: 3.
   - Difficulty: Hard / Timed.
   - Passing threshold: At least 2 out of 3 correct awards the `MASTERED` state and unlocks the Master badge in the Treasury and Codex!
