package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DifficultyTier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.math.pow
import kotlin.random.Random

class EkadhikenaPurvenaGeneratorTest {

    private lateinit var generator: EkadhikenaPurvenaGenerator

    @Before
    fun setUp() {
        // Use fixed seed for deterministic testing
        generator = EkadhikenaPurvenaGenerator(Random(12345))
    }

    @Test
    fun testCanonicalWorkedExample65Squared() {
        // Canonical worked example from docs/phase0/01_sutra_content_audit.md
        val problem = generator.generateSpecificProblem(65L)

        assertEquals("Ekadhikena Purvena", problem.sutraName)
        assertEquals(65L, problem.operand)
        assertEquals(6L, problem.prefixPart)
        assertEquals(7L, problem.incrementedPrefix)
        assertEquals(42L, problem.prefixProduct)
        assertEquals("25", problem.appendedSuffix)
        assertEquals(4225L, problem.correctAnswer)

        // Verify decomposition steps structure
        assertEquals(4, problem.decompositionSteps.size)
        assertEquals("6", problem.decompositionSteps[0].stepResult)
        assertEquals("7", problem.decompositionSteps[1].stepResult)
        assertEquals("42", problem.decompositionSteps[2].stepResult)
        assertEquals("4225", problem.decompositionSteps[3].stepResult)

        // Verify distractors
        assertFalse("Distractors must not contain correct answer 4225", problem.distractors.contains(4225L))
        assertTrue("Distractors list should contain plausible wrong options", problem.distractors.isNotEmpty())
    }

    @Test
    fun testCanonicalWorkedExample15Squared() {
        val problem = generator.generateSpecificProblem(15L)
        assertEquals(15L, problem.operand)
        assertEquals(1L, problem.prefixPart)
        assertEquals(2L, problem.incrementedPrefix)
        assertEquals(2L, problem.prefixProduct)
        assertEquals(225L, problem.correctAnswer)
    }

    @Test
    fun testCanonicalWorkedExample85Squared() {
        val problem = generator.generateSpecificProblem(85L)
        assertEquals(85L, problem.operand)
        assertEquals(8L, problem.prefixPart)
        assertEquals(9L, problem.incrementedPrefix)
        assertEquals(72L, problem.prefixProduct)
        assertEquals(7225L, problem.correctAnswer)
    }

    @Test
    fun testCanonicalWorkedExample105Squared() {
        val problem = generator.generateSpecificProblem(105L)
        assertEquals(105L, problem.operand)
        assertEquals(10L, problem.prefixPart)
        assertEquals(11L, problem.incrementedPrefix)
        assertEquals(110L, problem.prefixProduct)
        assertEquals(11025L, problem.correctAnswer)
    }

    @Test
    fun testTier1GeneratedProblemsMathematicalCorrectness() {
        // Generate 15 problems for Tier 1
        val problems = generator.generateProblemSet(15, DifficultyTier.TIER_1_EASY)
        
        for (problem in problems) {
            val operand = problem.operand
            
            // Assert operand ends in 5
            assertEquals("Operand must end in 5", 5L, operand % 10)
            assertTrue("Tier 1 operand must be 2 digits (15..95)", operand in 15..95)

            // Mathematical verification against standard arithmetic
            val expectedSquare = operand * operand
            assertEquals("Generator computed square must equal N²", expectedSquare, problem.correctAnswer)

            // Verify prefix decomposition formula: (N/10) * (N/10 + 1) * 100 + 25
            val expectedPrefix = operand / 10
            val expectedIncremented = expectedPrefix + 1
            val expectedProduct = expectedPrefix * expectedIncremented
            assertEquals(expectedPrefix, problem.prefixPart)
            assertEquals(expectedIncremented, problem.incrementedPrefix)
            assertEquals(expectedProduct, problem.prefixProduct)

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
            val operand = problem.operand

            assertEquals("Operand must end in 5", 5L, operand % 10)
            assertTrue("Tier 2 operand must be 3 digits (105..995)", operand in 105..995)

            val expectedSquare = operand * operand
            assertEquals("Generator computed square must equal N²", expectedSquare, problem.correctAnswer)

            val expectedPrefix = operand / 10
            val expectedIncremented = expectedPrefix + 1
            val expectedProduct = expectedPrefix * expectedIncremented
            assertEquals(expectedPrefix, problem.prefixPart)
            assertEquals(expectedIncremented, problem.incrementedPrefix)
            assertEquals(expectedProduct, problem.prefixProduct)

            assertFalse("Distractors must not contain correct answer", problem.distractors.contains(problem.correctAnswer))
        }
    }
}
