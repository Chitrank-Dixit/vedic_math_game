package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DifficultyTier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class NikhilamGeneratorTest {

    private lateinit var generator: NikhilamGenerator

    @Before
    fun setUp() {
        // Use fixed seed for deterministic testing
        generator = NikhilamGenerator(Random(12345))
    }

    @Test
    fun testCanonicalWorkedExample10000Minus3468() {
        // Canonical worked example from docs/phase0/01_sutra_content_audit.md
        val problem = generator.generateSpecificProblem(3468L)

        assertEquals("Nikhilam Navatashcaramam Dashatah", problem.sutraName)
        assertEquals(3468L, problem.operand)
        assertEquals(6532L, problem.correctAnswer)

        // Verify decomposition steps structure
        assertEquals(4, problem.decompositionSteps.size)
        assertEquals("Base = 10000", problem.decompositionSteps[0].stepResult)
        assertEquals("653", problem.decompositionSteps[1].stepResult)
        assertEquals("2", problem.decompositionSteps[2].stepResult)
        assertEquals("6532", problem.decompositionSteps[3].stepResult)

        // Verify distractors
        assertFalse("Distractors must not contain correct answer 6532", problem.distractors.contains(6532L))
        assertTrue("Distractors list should contain plausible wrong options", problem.distractors.isNotEmpty())
    }

    @Test
    fun testCanonicalWorkedExample100Minus37() {
        val problem = generator.generateSpecificProblem(37L)
        assertEquals(37L, problem.operand)
        assertEquals(63L, problem.correctAnswer)
    }

    @Test
    fun testCanonicalWorkedExample1000Minus346() {
        val problem = generator.generateSpecificProblem(346L)
        assertEquals(346L, problem.operand)
        assertEquals(654L, problem.correctAnswer)
    }

    @Test
    fun testTier1GeneratedProblemsMathematicalCorrectness() {
        // Generate 15 problems for Tier 1
        val problems = generator.generateProblemSet(15, DifficultyTier.TIER_1_EASY)

        for (problem in problems) {
            val subtrahend = problem.operand
            val base = problem.prefixPart

            assertTrue("Base must be 100 or 1000", base == 100L || base == 1000L)
            assertTrue("Subtrahend must be less than base", subtrahend < base)

            // Mathematical verification against standard arithmetic
            val expectedResult = base - subtrahend
            assertEquals("Generator computed result must equal Base - Subtrahend", expectedResult, problem.correctAnswer)

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
            val subtrahend = problem.operand
            val base = problem.prefixPart

            assertEquals("Tier 2 base must be 10000", 10000L, base)
            assertTrue("Tier 2 subtrahend must be 4 digits", subtrahend in 1000..9999)

            val expectedResult = base - subtrahend
            assertEquals("Generator computed result must equal Base - Subtrahend", expectedResult, problem.correctAnswer)

            assertFalse("Distractors must not contain correct answer", problem.distractors.contains(problem.correctAnswer))
        }
    }
}
