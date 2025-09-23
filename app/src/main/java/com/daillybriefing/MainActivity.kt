package com.daillybriefing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daillybriefing.worker.BriefScheduler
import com.daillybriefing.util.NotificationHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DailyBriefingApp() }

        BriefScheduler.schedule(this)
        // Optional demo notification:
        // NotificationHelper.showBriefNotification(this, "Daily Briefing", "Your update is ready")
    }
}

@Composable
fun DailyBriefingApp() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Daily Briefing", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(12.dp))
                    Text("Phase 1 scaffold running.")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDailyBriefing() {
    DailyBriefingApp()
}
