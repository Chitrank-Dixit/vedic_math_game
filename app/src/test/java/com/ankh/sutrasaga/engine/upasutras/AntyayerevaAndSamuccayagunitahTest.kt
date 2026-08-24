package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.UpaSutraId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AntyayerevaAndSamuccayagunitahTest {

    @Test
    fun testAntyayerevaGeneratorBasicProperties() {
        val generator = AntyayerevaGenerator()
        assertEquals(UpaSutraId.ANTYAYEREVA, generator.upaSutraId)
        assertEquals("Antyayoreva", generator.sutraName)

        val easyProblem = generator.generateProblem(DifficultyTier.TIER_1_EASY)
        assertNotNull(easyProblem)
        assertEquals(0L, easyProblem.correctAnswer)
        assertTrue(easyProblem.questionText.contains("Solve for x:"))
        assertEquals(3, easyProblem.decompositionSteps.size)

        val hardProblem = generator.generateProblem(DifficultyTier.TIER_2_HARD)
        assertNotNull(hardProblem)
        assertEquals(0L, hardProblem.correctAnswer)

        val specificProblem = generator.generateSpecificProblem(4L)
        assertNotNull(specificProblem)
        assertEquals(0L, specificProblem.correctAnswer)

        val problemSet = generator.generateProblemSet(5, DifficultyTier.TIER_1_EASY)
        assertEquals(5, problemSet.size)
        problemSet.forEach {
            assertEquals(0L, it.correctAnswer)
            assertTrue(it.decompositionSteps.isNotEmpty())
        }
    }

    @Test
    fun testSamuccayagunitahGeneratorBasicProperties() {
        val generator = SamuccayagunitahGenerator()
        assertEquals(UpaSutraId.SAMUCCAYAGUNITAH, generator.upaSutraId)
        assertEquals("Samuccayagunitah", generator.sutraName)

        val easyProblem = generator.generateProblem(DifficultyTier.TIER_1_EASY)
        assertNotNull(easyProblem)
        assertTrue(easyProblem.correctAnswer > 0)
        assertTrue(easyProblem.questionText.contains("Find the Sum of Coefficients"))
        assertEquals(3, easyProblem.decompositionSteps.size)

        val hardProblem = generator.generateProblem(DifficultyTier.TIER_2_HARD)
        assertNotNull(hardProblem)
        assertTrue(hardProblem.correctAnswer > 0)

        val specificProblem = generator.generateSpecificProblem(2L)
        assertNotNull(specificProblem)
        // (2x + 3)(1x + 4) evaluated at x=1 gives (2+3)(1+4) = 5 * 5 = 25
        assertEquals(25L, specificProblem.correctAnswer)

        val problemSet = generator.generateProblemSet(5, DifficultyTier.TIER_1_EASY)
        assertEquals(5, problemSet.size)
        problemSet.forEach {
            assertTrue(it.correctAnswer > 0)
            assertTrue(it.decompositionSteps.isNotEmpty())
        }
    }

    @Test
    fun testAll13UpaSutrasHaveGeneratorsInViewModel() {
        val allIds = UpaSutraId.values()
        assertEquals(13, allIds.size)
    }
}
