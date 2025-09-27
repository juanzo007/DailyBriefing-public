package com.dailybriefing.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.dailybriefing.MainActivity
import com.dailybriefing.R

class DailyBriefingAppWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { appWidgetId ->
            val views = RemoteViews(context.packageName, R.layout.widget_daily_briefing)

            // Hook the ListView in the widget to the RemoteViewsService
            views.setRemoteAdapter(R.id.brief_list, Intent(context, BriefWidgetService::class.java))
            views.setEmptyView(R.id.brief_list, R.id.empty_view)

            // Header tap → open the app
            val launchIntent = Intent(context, MainActivity::class.java)
            val pending = PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.header, pending)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    // Optional: ensure a manual broadcast update also redraws correctly
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            val mgr = AppWidgetManager.getInstance(context)
            val ids = mgr.getAppWidgetIds(
                android.content.ComponentName(context, DailyBriefingAppWidget::class.java)
            )
            if (ids.isNotEmpty()) {
                onUpdate(context, mgr, ids)
            }
        }
    }
}
