package com.ankh.sutrasaga.ui

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.domain.models.UpaSutraRegistry
import com.ankh.sutrasaga.ui.viewmodel.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UpaSutraRegistryAndQuestScreenTest {

    @Test
    fun testUpaSutraRegistryContainsAllThirteenUpaSutras() {
        val allIds = UpaSutraId.values()
        assertEquals("Registry must define all 13 Upa-Sutras", 13, allIds.size)
        assertEquals("Registry entries count must be 13", 13, UpaSutraRegistry.entries.size)

        for (id in allIds) {
            val def = UpaSutraRegistry.getById(id)
            assertNotNull("Definition for $id must exist", def)
            assertEquals(id, def.id)
            assertTrue("Display name must not be blank for $id", def.displayName.isNotBlank())
            assertTrue("Sanskrit name must not be blank for $id", def.sanskritName.isNotBlank())
            assertTrue("Meaning must not be blank for $id", def.meaning.isNotBlank())
            assertTrue("Worked example must not be blank for $id", def.workedExample.isNotBlank())
            assertTrue("Description must not be blank for $id", def.description.isNotBlank())
            assertTrue("Parent world must be in 1..16 for $id", def.parentWorldId in 1..16)
        }
    }

    @Test
    fun testAllThirteenUpaSutrasCanAdvanceFullQuestCycleInViewModel() {
        val allIds = UpaSutraId.values()
        val viewModel = GameViewModel()

        for (id in allIds) {
            viewModel.startUpaSutraQuest(id)
            assertEquals(id, viewModel.uiState.value.selectedUpaSutraId)

            // Story Beat -> Guided Example
            viewModel.advanceQuestStage()
            assertEquals("Stage must be GUIDED_EXAMPLE for $id", com.ankh.sutrasaga.ui.viewmodel.UpaSutraQuestStage.GUIDED_EXAMPLE, viewModel.uiState.value.questStage)

            // Guided Example -> Practice Set
            viewModel.advanceQuestStage()
            assertEquals("Stage must be PRACTICE for $id", com.ankh.sutrasaga.ui.viewmodel.UpaSutraQuestStage.PRACTICE, viewModel.uiState.value.questStage)
            assertTrue("Practice problem set must not be empty for $id", viewModel.uiState.value.problemList.isNotEmpty())
            assertNotNull("Current problem must not be null for $id", viewModel.uiState.value.currentProblem)

            // Practice -> Challenge Set
            viewModel.advanceQuestStage()
            assertEquals("Stage must be CHALLENGE for $id", com.ankh.sutrasaga.ui.viewmodel.UpaSutraQuestStage.CHALLENGE, viewModel.uiState.value.questStage)
            assertTrue("Challenge problem set must not be empty for $id", viewModel.uiState.value.problemList.isNotEmpty())
            assertNotNull("Current problem must not be null for $id", viewModel.uiState.value.currentProblem)

            // Challenge -> Reward
            viewModel.advanceQuestStage()
            assertEquals("Stage must be REWARD for $id", com.ankh.sutrasaga.ui.viewmodel.UpaSutraQuestStage.REWARD, viewModel.uiState.value.questStage)
        }
    }

    @Test
    fun testAllSixteenWorldsProduceValidPracticeAndBossProblems() {
        val viewModel = GameViewModel()

        for (worldId in 1..16) {
            viewModel.selectWorld(worldId)
            assertEquals(worldId, viewModel.uiState.value.selectedWorldId)

            // Start Practice (Tier 1)
            viewModel.startPractice()
            assertEquals(5, viewModel.uiState.value.problemList.size)
            val practiceProb = viewModel.uiState.value.currentProblem
            assertNotNull("World $worldId practice problem must not be null", practiceProb)
            assertTrue("World $worldId questionText must not be blank", practiceProb!!.questionText.isNotBlank())
            assertTrue("World $worldId decomposition steps must not be empty", practiceProb.decompositionSteps.isNotEmpty())

            // Start Boss Battle (Tier 2)
            viewModel.startBossBattle()
            assertEquals(5, viewModel.uiState.value.problemList.size)
            val bossProb = viewModel.uiState.value.currentProblem
            assertNotNull("World $worldId boss problem must not be null", bossProb)
            assertTrue("World $worldId boss questionText must not be blank", bossProb!!.questionText.isNotBlank())
            assertTrue("World $worldId boss decomposition steps must not be empty", bossProb.decompositionSteps.isNotEmpty())
        }
    }
}
