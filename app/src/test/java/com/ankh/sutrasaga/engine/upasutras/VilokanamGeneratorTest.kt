package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.ObservationPatternId
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.domain.models.VilokanamClassification
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VilokanamGeneratorTest {

    private lateinit var generator: VilokanamGenerator

    @Before
    fun setUp() {
        generator = VilokanamGenerator()
    }

    @Test
    fun testUpaSutraMetadata() {
        assertEquals(UpaSutraId.VILOKANAM, generator.upaSutraId)
        assertEquals("Vilokanam", generator.sutraName)
    }

    @Test
    fun testSeed1_EndsInFiveSquare_85Squared() {
        val sol = generator.classifyAndSolve(85L, 85L, true)
        assertEquals(ObservationPatternId.ENDS_IN_FIVE_SQUARE, sol.correctPattern)
        assertEquals(7225L, sol.numericalResult)
        assertEquals(VilokanamClassification.PATTERN_RECOGNIZED, sol.classification)
        assertTrue(sol.explanation.contains("25"))
    }

    @Test
    fun testSeed2_NearBaseSquare_97Squared() {
        val sol = generator.classifyAndSolve(97L, 97L, true)
        assertEquals(ObservationPatternId.NEAR_BASE_SQUARE, sol.correctPattern)
        assertEquals(9409L, sol.numericalResult)
        assertEquals(VilokanamClassification.PATTERN_RECOGNIZED, sol.classification)
        assertTrue(sol.explanation.contains("deficiency"))
    }

    @Test
    fun testSeed3_SameTensUnitsSumTen_43Times47() {
        val sol = generator.classifyAndSolve(43L, 47L, false)
        assertEquals(ObservationPatternId.SAME_TENS_UNITS_SUM_TEN, sol.correctPattern)
        assertEquals(2021L, sol.numericalResult)
        assertEquals(VilokanamClassification.PATTERN_RECOGNIZED, sol.classification)
        assertTrue(sol.explanation.contains("one more"))
    }

    @Test
    fun testSeed4_SymmetricProduct_58Times62() {
        val sol = generator.classifyAndSolve(58L, 62L, false)
        assertEquals(ObservationPatternId.SYMMETRIC_PRODUCT, sol.correctPattern)
        assertEquals(3596L, sol.numericalResult)
        assertEquals(VilokanamClassification.PATTERN_RECOGNIZED, sol.classification)
        assertTrue(sol.explanation.contains("Squares"))
    }

    @Test
    fun testSeed5_NegativeControl_46Times53() {
        val sol = generator.classifyAndSolve(46L, 53L, false)
        assertEquals(ObservationPatternId.NONE_OF_THE_ABOVE, sol.correctPattern)
        assertEquals(2438L, sol.numericalResult)
        assertEquals(VilokanamClassification.NONE_APPLIES, sol.classification)
    }

    @Test
    fun testTenHandVerifiedEndsInFiveSquares() {
        val testNums = listOf(15L, 25L, 35L, 45L, 55L, 65L, 75L, 85L, 95L, 105L)
        for (num in testNums) {
            val sol = generator.classifyAndSolve(num, num, true)
            assertEquals(ObservationPatternId.ENDS_IN_FIVE_SQUARE, sol.correctPattern)
            assertEquals(num * num, sol.numericalResult)
        }
    }

    @Test
    fun testTenHandVerifiedNearBaseSquares() {
        val testNums = listOf(88L, 89L, 91L, 92L, 93L, 94L, 96L, 97L, 98L, 99L)
        for (num in testNums) {
            val sol = generator.classifyAndSolve(num, num, true)
            assertEquals(ObservationPatternId.NEAR_BASE_SQUARE, sol.correctPattern)
            assertEquals(num * num, sol.numericalResult)
        }
    }

    @Test
    fun testTenHandVerifiedSameTensUnitsSumTen() {
        val pairs = listOf(
            Pair(23L, 27L), Pair(31L, 39L), Pair(42L, 48L), Pair(54L, 56L), Pair(61L, 69L),
            Pair(72L, 78L), Pair(83L, 87L), Pair(91L, 99L), Pair(34L, 36L), Pair(63L, 67L)
        )
        for ((a, b) in pairs) {
            val sol = generator.classifyAndSolve(a, b, false)
            assertEquals(ObservationPatternId.SAME_TENS_UNITS_SUM_TEN, sol.correctPattern)
            assertEquals(a * b, sol.numericalResult)
        }
    }

    @Test
    fun testTenHandVerifiedSymmetricProducts() {
        val pairs = listOf(
            Pair(18L, 22L), Pair(28L, 32L), Pair(37L, 43L), Pair(49L, 51L), Pair(57L, 63L),
            Pair(68L, 72L), Pair(77L, 83L), Pair(89L, 91L), Pair(38L, 42L), Pair(47L, 53L)
        )
        for ((a, b) in pairs) {
            val sol = generator.classifyAndSolve(a, b, false)
            assertEquals(ObservationPatternId.SYMMETRIC_PRODUCT, sol.correctPattern)
            assertEquals(a * b, sol.numericalResult)
        }
    }

    @Test
    fun testTwentyNoneOfTheAboveControls() {
        val pairs = listOf(
            Pair(23L, 45L), Pair(31L, 52L), Pair(41L, 63L), Pair(52L, 71L), Pair(63L, 82L),
            Pair(14L, 28L), Pair(26L, 39L), Pair(38L, 53L), Pair(47L, 68L), Pair(59L, 74L),
            Pair(33L, 48L), Pair(44L, 57L), Pair(55L, 68L), Pair(66L, 79L), Pair(77L, 88L),
            Pair(19L, 35L), Pair(28L, 43L), Pair(37L, 54L), Pair(46L, 61L), Pair(58L, 73L)
        )
        for ((a, b) in pairs) {
            val sol = generator.classifyAndSolve(a, b, false)
            assertEquals("Expected NONE_OF_THE_ABOVE for $a × $b", ObservationPatternId.NONE_OF_THE_ABOVE, sol.correctPattern)
            assertEquals(a * b, sol.numericalResult)
        }
    }

    @Test
    fun testNearMissCases() {
        // Same tens but units do NOT sum to 10 (e.g. 43 × 46)
        val sol1 = generator.classifyAndSolve(43L, 46L, false)
        assertEquals(ObservationPatternId.NONE_OF_THE_ABOVE, sol1.correctPattern)

        // Units sum to 10 but tens differ (e.g. 33 × 47)
        val sol2 = generator.classifyAndSolve(33L, 47L, false)
        assertEquals(ObservationPatternId.NONE_OF_THE_ABOVE, sol2.correctPattern)

        // Ends in 5 but not square and not symmetric around round base (e.g. 25 × 45)
        val sol3 = generator.classifyAndSolve(25L, 45L, false)
        assertEquals(ObservationPatternId.NONE_OF_THE_ABOVE, sol3.correctPattern)
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier1() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_1_EASY)
            assertTrue(problem.correctAnswer > 0L)
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
            assertNotNull(problem.questionText)
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier2() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_2_HARD)
            assertTrue(problem.correctAnswer > 0L)
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier3() {
        repeat(50) {
            val problem = generator.generateTier3Problem()
            assertTrue(problem.correctAnswer > 0L)
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
        }
    }

    @Test
    fun testQuestPracticeAndChallengeSets() {
        val practiceSet = generator.generateQuestPracticeSet()
        assertEquals(5, practiceSet.size)

        val challengeSet = generator.generateQuestChallengeSet()
        assertEquals(3, challengeSet.size)
    }
}
