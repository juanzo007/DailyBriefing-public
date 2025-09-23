package com.dailybriefing.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

object BriefScheduler {
    private const val UNIQUE_NAME = "daily_briefing_05"

    fun scheduleDailyAt5am(context: Context) {
        val now = LocalDateTime.now()
        val runAtToday = LocalDateTime.of(LocalDate.now(), LocalTime.of(5, 0))
        val firstRun = if (now.isBefore(runAtToday)) runAtToday else runAtToday.plusDays(1)
        val initialDelay = Duration.between(now, firstRun)

        val request = PeriodicWorkRequestBuilder<BriefRefreshWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, // 👈 ensure new delay takes effect
            request
        )
    }
}
