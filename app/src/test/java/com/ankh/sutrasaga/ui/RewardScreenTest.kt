package com.ankh.sutrasaga.ui

import com.ankh.sutrasaga.ui.screens.worldCompletionContent
import org.junit.Assert.assertEquals
import org.junit.Test

class RewardScreenTest {

    @Test
    fun rewardContentMatchesTheCompletedWorld() {
        assertEquals("WORLD 1 COMPLETE!", worldCompletionContent(1).title)
        assertEquals("WORLD 2 COMPLETE!", worldCompletionContent(2).title)
        assertEquals("WORLD 3 COMPLETE!", worldCompletionContent(3).title)
        assertEquals(
            "You have mastered Nikhilam Navatashcaramam Dashatah!",
            worldCompletionContent(2).masteryMessage
        )
    }
}
