package com.dailybriefing

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dailybriefing.worker.BriefRefreshWorker

class MainActivity : AppCompatActivity() {

    // Request READ_CALENDAR at runtime (Android 13+ / 15)
    private val requestCalendarPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                // ✅ Update 2: trigger a refresh right after permission is granted
                BriefRefreshWorker.enqueueNow(this)
                // Optional: keep things fresh while testing
                BriefRefreshWorker.schedulePeriodic(this, repeatMinutes = 30)
                Toast.makeText(this, "Calendar access granted. Refreshing…", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Calendar permission is required to show events.", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ensureCalendarPermissionAndRefresh()

        // ✅ Update 3 (testing convenience):
        // Provide a manual "Refresh now" action from the menu (see onOptionsItemSelected).
        // You can also uncomment the next line to force a refresh every time you open the app:
        // BriefRefreshWorker.enqueueNow(this)
    }

    private fun ensureCalendarPermissionAndRefresh() {
        val granted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            // If already granted, refresh immediately
            BriefRefreshWorker.enqueueNow(this)
            BriefRefreshWorker.schedulePeriodic(this, repeatMinutes = 30)
        } else {
            requestCalendarPermission.launch(Manifest.permission.READ_CALENDAR)
        }
    }

    // ===== Menu: adds a "Refresh now" item while testing =====
    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                BriefRefreshWorker.enqueueNow(this)
                Toast.makeText(this, "Refreshing…", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
