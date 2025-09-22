# Daily Briefing — Android App Scaffold (v0.2, Fold-aware)

Now updated to support **Galaxy Z Fold 4 / foldables**. Key changes:
- Added **resizable widget configs** for both compact (cover screen) and expanded (inner display).
- Widget code adapts font sizes based on widget width.
- Added note on handling config changes for fold/unfold.
- Added **folder tree diagram** so you can visualize where all files go.

---

## 📂 Project Folder Tree
Here’s how the project should look inside `app/src/main/java/com/dailybriefing/` and `res/`.

```
DailyBriefing/
├── app/
│   ├── build.gradle
│   ├── proguard-rules.pro
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/com/dailybriefing/
│           │   ├── DailyBriefingApp.kt
│           │   ├── ui/
│           │   │   ├── MainActivity.kt
│           │   │   └── theme/
│           │   │       └── AppTheme.kt
│           │   ├── ui/widget/
│           │   │   └── WidgetCard.kt
│           │   ├── widget/
│           │   │   └── DailyBriefingWidget.kt
│           │   ├── data/
│           │   │   └── Models.kt
│           │   ├── alerts/
│           │   │   ├── AlertScheduler.kt
│           │   │   └── AlertService.kt
│           │   ├── system/
│           │   │   ├── BootReceiver.kt
│           │   │   └── ConfigChangeReceiver.kt
│           │   ├── repo/
│           │   │   └── Repositories.kt
│           │   ├── rules/
│           │   │   └── RuleEngine.kt
│           │   ├── work/
│           │   │   └── SyncWorker.kt
│           │   └── di/
│           │       └── AppModule.kt
│           └── res/
│               ├── layout/
│               │   └── widget_placeholder.xml
│               ├── mipmap-anydpi-v26/
│               │   └── ic_launcher.xml
│               ├── xml/
│               │   └── daily_briefing_widget_info.xml
│               └── values/ (colors.xml, themes.xml, etc.)
└── build.gradle (Project)
```

---

## Example Configs

```xml
<!-- FILE: app/src/main/res/xml/daily_briefing_widget_info.xml -->
<?xml version="1.0" encoding="utf-8"?>
<AppWidgetProviderInfo xmlns:android="http://schemas.android.com/apk/res/android"
    android:minWidth="180dp"
    android:minHeight="110dp"
    android:minResizeWidth="120dp"
    android:minResizeHeight="80dp"
    android:updatePeriodMillis="0"
    android:resizeMode="horizontal|vertical"
    android:widgetCategory="home_screen"
    android:initialLayout="@layout/widget_placeholder"
    android:minResizeWidth="250dp"
    android:minResizeHeight="160dp"
    android:previewLayout="@layout/widget_placeholder"
    android:initialLayoutSize="4x3" />
```

---

```kotlin
// FILE: app/src/main/java/com/dailybriefing/widget/DailyBriefingWidget.kt
// (unchanged, fold-aware version)
```

---

### Handling fold/unfold

```kotlin
// FILE: app/src/main/java/com/dailybriefing/system/ConfigChangeReceiver.kt
```

And register in Manifest:
```xml
<receiver android:name=".system.ConfigChangeReceiver" android:exported="false">
    <intent-filter>
        <action android:name="android.intent.action.CONFIGURATION_CHANGED" />
    </intent-filter>
</receiver>
```

---

✅ With this folder tree + headers in code, you should be able to paste each snippet into the right file without guessing.


---

## Project folder tree (copy‑paste guide)

```text
DailyBriefing/                                  # Android Studio project root (settings.gradle)
├─ settings.gradle
├─ build.gradle                                 # Project-level
├─ gradle/                                      # Gradle wrapper stuff
└─ app/
   ├─ build.gradle                              # Module-level
   ├─ proguard-rules.pro
   ├─ src/
   │  ├─ main/
   │  │  ├─ AndroidManifest.xml
   │  │  ├─ java/
   │  │  │  └─ com/dailybriefing/
   │  │  │     ├─ DailyBriefingApp.kt          // Hilt @HiltAndroidApp
   │  │  │     ├─ di/
   │  │  │     │  └─ AppModule.kt              // Provides repos, scheduler
   │  │  │     ├─ ui/
   │  │  │     │  ├─ MainActivity.kt           // Settings/permissions screen
   │  │  │     │  └─ theme/
   │  │  │     │     └─ AppTheme.kt
   │  │  │     ├─ ui/widget/
   │  │  │     │  └─ WidgetCard.kt             // Reusable Glance card
   │  │  │     ├─ widget/
   │  │  │     │  └─ DailyBriefingWidget.kt    // Glance widget (Fold-aware)
   │  │  │     ├─ data/
   │  │  │     │  └─ Models.kt                 // EventItem, Rule, DemoData
   │  │  │     ├─ repo/
   │  │  │     │  └─ Repositories.kt           // CalendarRepository*, SheetsRepository*
   │  │  │     ├─ rules/
   │  │  │     │  └─ RuleEngine.kt             // DSL eval + dedupe
   │  │  │     ├─ alerts/
   │  │  │     │  ├─ AlertScheduler.kt         // setExactAndAllowWhileIdle
   │  │  │     │  └─ AlertService.kt           // Foreground notif service
   │  │  │     └─ system/
   │  │  │        ├─ BootReceiver.kt           // Reschedule after reboot
   │  │  │        └─ ConfigChangeReceiver.kt   // Refresh on fold/unfold
   │  │  ├─ res/
   │  │  │  ├─ layout/
   │  │  │  │  └─ widget_placeholder.xml       // Install-time layout
   │  │  │  ├─ xml/
   │  │  │  │  └─ daily_briefing_widget_info.xml
   │  │  │  ├─ mipmap-anydpi-v26/              // App icons
   │  │  │  └─ values/                         // colors/strings if you add them
   │  │  └─ assets/                            // (optional) local JSON/rule samples
   │  └─ test/                                 // Unit tests (e.g., RuleEngine)
   └─ build/                                   # Generated
```

### Paste‑order checklist
1. **Gradle files** → `settings.gradle`, project `build.gradle`, then `app/build.gradle` → Sync.
2. **Manifest** + **res/xml** + **res/layout**.
3. Create package `com.dailybriefing` and subfolders per tree above; paste Kotlin files.
4. Run the app → place the widget → verify demo events + demo alert text.

### Where to drop the real integrations (when ready)
- **Calendar v3 client** → `repo/CalendarRepositoryGoogle.kt` (new file).
- **Sheets v4 client** → `repo/SheetsRepositoryGoogle.kt` (new file).
- Wire them in **`di/AppModule.kt`** (replace stubs with real providers).

---

## Stub files for Google API integration

```kotlin
// FILE: app/src/main/java/com/dailybriefing/repo/CalendarRepositoryGoogle.kt
package com.dailybriefing.repo

import com.dailybriefing.data.EventItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Replace TODOs with actual Google Calendar REST calls using an authenticated OkHttp/Retrofit client.
 * Requires OAuth token with scope: https://www.googleapis.com/auth/calendar.readonly
 */
class CalendarRepositoryGoogle(private val accessTokenProvider: suspend () -> String) : CalendarRepository {
    override suspend fun getTodayAndTomorrow(): List<EventItem> = withContext(Dispatchers.IO) {
        val token = accessTokenProvider()
        // TODO: Call https://www.googleapis.com/calendar/v3/calendars/primary/events
        // with timeMin= today 00:00, timeMax= tomorrow 23:59
        // Map items into EventItem models
        return@withContext emptyList<EventItem>()
    }
}
```

```kotlin
// FILE: app/src/main/java/com/dailybriefing/repo/SheetsRepositoryGoogle.kt
package com.dailybriefing.repo

import com.dailybriefing.data.Rule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Replace TODOs with actual Google Sheets REST call using an authenticated OkHttp/Retrofit client.
 * Requires OAuth token with scope: https://www.googleapis.com/auth/spreadsheets.readonly
 */
class SheetsRepositoryGoogle(private val accessTokenProvider: suspend () -> String) : SheetsRepository {
    override suspend fun getRules(): List<Rule> = withContext(Dispatchers.IO) {
        val token = accessTokenProvider()
        // TODO: Call https://sheets.googleapis.com/v4/spreadsheets/{spreadsheetId}/values/Rules!A:G
        // Map rows into Rule models
        return@withContext emptyList<Rule>()
    }
}
```

### Add-on Features (planned)

- **Quick Actions:** tap chips in widget or notification → “Snooze next alert 30m”, “Silence today”.
- **Quiet Hours:** global setting + per-rule overrides.
- **On-device summaries:** digest notification at 07:00 (e.g., “3 events today, 2 important emails”).
- **Export/Import rules:** CSV backup + restore via Google Sheets.
- **Accessibility:** toggle for large text mode on widget.

## Next steps
1. Add these two files under `app/src/main/java/com/dailybriefing/repo/`.
2. Update **`AppModule.kt`** to provide these instead of stubs once OAuth is working:
```kotlin
@Provides @Singleton
fun provideCalendarRepo(): CalendarRepository = CalendarRepositoryGoogle { getAccessToken() }

@Provides @Singleton
fun provideSheetsRepo(): SheetsRepository = SheetsRepositoryGoogle { getAccessToken() }
```
3. Implement `getAccessToken()` using Google Sign-In → retrieve an OAuth token for the scopes.

This way, once you wire up Google Sign-In, you only need to fill in the REST call details and the app will start pulling real Calendar + Sheet data.


---

## Add‑ons (v0.3): Quick Actions, Quiet Hours, 7:00 Digest, CSV Export/Import, Accessibility Toggle

### A) Quick Actions — Snooze 30m / Silence Today
```kotlin
// FILE: app/src/main/java/com/dailybriefing/widget/DailyBriefingWidget.kt  (add below content composables)
import android.content.Intent
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.ActionParameters
import com.dailybriefing.ui.MainActivity

private object Params { val Action = ActionParameters.Key<String>("action") }

enum class WidgetAction { SNOOZE_30M, SILENCE_TODAY }

@Composable
private fun QuickActionsRow() {
    Row {
        ActionChip("Snooze 30m", WidgetAction.SNOOZE_30M)
        Spacer(GlanceModifier.width(6.dp))
        ActionChip("Silence today", WidgetAction.SILENCE_TODAY)
    }
}

@Composable
private fun ActionChip(label: String, action: WidgetAction) {
    androidx.glance.appwidget.lazy.LazyRow { }
    Text(label, modifier = GlanceModifier
        .background(0xFFEAEAEA.toInt()).cornerRadius(12.dp).padding(8.dp)
        .clickable(actionRunCallback<WidgetActionHandler>(
            ActionParameters.Builder().set(Params.Action, action.name).build()
        )))
}

class WidgetActionHandler : ActionCallback {
    override suspend fun onAction(context: android.content.Context, glanceId: GlanceId, parameters: ActionParameters) {
        when (WidgetAction.valueOf(parameters[Params.Action]!!)) {
            WidgetAction.SNOOZE_30M -> com.dailybriefing.alerts.SnoozeManager.snoozeNext(context, minutes = 30)
            WidgetAction.SILENCE_TODAY -> com.dailybriefing.alerts.SnoozeManager.silenceToday(context)
        }
        DailyBriefingWidget().updateAll(context)
    }
}
```

```kotlin
// FILE: app/src/main/java/com/dailybriefing/alerts/SnoozeManager.kt
package com.dailybriefing.alerts

import android.content.Context
import java.util.Calendar

object SnoozeManager {
    fun snoozeNext(context: Context, minutes: Int) {
        // TODO: persist a single upcoming alert override (+minutes) in Room/Prefs and reschedule
    }
    fun silenceToday(context: Context) {
        // TODO: mark quiet-hours override for the remainder of today and cancel remaining alarm notifications
    }
}
```

> In `WidgetRoot()` add `QuickActionsRow()` under the Next Alert section in compact/wide/expanded content.

---

### B) Quiet Hours + per‑rule overrides
Extend the model and engine:
```kotlin
// FILE: app/src/main/java/com/dailybriefing/data/Models.kt  (augment Rule)
data class Rule(
    val id: String,
    val active: Boolean,
    val timeExpr: String,
    val condition: String,
    val priority: String = "normal",
    val title: String,
    val body: String,
    val quietStart: String? = null,   // e.g., "22:00"
    val quietEnd: String? = null,     // e.g., "07:30"
    val overrideAllow: Boolean = true // allow rule to fire during quiet hours when explicitly enabled
)
```

```kotlin
// FILE: app/src/main/java/com/dailybriefing/rules/RuleEngine.kt  (filter during eval)
object RuleEngine {
    // ...
    private fun withinQuietHours(rule: Rule, nowEpoch: Long): Boolean {
        // TODO: implement local-time window check quietStart..quietEnd across midnight
        return false
    }
}
```

Add user‑level quiet hours (Settings):
```kotlin
// FILE: app/src/main/java/com/dailybriefing/ui/MainActivity.kt  (settings toggles)
var quietStart by remember { mutableStateOf("22:00") }
var quietEnd by remember { mutableStateOf("07:00") }
// Persist to DataStore and consult in RuleEngine as a global gate
```

---

### C) On‑device 7:00 digest notification
```kotlin
// FILE: app/src/main/java/com/dailybriefing/work/MorningDigestWorker.kt
package com.dailybriefing.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dailybriefing.repo.CalendarRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@HiltWorker
class MorningDigestWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val calendar: CalendarRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val events = calendar.getTodayAndTomorrow().filter { /* today only */ true }
        val count = events.size
        com.dailybriefing.notifications.Notifier.summary(
            applicationContext,
            title = "Daily Briefing",
            body = "${count} events today"
        )
        return Result.success()
    }
}
```

```kotlin
// FILE: app/src/main/java/com/dailybriefing/notifications/Notifier.kt
package com.dailybriefing.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dailybriefing.R

object Notifier {
    private const val DIGEST_CH = "digest"
    fun summary(ctx: Context, title: String, body: String) {
        if (Build.VERSION.SDK_INT >= 26) {
            ctx.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(NotificationChannel(DIGEST_CH, "Daily Summary", NotificationManager.IMPORTANCE_DEFAULT))
        }
        val n = NotificationCompat.Builder(ctx, DIGEST_CH)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .build()
        NotificationManagerCompat.from(ctx).notify(7, n)
    }
}
```

Schedule at exactly 07:00 local:
```kotlin
// FILE: app/src/main/java/com/dailybriefing/di/AppModule.kt  (on app start or first run)
// Enqueue a unique periodic/one-off exact alarm at 07:00 using AlarmManager or WorkManager with initial delay.
```

---

### D) Export/Import rules — CSV to/from Sheets
```kotlin
// FILE: app/src/main/java/com/dailybriefing/repo/SheetsRepositoryGoogle.kt  (add helpers)
suspend fun exportRulesCsv(csv: String): Boolean {
    // TODO: write CSV to an Apps Script web app endpoint or a specific Sheet tab via Sheets batchUpdate
    return false
}

suspend fun importRulesCsv(): String {
    // TODO: fetch Rules!A:G, convert to CSV string
    return "id,active,time_expr,condition,priority,title,body,quietStart,quietEnd
    "
}
```

Add Settings buttons:
```kotlin
// FILE: app/src/main/java/com/dailybriefing/ui/MainActivity.kt  (in SettingsScreen)
Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
    OutlinedButton(onClick = { /* call sheets.importRulesCsv() -> save to Downloads */ }) { Text("Import from Sheets") }
    OutlinedButton(onClick = { /* read local CSV -> sheets.exportRulesCsv(csv) */ }) { Text("Export to Sheets") }
}
```

---

### E) Accessibility — Large Text mode toggle (widget)
```kotlin
// FILE: app/src/main/java/com/dailybriefing/ui/MainActivity.kt  (setting)
var largeText by remember { mutableStateOf(false) } // persist via DataStore
Switch(checked = largeText, onCheckedChange = { /* save + request widget update */ })
Text("Large text in widget")
```

```kotlin
// FILE: app/src/main/java/com/dailybriefing/widget/DailyBriefingWidget.kt  (apply)
@Composable
private fun fontSize(base: Int, large: Boolean): Sp = Sp((if (large) base + 2 else base).toFloat())
// Use: TextStyle(fontSize = fontSize(14, largeTextEnabled))
```

```kotlin
// FILE: app/src/main/java/com/dailybriefing/widget/DailyBriefingWidget.kt  (read flag)
// Read DataStore flag and recompute sizes; trigger update on change
```

---

### F) Manifest additions
```xml
<!-- FILE: app/src/main/AndroidManifest.xml  (ensure receivers/services registered) -->
<receiver android:name=".system.ConfigChangeReceiver" android:exported="false">
    <intent-filter>
        <action android:name="android.intent.action.CONFIGURATION_CHANGED" />
    </intent-filter>
</receiver>
```

> These stubs wire the surfaces and flows. You can now fill the TODOs (Room/DataStore persistence, exact scheduling, Sheets/Calendar calls) and the widget will support your requested add‑ons without major refactors.
