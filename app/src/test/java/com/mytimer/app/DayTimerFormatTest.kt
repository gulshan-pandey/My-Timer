package com.mytimer.app

import org.junit.Assert.assertEquals
import org.junit.Test

class DayTimerFormatTest {

    @Test
    fun formatSeconds_midnight() {
        assertEquals("00:00:00", DayTimer.formatSeconds(0))
    }

    @Test
    fun formatSeconds_oneDay() {
        assertEquals("24:00:00", DayTimer.formatSeconds(DayTimer.SECONDS_PER_DAY))
    }

    @Test
    fun formatSeconds_sample() {
        assertEquals("23:59:59", DayTimer.formatSeconds(86_399))
        assertEquals("01:01:01", DayTimer.formatSeconds(3_661))
    }
}
