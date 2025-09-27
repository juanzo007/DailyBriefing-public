package com.dailybriefing.model

/**
 * Represents a single item shown in the Daily Briefing widget.
 *
 * @param title  Main text for the item (e.g., event title, email subject).
 * @param time   Epoch millis (used for formatting the time string).
 * @param source Where this item came from (e.g., "Google Calendar").
 * @param notes  Optional extra info or description.
 */
data class BriefItem(
    val title: String,
    val time: Long,
    val source: String,
    val notes: String? = null
)
