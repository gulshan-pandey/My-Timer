package com.mytimer.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
        findViewById<View>(R.id.developerInfoButton).setOnClickListener {
            showDeveloperInfo()
        }

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

    private fun showDeveloperInfo() {
        val content = layoutInflater.inflate(R.layout.dialog_developer_info, null)
        val illustration = content.findViewById<ImageView>(R.id.developerAnimeImage)
        val illustrationId = resources.getIdentifier("developer_anime", "drawable", packageName)
        if (illustrationId != 0) {
            illustration.setImageResource(illustrationId)
            illustration.visibility = View.VISIBLE
        }

        val dialog = AlertDialog.Builder(this)
            .setView(content)
            .setNegativeButton(R.string.close, null)
            .create()
        content.findViewById<View>(R.id.githubLinkButton).setOnClickListener {
            dialog.dismiss()
            openProfile(getString(R.string.github_profile_url))
        }
        content.findViewById<View>(R.id.linkedinLinkButton).setOnClickListener {
            dialog.dismiss()
            openProfile(getString(R.string.linkedin_profile_url))
        }

        dialog.show()
    }

    private fun openProfile(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
