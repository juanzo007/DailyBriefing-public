package com.dailybriefing.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.dailybriefing.data.CalendarRepository
import com.dailybriefing.widget.WidgetUpdater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * Fetches upcoming calendar events and pushes them to the widget via WidgetUpdater.
 * Safe on Android 13+ / 15: if READ_CALENDAR isn't granted, it quietly posts an empty list.
 */
class BriefRefreshWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val ctx = applicationContext

            val hasPermission =
                ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_CALENDAR) ==
                        PackageManager.PERMISSION_GRANTED

            val items = if (hasPermission) {
                // Adjust the limit to taste
                CalendarRepository.getUpcomingEvents(ctx, limit = 8)
            } else {
                emptyList()
            }

            // Update the widget list (will trigger notify or broadcast fallback)
            WidgetUpdater.update(ctx, items)

            Result.success()
        } catch (t: Throwable) {
            // Don't crash the app—log in real project; for now treat as a retryable failure
            Result.retry()
        }
    }

    companion object {
        private const val UNIQUE_ONE_TIME = "brief-refresh-now"
        private const val UNIQUE_PERIODIC = "brief-refresh-periodic"

        /** Enqueue a one-off refresh immediately. */
        fun enqueueNow(context: Context) {
            val req = OneTimeWorkRequestBuilder<BriefRefreshWorker>().build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                UNIQUE_ONE_TIME,
                ExistingWorkPolicy.REPLACE,
                req
            )
        }

        /**
         * Schedule periodic updates (min 15 min on Android).
         * You can call this once (e.g., after first launch or after permission granted).
         */
        fun schedulePeriodic(context: Context, repeatMinutes: Long = 15L) {
            val req = PeriodicWorkRequestBuilder<BriefRefreshWorker>(
                repeatMinutes, TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_PERIODIC,
                ExistingPeriodicWorkPolicy.UPDATE,
                req
            )
        }
    }
}
