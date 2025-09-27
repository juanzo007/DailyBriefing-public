package com.dailybriefing.data

import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import com.dailybriefing.model.BriefItem
import java.util.concurrent.TimeUnit
import kotlin.math.min

object CalendarRepository {

    /**
     * Returns upcoming calendar instances (recurrences expanded) within [windowHours].
     * Uses Instances table so it works for recurring/all-day events.
     */
    fun getUpcomingEvents(
        ctx: Context,
        limit: Int = 8,
        windowHours: Long = 72
    ): List<BriefItem> {
        val now = System.currentTimeMillis()
        val end = now + TimeUnit.HOURS.toMillis(windowHours)

        // Instances require a special "when" URI with start/end appended.
        val builder: Uri.Builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        CalendarContract.Instances.appendRange(builder, now, end)
        val uri = builder.build()

        val projection = arrayOf(
            CalendarContract.Instances.TITLE,                 // 0
            CalendarContract.Instances.BEGIN,                 // 1
            CalendarContract.Instances.ALL_DAY,               // 2
            CalendarContract.Instances.CALENDAR_DISPLAY_NAME  // 3
        )

        val sortOrder = "${CalendarContract.Instances.BEGIN} ASC"

        val items = mutableListOf<BriefItem>()

        ctx.contentResolver.query(
            uri,
            projection,
            null,
            null,
            sortOrder
        )?.use { c ->
            val titleIdx = 0
            val beginIdx = 1
            val allDayIdx = 2
            val calNameIdx = 3

            var count = 0
            while (c.moveToNext() && count < limit) {
                val title = c.getString(titleIdx) ?: "(No title)"
                val begin = c.getLong(beginIdx)
                val isAllDay = c.getInt(allDayIdx) == 1
                val calName = c.getString(calNameIdx) ?: "Calendar"

                // For all-day, Instances.BEGIN is midnight device-tz; leave as-is for now
                items.add(
                    BriefItem(
                        title = title,
                        time = begin,
                        source = calName,
                        notes = if (isAllDay) "All-day" else null
                    )
                )
                count++
            }
        }

        return items
    }
}
