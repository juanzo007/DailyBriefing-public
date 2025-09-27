package com.dailybriefing.widget

import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.RemoteViewsService.RemoteViewsFactory
import com.dailybriefing.R
import com.dailybriefing.model.BriefItem

class BriefWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory = Factory(this)
}

private class Factory(private val svc: BriefWidgetService) :
    RemoteViewsService.RemoteViewsFactory {

    private var items: List<BriefItem> = emptyList()

    override fun onCreate() {}
    override fun onDestroy() {}

    override fun onDataSetChanged() {
        items = WidgetUpdater.getLatest()
    }

    override fun getCount(): Int = items.size

    override fun getViewAt(position: Int): RemoteViews {
        val item = items.getOrNull(position)
            ?: return RemoteViews(svc.packageName, R.layout.widget_item)

        val rv = RemoteViews(svc.packageName, R.layout.widget_item)
        rv.setTextViewText(R.id.itemTitle, item.title)
        rv.setTextViewText(
            R.id.itemTime,
            android.text.format.DateFormat.format("EEE h:mm a", item.time)
        )
        rv.setTextViewText(R.id.itemSource, item.source)

        if (!item.notes.isNullOrBlank()) {
            rv.setViewVisibility(R.id.itemNotes, android.view.View.VISIBLE)
            rv.setTextViewText(R.id.itemNotes, item.notes)
        } else {
            rv.setViewVisibility(R.id.itemNotes, android.view.View.GONE)
        }

        return rv
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = true
}
