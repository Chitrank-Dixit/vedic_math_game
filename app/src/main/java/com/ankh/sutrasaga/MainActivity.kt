package com.ankh.sutrasaga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ankh.sutrasaga.ui.navigation.VedicAppNavigator
import com.ankh.sutrasaga.ui.theme.AnkhTheme
import com.ankh.sutrasaga.ui.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnkhTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val context = LocalContext.current
                    LaunchedEffect(Unit) {
                        viewModel.initRepository(context)
                    }
                    MainGameContent(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainGameContent(viewModel: GameViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    VedicAppNavigator(
        uiState = uiState,
        onCompleteLesson = { worldId ->
            viewModel.completeWorld(worldId)
        },
        onStartQuest = { questId ->
            viewModel.startUpaSutraQuest(questId)
        },

        onAdvanceQuestStage = {
            viewModel.advanceQuestStage()
        },
        onAppendDigit = { char ->
            viewModel.appendDigit(char)
        },
        onBackspaceDigit = {
            viewModel.backspaceDigit()
        },
        onClearDigit = {
            viewModel.clearDigit()
        },
        onSubmitAnswer = {
            viewModel.submitAnswer()
        },
        onNextProblem = {
            viewModel.nextProblem()
        },
        onRevealStep = {
            viewModel.revealNextStep()
        },
        onReturnToTreasury = {
            viewModel.openTreasury()
        }
    )
}
