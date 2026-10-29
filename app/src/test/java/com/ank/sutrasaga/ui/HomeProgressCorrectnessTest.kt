package com.ank.sutrasaga.ui

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ank.sutrasaga.data.db.AppDatabase
import com.ank.sutrasaga.data.repository.GameRepository
import com.ank.sutrasaga.ui.screens.CanonicalSutraModules
import com.ank.sutrasaga.ui.screens.SampleSutraModules
import com.ank.sutrasaga.ui.screens.buildSutraModules
import com.ank.sutrasaga.ui.viewmodel.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * P0.2 Verification: Home screen progress correctness, lifecycle persistence, and reactive data flow.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class HomeProgressCorrectnessTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var database: AppDatabase
    private lateinit var repository: GameRepository

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
        repository = GameRepository(database.userProgressDao(), database.upaSutraProgressDao())
    }

    @After
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }

    @Test
    fun testCanonicalSutraModulesHaveNoFakeProgress() {
        assertEquals(16, CanonicalSutraModules.size)

        // On fresh installation, all 16 canonical modules must be at initial 0% progress
        for (module in CanonicalSutraModules) {
            assertEquals("Module ${module.id} must start at 0% progress", 0.0f, module.progress, 0.001f)
            assertTrue("Module ${module.id} must have valid worldNumber 1..16", module.worldNumber in 1..16)
        }

        // Verify distinct world numbers across all 16 modules
        val distinctWorlds = CanonicalSutraModules.map { it.worldNumber }.toSet()
        assertEquals(16, distinctWorlds.size)

        // Verify distinct IDs
        val distinctIds = CanonicalSutraModules.map { it.id }.toSet()
        assertEquals(16, distinctIds.size)
    }

    @Test
    fun testFreshInstallUiStateHasZeroProgressForAllSutras() {
        val viewModel = GameViewModel()
        val state = viewModel.uiState.value

        assertEquals(16, state.sutraModules.size)
        for (module in state.sutraModules) {
            assertEquals("Fresh install module ${module.id} must be 0%", 0.0f, module.progress, 0.001f)
            assertFalse("Fresh install world ${module.worldNumber} must not be completed", state.isWorldCompleted(module.worldNumber))
        }

        // Verify that none of the old sample values (1.0f, 0.85f, 0.50f, 0.70f, 0.30f) appear in production state
        val nonZeroModules = state.sutraModules.filter { it.progress > 0.0f }
        assertTrue("No module should have non-zero progress on fresh install", nonZeroModules.isEmpty())
    }

    @Test
    fun testCompleteWorldUpdatesOnlyTargetSutraProgress() {
        val viewModel = GameViewModel()

        // Complete World 1 (Ekadhikena Purvena)
        viewModel.completeWorld(worldId = 1, score = 100)
        val stateAfterW1 = viewModel.uiState.value

        val w1Module = stateAfterW1.sutraModules.first { it.worldNumber == 1 }
        assertEquals("ekadhikena", w1Module.id)
        assertEquals("World 1 must be 100% complete", 1.0f, w1Module.progress, 0.001f)

        // All other 15 modules must remain strictly 0%
        val otherModules = stateAfterW1.sutraModules.filter { it.worldNumber != 1 }
        assertEquals(15, otherModules.size)
        for (other in otherModules) {
            assertEquals("Module ${other.id} (World ${other.worldNumber}) must remain 0%", 0.0f, other.progress, 0.001f)
        }

        // Complete World 5 (Urdhva-Tiryagbhyam)
        viewModel.completeWorld(worldId = 5, score = 90)
        val stateAfterW5 = viewModel.uiState.value

        val w5Module = stateAfterW5.sutraModules.first { it.worldNumber == 5 }
        assertEquals("urdhva", w5Module.id)
        assertEquals("World 5 must be 100% complete", 1.0f, w5Module.progress, 0.001f)

        // Verify Worlds 1 and 5 are completed, remaining 14 are 0%
        for (module in stateAfterW5.sutraModules) {
            if (module.worldNumber == 1 || module.worldNumber == 5) {
                assertEquals("World ${module.worldNumber} must be 100%", 1.0f, module.progress, 0.001f)
            } else {
                assertEquals("World ${module.worldNumber} must be 0%", 0.0f, module.progress, 0.001f)
            }
        }
    }

    @Test
    fun testStableIdMappingRegardlessOfOrder() {
        // Build progress with world 2 (nikhilam) and world 6 (paravartya) completed
        val completedWorlds = setOf(2, 6)
        val modules = buildSutraModules { worldId -> completedWorlds.contains(worldId) }

        val nikhilam = modules.first { it.id == "nikhilam" }
        assertEquals(2, nikhilam.worldNumber)
        assertEquals(1.0f, nikhilam.progress, 0.001f)

        val paravartya = modules.first { it.id == "paravartya" }
        assertEquals(6, paravartya.worldNumber)
        assertEquals(1.0f, paravartya.progress, 0.001f)

        val ekadhikena = modules.first { it.id == "ekadhikena" }
        assertEquals(1, ekadhikena.worldNumber)
        assertEquals(0.0f, ekadhikena.progress, 0.001f)
    }

    @Test
    fun testRoomPersistenceAndReactiveFlowUpdate() = runTest(testDispatcher) {
        val viewModel = GameViewModel()
        viewModel.initRepository(repository)

        // Initial check before database has any entries
        advanceUntilIdle()
        val initialState = viewModel.uiState.value
        assertEquals(0.0f, initialState.sutraModules.first { it.worldNumber == 1 }.progress, 0.001f)

        // Complete World 1 via ViewModel
        viewModel.completeWorld(1, score = 200)
        advanceUntilIdle()

        // Verify Room database has received the completion
        val dbRecord = database.userProgressDao().getProgressForWorld(1)
        assertTrue("Room entity must be completed", dbRecord?.isCompleted == true)
        assertEquals(200, dbRecord?.bestScore)

        // Verify ViewModel reactively updated sutraModules
        val stateAfterSave = viewModel.uiState.value
        val w1 = stateAfterSave.sutraModules.first { it.worldNumber == 1 }
        assertEquals(1.0f, w1.progress, 0.001f)
    }

    @Test
    fun testProgressPersistenceAcrossAppRestart() = runTest(testDispatcher) {
        // Step 1: Session 1 completes World 1 and World 4 in Room
        repository.saveWorldCompletion(worldId = 1, score = 300)
        repository.saveWorldCompletion(worldId = 4, score = 250)

        // Step 2: Simulate App Restart by instantiating a completely new GameViewModel
        val restartedViewModel = GameViewModel()
        // Initial state before DB connects is clean 0%
        assertEquals(0.0f, restartedViewModel.uiState.value.sutraModules.first { it.worldNumber == 1 }.progress, 0.001f)

        // Connect repository to the new ViewModel (simulating MainActivity.onCreate)
        restartedViewModel.initRepository(repository)
        advanceUntilIdle()

        // Step 3: Verify the restarted ViewModel immediately reflects real persisted progress from Room
        val stateAfterRestart = restartedViewModel.uiState.value
        val w1 = stateAfterRestart.sutraModules.first { it.worldNumber == 1 }
        val w4 = stateAfterRestart.sutraModules.first { it.worldNumber == 4 }
        val w2 = stateAfterRestart.sutraModules.first { it.worldNumber == 2 }

        assertEquals("World 1 must remain 100% after restart", 1.0f, w1.progress, 0.001f)
        assertEquals("World 4 must remain 100% after restart", 1.0f, w4.progress, 0.001f)
        assertEquals("World 2 was never completed and must remain 0% after restart", 0.0f, w2.progress, 0.001f)
    }

    @Test
    fun testSampleDataIsIsolatedFromProductionData() {
        // SampleSutraModules has hardcoded values (e.g. urdhva = 1.0f, ekadhikena = 0.85f)
        val sampleUrdhva = SampleSutraModules.first { it.id == "urdhva" }
        val sampleEkadhikena = SampleSutraModules.first { it.id == "ekadhikena" }
        assertEquals(1.0f, sampleUrdhva.progress, 0.001f)
        assertEquals(0.85f, sampleEkadhikena.progress, 0.001f)

        // But CanonicalSutraModules has clean initial 0.0f
        val canonicalUrdhva = CanonicalSutraModules.first { it.id == "urdhva" }
        val canonicalEkadhikena = CanonicalSutraModules.first { it.id == "ekadhikena" }
        assertEquals(0.0f, canonicalUrdhva.progress, 0.001f)
        assertEquals(0.0f, canonicalEkadhikena.progress, 0.001f)

        assertNotEquals(sampleEkadhikena.progress, canonicalEkadhikena.progress, 0.001f)
    }
}
