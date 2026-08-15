package com.ankh.sutrasaga.data

import com.ankh.sutrasaga.data.repository.GurukulContentRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GurukulContentRepositoryTest {

    @Test
    fun world2UsesItsOwnStoryAndTutorial() {
        val story = GurukulContentRepository.getStoryScript(2)
        val tutorial = GurukulContentRepository.getTutorialScript(2)

        assertEquals(2, story.worldId)
        assertEquals(2, tutorial.worldId)
        assertTrue(tutorial.title.contains("Nikhilam"))
        assertTrue(tutorial.subtitle.contains("100 - 37 = 63"))
    }

    @Test
    fun world3UsesItsOwnStoryAndTutorial() {
        val story = GurukulContentRepository.getStoryScript(3)
        val tutorial = GurukulContentRepository.getTutorialScript(3)

        assertEquals(3, story.worldId)
        assertEquals(3, tutorial.worldId)
        assertTrue(tutorial.title.contains("Ekanyunena"))
        assertTrue(tutorial.subtitle.contains("47 x 99 = 4653"))
    }
}
