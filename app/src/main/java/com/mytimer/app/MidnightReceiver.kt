package com.mytimer.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MidnightReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        DayTimerWidgetProvider.updateAll(context)
        DayTimerForegroundService.start(context)
        DayTimerMidnightScheduler.schedule(context)
    }
}
