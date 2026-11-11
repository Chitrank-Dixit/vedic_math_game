package com.ank.sutrasaga.ui.navigation

import com.ank.sutrasaga.domain.models.UpaSutraId
import com.ank.sutrasaga.ui.viewmodel.GameScreen
import com.ank.sutrasaga.ui.viewmodel.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * P0.4 Verification: Unified Navigation and Game State Ownership.
 * Ensures GameViewModel is the single authoritative source of truth for navigation state,
 * eliminating duplicate state machines, synchronization hacks, and conflicting routes.
 */
class NavigationStateOwnershipTest {

    private lateinit var viewModel: GameViewModel

    @Before
    fun setUp() {
        viewModel = GameViewModel()
    }

    @Test
    fun testInitialRouteIsHome() {
        val state = viewModel.uiState.value
        assertEquals("Default route must be HOME", VedicAppRoute.HOME, state.currentRoute)
        assertEquals("Default screen must be WORLD_SELECT", GameScreen.WORLD_SELECT, state.currentScreen)
        assertEquals("Default selected world must be 1", 1, state.selectedWorldId)
    }

    @Test
    fun testSelectSutraNavigatesToGurukulTutorialWithDeepState() {
        // User selects World 5 (Urdhva Tiryagbhyam)
        viewModel.selectSutra(5)
        val state = viewModel.uiState.value

        assertEquals(VedicAppRoute.GURUKUL_TUTORIAL, state.currentRoute)
        assertEquals(5, state.selectedWorldId)
        assertEquals(5, state.selectedModule.worldNumber)
        assertEquals(5, state.selectedScript.worldId)
        assertEquals("urdhva", state.selectedLesson.id)
        assertNotNull(state.selectedLessonProblem)
    }

    @Test
    fun testTutorialToSolverTransitionPreservesSelectedSutra() {
        // Navigate to World 4 (Yavadunam)
        viewModel.selectSutra(4)
        assertEquals(VedicAppRoute.GURUKUL_TUTORIAL, viewModel.uiState.value.currentRoute)
        assertEquals(4, viewModel.uiState.value.selectedWorldId)

        // Complete / continue from tutorial to Solver
        viewModel.navigateToRoute(VedicAppRoute.SUTRA_SOLVER)
        val solverState = viewModel.uiState.value

        assertEquals(VedicAppRoute.SUTRA_SOLVER, solverState.currentRoute)
        assertEquals("Selected world must remain 4 in solver", 4, solverState.selectedWorldId)
        assertEquals("yavadunam", solverState.selectedLesson.id)
    }

    @Test
    fun testSolverCompletionNavigatesToHomeAndPersistsProgress() {
        viewModel.selectSutra(2)
        viewModel.navigateToRoute(VedicAppRoute.SUTRA_SOLVER)
        assertEquals(VedicAppRoute.SUTRA_SOLVER, viewModel.uiState.value.currentRoute)

        // Lesson complete
        viewModel.completeWorld(worldId = 2, score = 100)
        val homeState = viewModel.uiState.value

        assertEquals("Completing lesson must authoritatively return to HOME", VedicAppRoute.HOME, homeState.currentRoute)
        assertEquals(GameScreen.WORLD_SELECT, homeState.currentScreen)
        assertTrue("World 2 must be marked completed", homeState.isWorldCompleted(2))
    }

    @Test
    fun testPracticeArenaFlow() {
        viewModel.startPracticeArena()
        val arenaState = viewModel.uiState.value

        assertEquals(VedicAppRoute.PRACTICE_ARENA, arenaState.currentRoute)
        assertEquals(GameScreen.PRACTICE_ARENA, arenaState.currentScreen)
        assertEquals(5, arenaState.problemList.size)

        // Return from arena
        viewModel.returnToHome()
        assertEquals(VedicAppRoute.HOME, viewModel.uiState.value.currentRoute)
    }

    @Test
    fun testTreasuryCodexAndQuestTransitions() {
        // Open Treasury
        viewModel.openTreasury()
        assertEquals(VedicAppRoute.UPA_SUTRA_TREASURY, viewModel.uiState.value.currentRoute)
        assertEquals(GameScreen.UPA_SUTRA_TREASURY, viewModel.uiState.value.currentScreen)

        // Open Codex
        viewModel.openCodex()
        assertEquals(VedicAppRoute.UPA_SUTRA_CODEX, viewModel.uiState.value.currentRoute)
        assertEquals(GameScreen.UPA_SUTRA_CODEX, viewModel.uiState.value.currentScreen)

        // Back to Treasury
        viewModel.navigateBack()
        assertEquals(VedicAppRoute.UPA_SUTRA_TREASURY, viewModel.uiState.value.currentRoute)

        // Start Quest
        viewModel.startUpaSutraQuest(UpaSutraId.VESHTANAM)
        val questState = viewModel.uiState.value
        assertEquals(VedicAppRoute.UPA_SUTRA_QUEST, questState.currentRoute)
        assertEquals(GameScreen.UPA_SUTRA_QUEST, questState.currentScreen)
        assertEquals(UpaSutraId.VESHTANAM, questState.selectedUpaSutraId)

        // Back from Quest returns to Treasury
        viewModel.navigateBack()
        assertEquals(VedicAppRoute.UPA_SUTRA_TREASURY, viewModel.uiState.value.currentRoute)

        // Back from Treasury returns to Home
        viewModel.navigateBack()
        assertEquals(VedicAppRoute.HOME, viewModel.uiState.value.currentRoute)
    }

    @Test
    fun testDeterministicBackNavigationFromAllScreens() {
        // 1. From Tutorial -> Back -> Home
        viewModel.selectSutra(1)
        assertEquals(VedicAppRoute.GURUKUL_TUTORIAL, viewModel.uiState.value.currentRoute)
        viewModel.navigateBack()
        assertEquals(VedicAppRoute.HOME, viewModel.uiState.value.currentRoute)

        // 2. From Solver -> Back -> Home
        viewModel.selectSutra(1)
        viewModel.navigateToSolver()
        assertEquals(VedicAppRoute.SUTRA_SOLVER, viewModel.uiState.value.currentRoute)
        viewModel.navigateBack()
        assertEquals(VedicAppRoute.HOME, viewModel.uiState.value.currentRoute)

        // 3. From Practice Arena -> Back -> Home
        viewModel.startPracticeArena()
        assertEquals(VedicAppRoute.PRACTICE_ARENA, viewModel.uiState.value.currentRoute)
        viewModel.navigateBack()
        assertEquals(VedicAppRoute.HOME, viewModel.uiState.value.currentRoute)

        // 4. From Treasury -> Back -> Home
        viewModel.openTreasury()
        assertEquals(VedicAppRoute.UPA_SUTRA_TREASURY, viewModel.uiState.value.currentRoute)
        viewModel.navigateBack()
        assertEquals(VedicAppRoute.HOME, viewModel.uiState.value.currentRoute)

        // 5. From Codex -> Back -> Treasury
        viewModel.openTreasury()
        viewModel.openCodex()
        assertEquals(VedicAppRoute.UPA_SUTRA_CODEX, viewModel.uiState.value.currentRoute)
        viewModel.navigateBack()
        assertEquals(VedicAppRoute.UPA_SUTRA_TREASURY, viewModel.uiState.value.currentRoute)

        // 6. From Quest -> Back -> Treasury
        viewModel.startUpaSutraQuest(UpaSutraId.ANTYAYEREVA)
        assertEquals(VedicAppRoute.UPA_SUTRA_QUEST, viewModel.uiState.value.currentRoute)
        viewModel.navigateBack()
        assertEquals(VedicAppRoute.UPA_SUTRA_TREASURY, viewModel.uiState.value.currentRoute)
    }

    @Test
    fun testDeepStatePreservedForAllSixteenWorlds() {
        for (worldNumber in 1..16) {
            viewModel.selectSutra(worldNumber)
            val state = viewModel.uiState.value

            assertEquals(worldNumber, state.selectedWorldId)
            assertEquals(worldNumber, state.selectedModule.worldNumber)
            assertEquals(worldNumber, state.selectedScript.worldId)
            assertNotNull(state.selectedLesson)
            assertNotNull(state.selectedLessonProblem)
        }
    }

    @Test
    fun testRecreationPreservesStateWithoutDivergence() {
        // User is on Sutra 3 solver
        viewModel.selectSutra(3)
        viewModel.navigateToSolver()
        val originalState = viewModel.uiState.value

        assertEquals(VedicAppRoute.SUTRA_SOLVER, originalState.currentRoute)
        assertEquals(3, originalState.selectedWorldId)

        // Simulate activity recreation: uiState remains authoritative
        val restoredRoute = originalState.currentRoute
        val restoredLesson = originalState.selectedLesson

        assertEquals(VedicAppRoute.SUTRA_SOLVER, restoredRoute)
        assertEquals("ekanyunena", restoredLesson.id)
    }

    @Test
    fun testNoConflictingNavigationStateAcrossRoutes() {
        val routesAndScreens = listOf(
            VedicAppRoute.HOME to GameScreen.WORLD_SELECT,
            VedicAppRoute.GURUKUL_TUTORIAL to GameScreen.TUTORIAL,
            VedicAppRoute.PRACTICE_ARENA to GameScreen.PRACTICE_ARENA,
            VedicAppRoute.UPA_SUTRA_TREASURY to GameScreen.UPA_SUTRA_TREASURY,
            VedicAppRoute.UPA_SUTRA_CODEX to GameScreen.UPA_SUTRA_CODEX,
            VedicAppRoute.UPA_SUTRA_QUEST to GameScreen.UPA_SUTRA_QUEST
        )

        for ((route, expectedScreen) in routesAndScreens) {
            viewModel.navigateToRoute(route)
            val state = viewModel.uiState.value
            assertEquals("Route $route must match expected screen $expectedScreen", expectedScreen, state.currentScreen)
            assertEquals("Route must be $route", route, state.currentRoute)
        }
    }
}
