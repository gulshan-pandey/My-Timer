package com.mytimer.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var secondsText: TextView
    private lateinit var deviceSecondsValue: TextView
    private lateinit var deviceFreeValue: TextView

    // Tracks seconds the app has been open today (proxy for "on device")
    private var deviceSecondsToday = 0L
    private val handler = Handler(Looper.getMainLooper())

    private val tickRunnable = object : Runnable {
        override fun run() {
            val remaining = DayTimer.secondsRemainingToday()
            secondsText.text = remaining.toString()
            deviceSecondsToday++
            deviceSecondsValue.text = deviceSecondsToday.toString()
            deviceFreeValue.text = (remaining - deviceSecondsToday).coerceAtLeast(0).toString()
            handler.postDelayed(this, 1000L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        secondsText = findViewById(R.id.secondsRemaining)
        deviceSecondsValue = findViewById(R.id.deviceSecondsValue)
        deviceFreeValue = findViewById(R.id.deviceFreeValue)

        requestNotificationPermissionIfNeeded()
        DayTimerForegroundService.start(this)
        DayTimerMidnightScheduler.schedule(this)
    }

    override fun onResume() {
        super.onResume()
        secondsText.text = DayTimer.secondsRemainingToday().toString()
        handler.post(tickRunnable)
    }

    override fun onPause() {
        handler.removeCallbacks(tickRunnable)
        super.onPause()
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED) return
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
    }
}
