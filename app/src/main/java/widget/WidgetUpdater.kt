package com.dailybriefing.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.dailybriefing.R
import com.dailybriefing.model.BriefItem

object WidgetUpdater {
    @Volatile private var latest: List<BriefItem> = emptyList()
    fun getLatest(): List<BriefItem> = latest

    fun update(ctx: Context, items: List<BriefItem>) {
        latest = items

        val mgr = AppWidgetManager.getInstance(ctx)
        val provider = ComponentName(ctx, DailyBriefingAppWidget::class.java)
        val ids = mgr.getAppWidgetIds(provider)

        // Notify collection view if present
        runCatching {
            mgr.notifyAppWidgetViewDataChanged(ids, R.id.brief_list)
        }.onFailure {
            // Fallback: broadcast a generic update
            val intent = Intent(ctx, DailyBriefingAppWidget::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            }
            ctx.sendBroadcast(intent)
        }
    }
}
