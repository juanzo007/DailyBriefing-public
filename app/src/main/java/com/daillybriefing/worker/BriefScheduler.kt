package com.daillybriefing.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object BriefScheduler {
    private const val UNIQUE_NAME = "brief_refresh"

    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<BriefRefreshWorker>(
            15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}