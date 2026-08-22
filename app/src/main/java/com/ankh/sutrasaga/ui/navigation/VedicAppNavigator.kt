package com.ankh.sutrasaga.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.ui.screens.HomeScreen
import com.ankh.sutrasaga.ui.screens.SampleUrdhvaLesson
import com.ankh.sutrasaga.ui.screens.SutraLesson
import com.ankh.sutrasaga.ui.screens.SutraModule
import com.ankh.sutrasaga.ui.screens.SutraSolverScreen
import com.ankh.sutrasaga.ui.screens.UpaSutraCodexScreen
import com.ankh.sutrasaga.ui.screens.UpaSutraTreasuryScreen
import com.ankh.sutrasaga.ui.screens.VedicPracticeArenaScreen
import com.ankh.sutrasaga.ui.viewmodel.GameUiState

enum class VedicAppRoute {
    HOME,
    SUTRA_SOLVER,
    PRACTICE_ARENA,
    UPA_SUTRA_TREASURY,
    UPA_SUTRA_CODEX
}

/**
 * VedicAppNavigator — Top-Level Navigation Engine & State Coordinator
 * Handles:
 * 1. HomeScreen -> Select Sutra -> SutraSolverScreen
 * 2. HomeScreen -> Quick Start -> PracticeArenaScreen
 * 3. HomeScreen -> Treasury -> UpaSutraTreasuryScreen -> Codex
 * 4. Back navigation across all sub-screens
 */
@Composable
fun VedicAppNavigator(
    initialRoute: VedicAppRoute = VedicAppRoute.HOME,
    uiState: GameUiState? = null,
    onStartQuest: (UpaSutraId) -> Unit = {}
) {
    var currentRoute by remember { mutableStateOf(initialRoute) }
    var currentStreak by remember { mutableIntStateOf(7) }
    var selectedLesson by remember { mutableStateOf<SutraLesson>(SampleUrdhvaLesson) }

    // System Back Navigation handling
    when (currentRoute) {
        VedicAppRoute.HOME -> {
            // Default system back
        }
        VedicAppRoute.SUTRA_SOLVER,
        VedicAppRoute.PRACTICE_ARENA,
        VedicAppRoute.UPA_SUTRA_TREASURY -> {
            BackHandler {
                currentRoute = VedicAppRoute.HOME
            }
        }
        VedicAppRoute.UPA_SUTRA_CODEX -> {
            BackHandler {
                currentRoute = VedicAppRoute.UPA_SUTRA_TREASURY
            }
        }
    }

    Crossfade(targetState = currentRoute, label = "VedicScreenNavigation") { route ->
        when (route) {
            VedicAppRoute.HOME -> {
                HomeScreen(
                    streakDays = currentStreak,
                    onSelectSutra = { module: SutraModule ->
                        selectedLesson = com.ankh.sutrasaga.ui.screens.SutraLessonsRepository.getLessonForModule(module)
                        currentRoute = VedicAppRoute.SUTRA_SOLVER
                    },
                    onStartPracticeArena = {
                        currentRoute = VedicAppRoute.PRACTICE_ARENA
                    },
                    onOpenTreasury = {
                        currentRoute = VedicAppRoute.UPA_SUTRA_TREASURY
                    }
                )
            }

            VedicAppRoute.SUTRA_SOLVER -> {
                SutraSolverScreen(
                    lesson = selectedLesson,
                    onBackClick = {
                        currentRoute = VedicAppRoute.HOME
                    },
                    onLessonComplete = {
                        currentStreak++
                        currentRoute = VedicAppRoute.HOME
                    }
                )
            }

            VedicAppRoute.PRACTICE_ARENA -> {
                VedicPracticeArenaScreen(
                    onBackClick = {
                        currentRoute = VedicAppRoute.HOME
                    },
                    onFinishArena = { score, opsPerMin ->
                        currentRoute = VedicAppRoute.HOME
                    }
                )
            }

            VedicAppRoute.UPA_SUTRA_TREASURY -> {
                UpaSutraTreasuryScreen(
                    state = uiState ?: GameUiState(),
                    onQuestClick = { questId: UpaSutraId ->
                        onStartQuest(questId)
                    },
                    onCodexClick = {
                        currentRoute = VedicAppRoute.UPA_SUTRA_CODEX
                    },
                    onBackClick = {
                        currentRoute = VedicAppRoute.HOME
                    }
                )
            }

            VedicAppRoute.UPA_SUTRA_CODEX -> {
                UpaSutraCodexScreen(
                    state = uiState ?: GameUiState(),
                    onBackClick = {
                        currentRoute = VedicAppRoute.UPA_SUTRA_TREASURY
                    }
                )
            }
        }
    }
}
