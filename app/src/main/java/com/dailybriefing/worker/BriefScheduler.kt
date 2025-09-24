package com.dailybriefing.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.math.max

object BriefScheduler {
    private const val UNIQUE_NAME = "DailyBriefingRefresh"

    /** Schedule periodic job every [repeatHours] hours (default 24). */
    fun scheduleDaily(context: Context, repeatHours: Long = 24) {
        val request = PeriodicWorkRequestBuilder<BriefRefreshWorker>(
            repeatHours, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    /** Schedule to start at **5:00 AM local time**, then repeat every 24h. */
    fun scheduleDailyAt5am(context: Context) {
        val now = Calendar.getInstance()
        val first = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 5)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
        }
        val initialDelay = max(0, first.timeInMillis - now.timeInMillis)

        val request = PeriodicWorkRequestBuilder<BriefRefreshWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}
