package com.mytimer.app

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.RemoteViews

class DayTimerWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) updateWidget(context, appWidgetManager, id)
    }

    override fun onEnabled(context: Context) {
        DayTimerMidnightScheduler.schedule(context)
    }

    override fun onDisabled(context: Context) {
        DayTimerMidnightScheduler.cancel(context)
    }

    companion object {
        // Vibrant palette cycling across digits
        private val DIGIT_COLORS = intArrayOf(
            Color.parseColor("#FF6B6B"), // coral red
            Color.parseColor("#FFD93D"), // golden yellow
            Color.parseColor("#6BCB77"), // mint green
            Color.parseColor("#4D96FF"), // sky blue
            Color.parseColor("#FF922B"), // orange
        )

        fun updateAll(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, DayTimerWidgetProvider::class.java))
            for (id in ids) updateWidget(context, manager, id)
        }

        private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_day_timer)
            val seconds = DayTimer.secondsRemainingToday().toString()

            // Build multi-colored spannable
            val spannable = SpannableString(seconds)
            seconds.forEachIndexed { i, _ ->
                spannable.setSpan(
                    ForegroundColorSpan(DIGIT_COLORS[i % DIGIT_COLORS.size]),
                    i, i + 1,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            views.setTextViewText(R.id.widgetSeconds, spannable)

            val openApp = PendingIntent.getActivity(
                context, appWidgetId,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widgetRoot, openApp)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
