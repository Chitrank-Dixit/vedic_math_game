package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DifficultyTier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class EkanyunenaGeneratorTest {

    private lateinit var generator: EkanyunenaGenerator

    @Before
    fun setUp() {
        // Use fixed seed for deterministic testing
        generator = EkanyunenaGenerator(Random(12345))
    }

    @Test
    fun testCanonicalWorkedExample743Times999() {
        // Canonical worked example from docs/phase0/01_sutra_content_audit.md
        val problem = generator.generateSpecificProblem(743L)

        assertEquals("Ekanyunena Purvena", problem.sutraName)
        assertEquals(743L, problem.operand)
        assertEquals(742257L, problem.correctAnswer)

        // Verify decomposition steps structure
        assertEquals(3, problem.decompositionSteps.size)
        assertEquals("742", problem.decompositionSteps[0].stepResult)
        assertEquals("257", problem.decompositionSteps[1].stepResult)
        assertEquals("742257", problem.decompositionSteps[2].stepResult)

        // Verify distractors
        assertFalse("Distractors must not contain correct answer 742257", problem.distractors.contains(742257L))
        assertTrue("Distractors list should contain plausible wrong options", problem.distractors.isNotEmpty())
    }

    @Test
    fun testCanonicalWorkedExample46Times99() {
        val problem = generator.generateSpecificProblem(46L)
        assertEquals(46L, problem.operand)
        assertEquals(4554L, problem.correctAnswer)
    }

    @Test
    fun testTier1GeneratedProblemsMathematicalCorrectness() {
        // Generate 15 problems for Tier 1
        val problems = generator.generateProblemSet(15, DifficultyTier.TIER_1_EASY)

        for (problem in problems) {
            val multiplicand = problem.operand
            val nines = 99L

            assertTrue("Tier 1 multiplicand must be 2 digits (11..99)", multiplicand in 11..99)

            val expectedProduct = multiplicand * nines
            assertEquals("Generator computed result must equal N * 99", expectedProduct, problem.correctAnswer)

            // Distractor sanity check
            assertFalse("Distractors must not contain correct answer", problem.distractors.contains(problem.correctAnswer))
            for (d in problem.distractors) {
                assertTrue("Distractors must be positive", d > 0)
            }
        }
    }

    @Test
    fun testTier2GeneratedProblemsMathematicalCorrectness() {
        // Generate 15 problems for Tier 2
        val problems = generator.generateProblemSet(15, DifficultyTier.TIER_2_HARD)

        for (problem in problems) {
            val multiplicand = problem.operand
            val nines = 999L

            assertTrue("Tier 2 multiplicand must be 3 digits (101..999)", multiplicand in 101..999)

            val expectedProduct = multiplicand * nines
            assertEquals("Generator computed result must equal N * 999", expectedProduct, problem.correctAnswer)

            assertFalse("Distractors must not contain correct answer", problem.distractors.contains(problem.correctAnswer))
        }
    }
}
