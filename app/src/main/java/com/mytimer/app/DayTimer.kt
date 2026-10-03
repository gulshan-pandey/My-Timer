package com.mytimer.app

import android.os.SystemClock
import java.util.Calendar
object DayTimer {
    const val SECONDS_PER_DAY = 86_400L

    fun endOfTodayMillis(): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun secondsRemainingToday(): Long {
        val remainingMs = endOfTodayMillis() - System.currentTimeMillis()
        return (remainingMs / 1000).coerceAtLeast(0)
    }

    /** Chronometer base for countdown mode (remaining time ticks down automatically). */
    fun chronometerBase(): Long {
        val remainingMs = endOfTodayMillis() - System.currentTimeMillis()
        return SystemClock.elapsedRealtime() + remainingMs.coerceAtLeast(0)
    }

    fun formatSeconds(totalSeconds: Long): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun formatSecondsWithLabel(totalSeconds: Long): String {
        return "${formatSeconds(totalSeconds)} left today"
    }

    fun millisUntilMidnight(): Long {
        return (endOfTodayMillis() - System.currentTimeMillis()).coerceAtLeast(0)
    }

    fun secondsSinceMidnight(): Long {
        return SECONDS_PER_DAY - secondsRemainingToday()
    }

    fun progressFractionToday(): Float {
        return secondsSinceMidnight().toFloat() / SECONDS_PER_DAY.toFloat()
    }
}
