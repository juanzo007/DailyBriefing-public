package com.dailybriefing.worker

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dailybriefing.notify.Notify
import com.dailybriefing.widget.BriefWidgetProvider
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class BriefRefreshWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        // Stub data — replace with real sources later
        val time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        val text = "Good morning — (stub) 3 events • 2 emails @ $time"

        // 1) Show notification
        Notify.show(applicationContext, "Daily Briefing", text)

        // 2) Store summary for widget
        applicationContext.getSharedPreferences("brief", Context.MODE_PRIVATE)
            .edit()
            .putString("summary", text)
            .apply()

        // 3) Nudge the widget to refresh
        val manager = AppWidgetManager.getInstance(applicationContext)
        val ids = manager.getAppWidgetIds(
            ComponentName(applicationContext, BriefWidgetProvider::class.java)
        )
        val intent = Intent(applicationContext, BriefWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        }
        applicationContext.sendBroadcast(intent)

        return Result.success()
    }
}
