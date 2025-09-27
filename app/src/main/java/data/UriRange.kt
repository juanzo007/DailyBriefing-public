package com.dailybriefing.data

import android.content.ContentUris
import android.net.Uri

/** Top-level function form (appendRange(builder, start, end)) */
fun appendRange(builder: Uri.Builder, startMillis: Long, endMillis: Long): Uri.Builder {
    ContentUris.appendId(builder, startMillis)
    ContentUris.appendId(builder, endMillis)
    return builder
}

/** Extension form (builder.appendRange(start, end)) */
fun Uri.Builder.appendRange(startMillis: Long, endMillis: Long): Uri.Builder {
    ContentUris.appendId(this, startMillis)
    ContentUris.appendId(this, endMillis)
    return this
}