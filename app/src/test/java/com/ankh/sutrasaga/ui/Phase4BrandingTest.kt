package com.ankh.sutrasaga.ui

import com.ankh.sutrasaga.ui.feedback.VedicFeedbackEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class Phase4BrandingTest {

    @Test
    fun testVedicFeedbackEventsEnum() {
        assertEquals(4, VedicFeedbackEvent.entries.size)
        assertNotNull(VedicFeedbackEvent.NUMPAD_TAP)
        assertNotNull(VedicFeedbackEvent.CORRECT_DIGIT)
        assertNotNull(VedicFeedbackEvent.INCORRECT_STEP)
        assertNotNull(VedicFeedbackEvent.SUTRA_MASTERED)
    }
}
