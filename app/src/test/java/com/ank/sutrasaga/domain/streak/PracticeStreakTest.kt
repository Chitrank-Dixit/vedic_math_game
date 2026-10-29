package com.ank.sutrasaga.domain.streak

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ank.sutrasaga.data.db.AppDatabase
import com.ank.sutrasaga.data.repository.GameRepository
import com.ank.sutrasaga.domain.models.PracticeStreak
import com.ank.sutrasaga.ui.screens.CanonicalSutraModules
import com.ank.sutrasaga.ui.viewmodel.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar
import java.util.TimeZone

/**
 * Deterministic date provider for unit testing.
 */
class FakePracticeDateProvider(var today: PracticeDate) : PracticeDateProvider {
    override fun getTodayDate(): PracticeDate = today
}

/**
 * P0.3 Verification: Authoritative Practice Streak calculation, Room persistence,
 * calendar date semantics, and edge cases.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class PracticeStreakTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var database: AppDatabase
    private lateinit var repository: GameRepository
    private lateinit var fakeDateProvider: FakePracticeDateProvider

    private val monday = PracticeDate(2026, 10, 5)     // Monday
    private val tuesday = PracticeDate(2026, 10, 6)    // Tuesday
    private val wednesday = PracticeDate(2026, 10, 7)  // Wednesday
    private val thursday = PracticeDate(2026, 10, 8)   // Thursday
    private val friday = PracticeDate(2026, 10, 9)     // Friday

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeDateProvider = FakePracticeDateProvider(monday)

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries()
            .setQueryExecutor { it.run() }
            .setTransactionExecutor { it.run() }
            .build()

        repository = GameRepository(
            userDao = database.userProgressDao(),
            upaDao = database.upaSutraProgressDao(),
            streakDao = database.practiceStreakDao(),
            dateProvider = fakeDateProvider
        )
    }

    @After
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }

    // -------------------------------------------------------------
    // Pure Logic Unit Tests: StreakCalculator
    // -------------------------------------------------------------

    @Test
    fun testNoPreviousPracticeStartsAtStreakOne() {
        // null last practice date
        val (streak, dateStr) = StreakCalculator.calculateNewStreak(
            currentStreak = 0,
            lastPracticeDateStr = null,
            today = monday
        )
        assertEquals(1, streak)
        assertEquals("2026-10-05", dateStr)
    }

    @Test
    fun testFirstPracticeZeroToOne() {
        val (streak, dateStr) = StreakCalculator.calculateNewStreak(
            currentStreak = 0,
            lastPracticeDateStr = null,
            today = monday
        )
        assertEquals(1, streak)
        assertEquals(monday.toIsoString(), dateStr)
    }

    @Test
    fun testSameDayPracticeDoesNotIncrementStreak() {
        // First practice on Monday
        val (streak1, date1) = StreakCalculator.calculateNewStreak(
            currentStreak = 0,
            lastPracticeDateStr = null,
            today = monday
        )
        assertEquals(1, streak1)

        // Practice again on Monday
        val (streak2, date2) = StreakCalculator.calculateNewStreak(
            currentStreak = streak1,
            lastPracticeDateStr = date1,
            today = monday
        )
        assertEquals(1, streak2)
        assertEquals(date1, date2)

        // Third session on Monday
        val (streak3, date3) = StreakCalculator.calculateNewStreak(
            currentStreak = streak2,
            lastPracticeDateStr = date2,
            today = monday
        )
        assertEquals(1, streak3)
    }

    @Test
    fun testConsecutiveDayPracticeIncrementsStreak() {
        // Day 1: Monday -> streak = 1
        val (s1, d1) = StreakCalculator.calculateNewStreak(0, null, monday)
        assertEquals(1, s1)
        assertEquals("2026-10-05", d1)

        // Day 2: Tuesday -> streak = 2
        val (s2, d2) = StreakCalculator.calculateNewStreak(s1, d1, tuesday)
        assertEquals(2, s2)
        assertEquals("2026-10-06", d2)

        // Day 3: Wednesday -> streak = 3
        val (s3, d3) = StreakCalculator.calculateNewStreak(s2, d2, wednesday)
        assertEquals(3, s3)
        assertEquals("2026-10-07", d3)
    }

    @Test
    fun testMissedDayResetsStreakToOneOnNextPractice() {
        // Monday -> 1
        val (s1, d1) = StreakCalculator.calculateNewStreak(0, null, monday)
        assertEquals(1, s1)

        // Tuesday -> 2
        val (s2, d2) = StreakCalculator.calculateNewStreak(s1, d1, tuesday)
        assertEquals(2, s2)

        // Wednesday -> No practice

        // Thursday -> 1
        val (s4, d4) = StreakCalculator.calculateNewStreak(s2, d2, thursday)
        assertEquals(1, s4)
        assertEquals("2026-10-08", d4)
    }

    @Test
    fun testFutureDateSafetyRecovery() {
        // Suppose clock was set ahead to next year or next week
        val futureDateStr = "2026-10-25"
        val (streak, dateStr) = StreakCalculator.calculateNewStreak(
            currentStreak = 5,
            lastPracticeDateStr = futureDateStr,
            today = monday // 2026-10-05 is before future date
        )
        // Safe recovery: do not crash, do not increment; reset safely to 1
        assertEquals(1, streak)
        assertEquals(monday.toIsoString(), dateStr)
    }

    @Test
    fun testCorruptDateStringRecovery() {
        val (streak, dateStr) = StreakCalculator.calculateNewStreak(
            currentStreak = 42,
            lastPracticeDateStr = "INVALID_CORRUPT_DATE_STRING",
            today = monday
        )
        // Safe recovery: resets safely to 1
        assertEquals(1, streak)
        assertEquals(monday.toIsoString(), dateStr)
    }

    // -------------------------------------------------------------
    // Repository & Room Persistence Tests
    // -------------------------------------------------------------

    @Test
    fun testFreshInstallDefaultsToZeroStreak() = runTest {
        val initial = repository.getPracticeStreakSnapshot()
        assertEquals(0, initial.currentStreak)
        assertNull(initial.lastPracticeDate)

        val flowEmission = repository.getPracticeStreak().first()
        assertEquals(0, flowEmission.currentStreak)
        assertNull(flowEmission.lastPracticeDate)
    }

    @Test
    fun testRecordPracticeDayPersistsInRoom() = runTest {
        fakeDateProvider.today = monday
        val streakAfterMonday = repository.recordPracticeDay()
        assertEquals(1, streakAfterMonday.currentStreak)
        assertEquals("2026-10-05", streakAfterMonday.lastPracticeDate)

        // Verify direct snapshot from Room
        val snapshot = repository.getPracticeStreakSnapshot()
        assertEquals(1, snapshot.currentStreak)
        assertEquals("2026-10-05", snapshot.lastPracticeDate)

        // Multiple sessions same day -> remains 1
        repository.recordPracticeDay()
        repository.recordPracticeDay()
        val sameDaySnapshot = repository.getPracticeStreakSnapshot()
        assertEquals(1, sameDaySnapshot.currentStreak)

        // Next day practice -> increments to 2
        fakeDateProvider.today = tuesday
        val streakAfterTuesday = repository.recordPracticeDay()
        assertEquals(2, streakAfterTuesday.currentStreak)
        assertEquals("2026-10-06", streakAfterTuesday.lastPracticeDate)
    }

    @Test
    fun testStreakSurvivesRepositoryAndAppReload() = runTest {
        // Record practice on Monday & Tuesday
        fakeDateProvider.today = monday
        repository.recordPracticeDay()

        fakeDateProvider.today = tuesday
        repository.recordPracticeDay()

        assertEquals(2, repository.getPracticeStreakSnapshot().currentStreak)

        // Simulate app kill & reload: create a new repository instance pointing to the same DB
        val reloadedRepository = GameRepository(
            userDao = database.userProgressDao(),
            upaDao = database.upaSutraProgressDao(),
            streakDao = database.practiceStreakDao(),
            dateProvider = fakeDateProvider
        )

        val reloadedSnapshot = reloadedRepository.getPracticeStreakSnapshot()
        assertEquals(2, reloadedSnapshot.currentStreak)
        assertEquals("2026-10-06", reloadedSnapshot.lastPracticeDate)

        val reloadedFlow = reloadedRepository.getPracticeStreak().first()
        assertEquals(2, reloadedFlow.currentStreak)
        assertEquals("2026-10-06", reloadedFlow.lastPracticeDate)
    }

    // -------------------------------------------------------------
    // ViewModel Reactive Data Flow & Practice Completion Tests
    // -------------------------------------------------------------

    @Test
    fun testFreshInstallUiStateHasZeroStreak() {
        val viewModel = GameViewModel()
        val state = viewModel.uiState.value
        assertEquals("Fresh install streak must be 0", 0, state.currentStreak)
        assertNull("Fresh install lastPracticeDate must be null", state.lastPracticeDate)
    }

    @Test
    fun testPracticeCompletionFlowsThroughViewModelToUiState() = runTest {
        val viewModel = GameViewModel()
        viewModel.initRepository(repository)
        advanceUntilIdle()

        assertEquals(0, viewModel.uiState.value.currentStreak)

        // User completes a lesson (World 1) on Monday
        fakeDateProvider.today = monday
        viewModel.completeWorld(worldId = 1, score = 100)
        advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.currentStreak)
        assertEquals("2026-10-05", viewModel.uiState.value.lastPracticeDate)

        // User practices again same day (World 2) on Monday
        viewModel.completeWorld(worldId = 2, score = 100)
        advanceUntilIdle()

        // Streak must remain 1 on the same day
        assertEquals(1, viewModel.uiState.value.currentStreak)

        // Next day (Tuesday) user completes World 3
        fakeDateProvider.today = tuesday
        viewModel.completeWorld(worldId = 3, score = 100)
        advanceUntilIdle()

        // Streak increments to 2
        assertEquals(2, viewModel.uiState.value.currentStreak)
        assertEquals("2026-10-06", viewModel.uiState.value.lastPracticeDate)
    }

    @Test
    fun testOpenAppAndNavigationDoesNotChangeStreak() = runTest {
        val viewModel = GameViewModel()
        viewModel.initRepository(repository)
        advanceUntilIdle()

        // Earn streak 1 on Monday
        fakeDateProvider.today = monday
        viewModel.completeWorld(worldId = 1, score = 100)
        advanceUntilIdle()
        assertEquals(1, viewModel.uiState.value.currentStreak)

        // User navigates around the app on Tuesday without completing practice
        fakeDateProvider.today = tuesday
        viewModel.navigateToTutorial()
        viewModel.startPractice()
        viewModel.openTreasury()
        viewModel.returnToWorldSelect()
        advanceUntilIdle()

        // Streak must remain unchanged until user actually finishes a lesson
        assertEquals(1, viewModel.uiState.value.currentStreak)
    }

    // -------------------------------------------------------------
    // Midnight Transition & Calendar Date Math Tests
    // -------------------------------------------------------------

    @Test
    fun testCalendarDatePrecedingAndNextMathAcrossMonthBoundaries() {
        // March 1 -> Feb 28 in non-leap year (2026)
        val march1st2026 = PracticeDate(2026, 3, 1)
        val feb28th2026 = march1st2026.previousDay()
        assertEquals(2026, feb28th2026.year)
        assertEquals(2, feb28th2026.month)
        assertEquals(28, feb28th2026.day)

        // March 1 -> Feb 29 in leap year (2024)
        val march1st2024 = PracticeDate(2024, 3, 1)
        val feb29th2024 = march1st2024.previousDay()
        assertEquals(2024, feb29th2024.year)
        assertEquals(2, feb29th2024.month)
        assertEquals(29, feb29th2024.day)

        // Jan 1 -> Dec 31 previous year
        val jan1st2026 = PracticeDate(2026, 1, 1)
        val dec31st2025 = jan1st2026.previousDay()
        assertEquals(2025, dec31st2025.year)
        assertEquals(12, dec31st2025.month)
        assertEquals(31, dec31st2025.day)

        // Dec 31 -> Jan 1 next year
        val jan1stNext = dec31st2025.nextDay()
        assertEquals(jan1st2026, jan1stNext)
    }

    @Test
    fun testMidnightTransitionInLocalTimezone() {
        val nyTimeZone = TimeZone.getTimeZone("America/New_York")

        // Practice at 11:59 PM on 2026-10-05 in New York
        val cal1159PM = Calendar.getInstance(nyTimeZone).apply {
            clear()
            set(2026, Calendar.OCTOBER, 5, 23, 59, 0)
        }
        val date1159PM = PracticeDate.fromCalendar(cal1159PM)
        assertEquals(PracticeDate(2026, 10, 5), date1159PM)

        val (s1, d1) = StreakCalculator.calculateNewStreak(0, null, date1159PM)
        assertEquals(1, s1)
        assertEquals("2026-10-05", d1)

        // Two minutes later: 12:01 AM on 2026-10-06 in New York
        val cal1201AM = Calendar.getInstance(nyTimeZone).apply {
            clear()
            set(2026, Calendar.OCTOBER, 6, 0, 1, 0)
        }
        val date1201AM = PracticeDate.fromCalendar(cal1201AM)
        assertEquals(PracticeDate(2026, 10, 6), date1201AM)

        // Even though only 2 minutes elapsed, calendar day changed -> streak increments!
        val (s2, d2) = StreakCalculator.calculateNewStreak(s1, d1, date1201AM)
        assertEquals(2, s2)
        assertEquals("2026-10-06", d2)
    }
}
