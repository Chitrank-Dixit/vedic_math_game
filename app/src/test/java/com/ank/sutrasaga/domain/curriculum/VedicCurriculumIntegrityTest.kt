package com.ank.sutrasaga.domain.curriculum

import com.ank.sutrasaga.domain.models.UpaSutraRegistry
import com.ank.sutrasaga.ui.screens.CanonicalSutraModules
import com.ank.sutrasaga.ui.screens.GurukulScriptsRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Automated integrity tests for the Vedic Maths Curriculum.
 *
 * Verifies:
 * 1. No duplicate lesson IDs.
 * 2. Valid world numbers (1..16 for main campaign worlds).
 * 3. Exact coverage of the 16 Principal Sutras and 13 Upa-Sutras.
 * 4. Detection of broken tutorial-to-quiz mappings (specifically World 7).
 * 5. Complete synchronization between runtime UI modules, repositories, and the manifest.
 */
class VedicCurriculumIntegrityTest {

    @Test
    fun noDuplicateLessonIds_inManifest() {
        val allIds = VedicCurriculumManifest.entries.map { it.lessonId }
        val duplicateIds = allIds.groupBy { it }.filter { it.value.size > 1 }.keys
        assertTrue("Duplicate lesson IDs detected: $duplicateIds", duplicateIds.isEmpty())
        assertEquals("Total manifest entries must be 29 (16 Principal + 13 Upa-Sutras)", 29, allIds.size)
    }

    @Test
    fun validWorldNumbers_forMainCampaign() {
        val mainWorlds = VedicCurriculumManifest.getMainCampaignWorlds()
        assertEquals("Main campaign must contain exactly 16 worlds", 16, mainWorlds.size)

        val worldNumbers = mainWorlds.mapNotNull { it.worldNumber }
        assertEquals("Every main campaign entry must have a worldNumber", 16, worldNumbers.size)

        val distinctWorldNumbers = worldNumbers.toSet()
        assertEquals("All world numbers must be distinct", 16, distinctWorldNumbers.size)
        assertEquals("World numbers must span exactly 1..16", (1..16).toSet(), distinctWorldNumbers)
    }

    @Test
    fun completeCanonicalCoverage_16PrincipalSutras() {
        val principalSutras = VedicCurriculumManifest.getPrincipalSutras()
        assertEquals("Must catalog exactly 16 Principal Sutras", 16, principalSutras.size)

        val traditionalIndices = principalSutras.map { it.traditionalIndex }.sorted()
        assertEquals(
            "Principal sutras must cover all traditional indices 1..16 from Swami Bharati Krishna Tirtha (1965)",
            (1..16).toList(),
            traditionalIndices
        )
    }

    @Test
    fun completeCanonicalCoverage_13UpaSutras() {
        val upaSutras = VedicCurriculumManifest.getUpaSutras()
        assertEquals("Must catalog exactly 13 Upa-Sutras", 13, upaSutras.size)

        val traditionalIndices = upaSutras.map { it.traditionalIndex }.sorted()
        assertEquals(
            "Upa-Sutras must cover all 13 canonical subsidiary indices",
            (1..13).toList(),
            traditionalIndices
        )

        val playableQuests = VedicCurriculumManifest.getPlayableQuests()
        val codexReferences = VedicCurriculumManifest.getCodexReferences()
        assertEquals("Must have 9 playable Treasury Quests", 9, playableQuests.size)
        assertEquals("Must have 4 Codex Reference cards", 4, codexReferences.size)
    }

    @Test
    fun detectBrokenTutorialToQuizMappings_flagsWorld7Discrepancy() {
        val divergentEntries = VedicCurriculumManifest.getMisclassifiedOrDivergentEntries()
        assertTrue("Manifest must detect divergent curriculum entries", divergentEntries.isNotEmpty())

        val world7 = VedicCurriculumManifest.getEntryByWorldNumber(7)
        assertNotNull("World 7 must exist in manifest", world7)
        assertEquals("anurupyena_square", world7?.lessonId)

        // World 7 must be explicitly flagged as misaligned between tutorial and quiz
        assertFalse(
            "World 7 tutorial-to-quiz mapping must be flagged as NOT aligned",
            world7!!.verification.tutorialQuizAligned
        )
        assertFalse(
            "World 7 must NOT be marked as reviewed and complete while divergent",
            world7.verification.isReviewedAndComplete
        )
        assertNotNull("World 7 must have explicit discrepancy notes", world7.discrepancyNotes)
        assertTrue(
            "Discrepancy notes must explain Upa-Sutra 1 vs Principal Sutra 6 clash",
            world7.discrepancyNotes!!.contains("Upa-Sutra #1") && world7.discrepancyNotes!!.contains("Principal Sutra #6")
        )
    }

    @Test
    fun alignedWorlds_areVerifiedAndComplete() {
        val alignedWorlds = VedicCurriculumManifest.getMainCampaignWorlds().filter { it.worldNumber != 7 }
        for (world in alignedWorlds) {
            assertTrue("World ${world.worldNumber} (${world.lessonId}) content must exist", world.verification.contentExists)
            assertTrue("World ${world.worldNumber} (${world.lessonId}) must be reachable at runtime", world.verification.isReachableAtRuntime)
            assertTrue("World ${world.worldNumber} (${world.lessonId}) math must be verified", world.verification.mathematicsVerified)
            assertTrue("World ${world.worldNumber} (${world.lessonId}) tutorial and quiz must align", world.verification.tutorialQuizAligned)
            assertTrue("World ${world.worldNumber} (${world.lessonId}) must be reviewed and complete", world.verification.isReviewedAndComplete)
        }
    }

    @Test
    fun syncWithCanonicalSutraModules_uiHomeSync() {
        assertEquals(
            "CanonicalSutraModules in HomeScreen must have exactly 16 modules",
            16,
            CanonicalSutraModules.size
        )

        for (module in CanonicalSutraModules) {
            val manifestEntry = VedicCurriculumManifest.getEntryByLessonId(module.id)
            assertNotNull(
                "Module id '${module.id}' from CanonicalSutraModules must be registered in manifest",
                manifestEntry
            )
            assertEquals(
                "World number for module '${module.id}' must match manifest",
                manifestEntry?.worldNumber,
                module.worldNumber
            )
        }
    }

    @Test
    fun syncWithUpaSutraRegistry_domainSync() {
        assertEquals(
            "UpaSutraRegistry must have 13 registered entries",
            13,
            UpaSutraRegistry.entries.size
        )

        val manifestUpaSutras = VedicCurriculumManifest.getUpaSutras()
        assertEquals(13, manifestUpaSutras.size)

        for (upaDefinition in UpaSutraRegistry.entries) {
            val matchingManifest = manifestUpaSutras.find {
                it.displayName.equals(upaDefinition.displayName, ignoreCase = true) ||
                it.canonicalSanskritName.replace("[^A-Za-z]".toRegex(), "").equals(
                    upaDefinition.displayName.replace("[^A-Za-z]".toRegex(), ""),
                    ignoreCase = true
                ) ||
                it.traditionalIndex == upaDefinition.id.ordinal + 1
            }
            assertNotNull(
                "UpaSutra '${upaDefinition.displayName}' must exist in manifest",
                matchingManifest
            )
        }
    }

    @Test
    fun runtimeTutorialContentReachable_all16WorldsHaveGurukulScripts() {
        for (worldNumber in 1..16) {
            val script = GurukulScriptsRepository.allScripts.values.firstOrNull { it.worldId == worldNumber }
            assertNotNull("GurukulScriptsRepository must have a script for World $worldNumber", script)
            assertTrue("Script for World $worldNumber must have dialogue beats", script!!.beats.isNotEmpty())
        }
    }
}
