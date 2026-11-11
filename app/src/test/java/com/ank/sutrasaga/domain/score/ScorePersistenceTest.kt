package com.ank.sutrasaga.domain.score

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ank.sutrasaga.data.db.AppDatabase
import com.ank.sutrasaga.data.db.UserProgressDao
import com.ank.sutrasaga.data.repository.GameRepository
import com.ank.sutrasaga.ui.viewmodel.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ScorePersistenceTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: UserProgressDao
    private lateinit var repository: GameRepository
    private lateinit var viewModel: GameViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries()
            .setQueryExecutor { it.run() }
            .setTransactionExecutor { it.run() }
            .build()

        dao = database.userProgressDao()
        repository = GameRepository(userDao = dao)
        viewModel = GameViewModel()
        viewModel.initRepository(repository)
    }

    @After
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }

    @Test
    fun testActualEarnedScorePersistedToRoomNot100() = runTest {
        viewModel.selectSutra(1)
        assertEquals(0, viewModel.uiState.value.score)

        // Simulate user earning a score of 72
        viewModel.recordSolverStepAnswer(isCorrect = false) // 1 mistake: penalty 15
        viewModel.recordSolverStepAnswer(isCorrect = false) // 2 mistakes: penalty 30 -> 70 pts
        // Or directly set score via steps or solver
        // Let's test completing with actual score 72
        viewModel.completeWorld(worldId = 1, score = 72)
        testDispatcher.scheduler.advanceUntilIdle()

        // In-memory GameUiState reflects 72
        assertEquals(72, viewModel.uiState.value.world1BestScore)

        // Room DB reflects 72, NOT 100!
        val entity = dao.getProgressForWorld(1)
        assertNotNull(entity)
        assertEquals(72, entity?.bestScore)
        assertTrue(entity?.isCompleted == true)
    }

    @Test
    fun testNonPerfectScorePersistence() = runTest {
        viewModel.selectSutra(3)
        viewModel.completeWorld(worldId = 3, score = 72)
        testDispatcher.scheduler.advanceUntilIdle()

        val entity = dao.getProgressForWorld(3)
        assertEquals(72, entity?.bestScore)
        assertEquals(72, viewModel.uiState.value.world3BestScore)
    }

    @Test
    fun testPerfectScorePersistence() = runTest {
        viewModel.selectSutra(5)
        viewModel.completeWorld(worldId = 5, score = 1000)
        testDispatcher.scheduler.advanceUntilIdle()

        val entity = dao.getProgressForWorld(5)
        assertEquals(1000, entity?.bestScore)
        assertEquals(1000, viewModel.uiState.value.world5BestScore)
    }

    @Test
    fun testLowScorePersistence() = runTest {
        viewModel.selectSutra(4)
        viewModel.completeWorld(worldId = 4, score = 40)
        testDispatcher.scheduler.advanceUntilIdle()

        val entity = dao.getProgressForWorld(4)
        assertEquals(40, entity?.bestScore)
        assertEquals(40, viewModel.uiState.value.world4BestScore)
    }

    @Test
    fun testZeroScorePersistenceNotSilentlyConvertedTo100() = runTest {
        viewModel.selectSutra(2)
        viewModel.completeWorld(worldId = 2, score = 0)
        testDispatcher.scheduler.advanceUntilIdle()

        val entity = dao.getProgressForWorld(2)
        assertEquals("Zero score must be persisted as 0 and not silently converted to 100", 0, entity?.bestScore)
        assertEquals(0, viewModel.uiState.value.world2BestScore)
    }

    @Test
    fun testNegativeScoreCoercedToZeroSafely() = runTest {
        viewModel.selectSutra(6)
        viewModel.completeWorld(worldId = 6, score = -50)
        testDispatcher.scheduler.advanceUntilIdle()

        val entity = dao.getProgressForWorld(6)
        assertEquals(0, entity?.bestScore)
        assertEquals(0, viewModel.uiState.value.world6BestScore)
    }

    @Test
    fun testScorePersistenceSurvivesReload() = runTest {
        // Complete world 1 with score 72
        repository.saveWorldCompletion(worldId = 1, score = 72)

        // Create new repository instance simulating process restart / reload
        val newRepository = GameRepository(userDao = dao)
        val loadedEntity = newRepository.getProgressForWorld(1)

        assertNotNull(loadedEntity)
        assertEquals(72, loadedEntity?.bestScore)
        assertEquals(true, loadedEntity?.isCompleted)
    }

    @Test
    fun testBestScoreBehaviorAcrossMultipleAttempts() = runTest {
        // Attempt 1: score = 70 -> best = 70
        viewModel.completeWorld(worldId = 1, score = 70)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(70, dao.getProgressForWorld(1)?.bestScore)
        assertEquals(70, viewModel.uiState.value.world1BestScore)

        // Attempt 2: score = 85 -> best = 85
        viewModel.completeWorld(worldId = 1, score = 85)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(85, dao.getProgressForWorld(1)?.bestScore)
        assertEquals(85, viewModel.uiState.value.world1BestScore)

        // Attempt 3: score = 60 -> best must remain 85 (not 60, not 215)
        viewModel.completeWorld(worldId = 1, score = 60)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Best score must remain 85 after lower attempt", 85, dao.getProgressForWorld(1)?.bestScore)
        assertEquals(85, viewModel.uiState.value.world1BestScore)
    }

    @Test
    fun testMultipleRapidCompletionsDoNotInflateScore() = runTest {
        // Rapid repeated taps with score 72
        viewModel.completeWorld(worldId = 2, score = 72)
        viewModel.completeWorld(worldId = 2, score = 72)
        viewModel.completeWorld(worldId = 2, score = 72)
        testDispatcher.scheduler.advanceUntilIdle()

        val entity = dao.getProgressForWorld(2)
        assertEquals(72, entity?.bestScore)
        assertEquals(72, viewModel.uiState.value.world2BestScore)

        val all = dao.getAllProgress().first()
        val countForWorld2 = all.count { it.worldId == 2 }
        assertEquals("Must only have 1 record for world 2", 1, countForWorld2)
    }

    @Test
    fun testScoreResetsBetweenLessons() = runTest {
        // Lesson 1: starts at 0, earns 80, completes
        viewModel.selectSutra(1)
        assertEquals(0, viewModel.uiState.value.score)
        viewModel.recordSolverStepAnswer(isCorrect = false) // hint/mistake 1 (-15)
        viewModel.recordSolverStepAnswer(isCorrect = true)  // 85 points
        assertEquals(85, viewModel.uiState.value.score)
        viewModel.completeLesson(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Lesson 2: start new lesson
        viewModel.selectSutra(2)
        assertEquals("New lesson must reset score to 0 and not inherit previous score", 0, viewModel.uiState.value.score)
    }

    @Test
    fun testSolverStepScoringWorkflow() = runTest {
        viewModel.selectSutra(5)
        assertEquals(0, viewModel.uiState.value.score)

        // Step 1: correct on first attempt (+100)
        viewModel.recordSolverStepAnswer(isCorrect = true)
        assertEquals(100, viewModel.uiState.value.score)

        // Step 2: 1 wrong attempt, then correct (+85)
        viewModel.recordSolverStepAnswer(isCorrect = false)
        viewModel.recordSolverStepAnswer(isCorrect = true)
        assertEquals(185, viewModel.uiState.value.score)

        // Step 3: 2 wrong attempts, then correct (+70)
        viewModel.recordSolverStepAnswer(isCorrect = false)
        viewModel.recordSolverStepAnswer(isCorrect = false)
        viewModel.recordSolverStepAnswer(isCorrect = true)
        assertEquals(255, viewModel.uiState.value.score)

        // Complete lesson using authoritative score
        viewModel.completeLesson(5)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(255, viewModel.uiState.value.world5BestScore)
        assertEquals(255, dao.getProgressForWorld(5)?.bestScore)
    }

    @Test
    fun testPracticeToBossBattleScoreAccumulation() = runTest {
        viewModel.selectWorld(1)
        viewModel.startPractice()
        assertEquals(0, viewModel.uiState.value.score)

        // 5 practice problems: answer all correct
        for (i in 0 until 5) {
            val problem = viewModel.uiState.value.currentProblem!!
            viewModel.clearDigit()
            for (c in problem.correctAnswer.toString()) {
                viewModel.appendDigit(c)
            }
            viewModel.submitAnswer()
            viewModel.nextProblem()
        }

        // Entering Boss Battle preserves practice score
        assertEquals(500, viewModel.uiState.value.score)

        // 5 boss battle problems: answer all correct
        for (i in 0 until 5) {
            val problem = viewModel.uiState.value.currentProblem!!
            viewModel.clearDigit()
            for (c in problem.correctAnswer.toString()) {
                viewModel.appendDigit(c)
            }
            viewModel.submitAnswer()
            viewModel.nextProblem()
        }

        testDispatcher.scheduler.advanceUntilIdle()

        // 10/10 correct = 1000 points maximum persisted
        assertEquals(1000, viewModel.uiState.value.world1BestScore)
        assertEquals(1000, dao.getProgressForWorld(1)?.bestScore)
    }

    @Test
    fun testPartialRunSevenOfTenCorrect() = runTest {
        viewModel.selectWorld(2)
        viewModel.startPractice()
        assertEquals(0, viewModel.uiState.value.score)

        // 5 practice: 5 correct (500 pts)
        for (i in 0 until 5) {
            val problem = viewModel.uiState.value.currentProblem!!
            viewModel.clearDigit()
            for (c in problem.correctAnswer.toString()) {
                viewModel.appendDigit(c)
            }
            viewModel.submitAnswer()
            viewModel.nextProblem()
        }

        // 5 boss: 2 correct, 3 wrong (200 pts) -> Total 7/10 correct = 700 pts
        for (i in 0 until 5) {
            val problem = viewModel.uiState.value.currentProblem!!
            viewModel.clearDigit()
            if (i < 2) {
                // Correct
                for (c in problem.correctAnswer.toString()) {
                    viewModel.appendDigit(c)
                }
            } else {
                // Wrong: problem.correctAnswer + 99
                val wrong = (problem.correctAnswer + 99).toString()
                for (c in wrong) {
                    viewModel.appendDigit(c)
                }
            }
            viewModel.submitAnswer()
            viewModel.nextProblem()
        }

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("7/10 correct must persist 700 pts", 700, viewModel.uiState.value.world2BestScore)
        assertEquals("7/10 correct in Room must be 700 pts", 700, dao.getProgressForWorld(2)?.bestScore)
    }

    @Test
    fun testPoorRunTwoOfTenCorrect() = runTest {
        viewModel.selectWorld(3)
        viewModel.startPractice()
        assertEquals(0, viewModel.uiState.value.score)

        // 5 practice: 2 correct, 3 wrong (200 pts)
        for (i in 0 until 5) {
            val problem = viewModel.uiState.value.currentProblem!!
            viewModel.clearDigit()
            if (i < 2) {
                for (c in problem.correctAnswer.toString()) {
                    viewModel.appendDigit(c)
                }
            } else {
                val wrong = (problem.correctAnswer + 99).toString()
                for (c in wrong) {
                    viewModel.appendDigit(c)
                }
            }
            viewModel.submitAnswer()
            viewModel.nextProblem()
        }

        // 5 boss: 0 correct (all wrong)
        for (i in 0 until 5) {
            val problem = viewModel.uiState.value.currentProblem!!
            viewModel.clearDigit()
            val wrong = (problem.correctAnswer + 99).toString()
            for (c in wrong) {
                viewModel.appendDigit(c)
            }
            viewModel.submitAnswer()
            viewModel.nextProblem()
        }

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("2/10 correct must persist 200 pts", 200, viewModel.uiState.value.world3BestScore)
        assertEquals("2/10 correct in Room must be 200 pts", 200, dao.getProgressForWorld(3)?.bestScore)
    }

    @Test
    fun testZeroRunZeroOfTenCorrect() = runTest {
        viewModel.selectWorld(4)
        viewModel.startPractice()
        assertEquals(0, viewModel.uiState.value.score)

        // 5 practice: all wrong (0 pts)
        for (i in 0 until 5) {
            val problem = viewModel.uiState.value.currentProblem!!
            viewModel.clearDigit()
            val wrong = (problem.correctAnswer + 99).toString()
            for (c in wrong) {
                viewModel.appendDigit(c)
            }
            viewModel.submitAnswer()
            viewModel.nextProblem()
        }

        // 5 boss: all wrong (0 pts)
        for (i in 0 until 5) {
            val problem = viewModel.uiState.value.currentProblem!!
            viewModel.clearDigit()
            val wrong = (problem.correctAnswer + 99).toString()
            for (c in wrong) {
                viewModel.appendDigit(c)
            }
            viewModel.submitAnswer()
            viewModel.nextProblem()
        }

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("0/10 correct must persist 0 pts", 0, viewModel.uiState.value.world4BestScore)
        assertEquals("0/10 correct in Room must be 0 pts", 0, dao.getProgressForWorld(4)?.bestScore)
    }
}
