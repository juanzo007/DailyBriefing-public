package com.dailybriefing

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.dailybriefing.notify.Notify

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(
                Notify.CHANNEL_ID,
                "Daily Briefing",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Daily 05:00 briefing notifications" }
            getSystemService(NotificationManager::class.java).createNotificationChannel(ch)
        }
    }
}
