package com.example.badhabitstrackerdam

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.badhabitstrackerdam.notifications.ReminderScheduler
import com.example.badhabitstrackerdam.view.navigation.AppNavigation
import com.example.badhabitstrackerdam.view.theme.BadHabitsTrackerDAMTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Launcher pentru cererea permisiunii de notificări
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permisiune acordată - programăm notificările
            scheduleNotifications()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cerem permisiunea și programăm notificările
        requestNotificationPermissionAndSchedule()

        setContent {
            BadHabitsTrackerDAMTheme {
                AppNavigation()
            }
        }
    }

    private fun requestNotificationPermissionAndSchedule() {
        // Pentru Android 13+ (API 33+), trebuie să cerem permisiune explicit
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permisiune deja acordată
                    scheduleNotifications()
                }
                else -> {
                    // Cerem permisiunea
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // Pentru versiuni mai vechi, permisiunea e acordată automat
            scheduleNotifications()
        }
    }

    private fun scheduleNotifications() {
        // Programăm notificări zilnice la ora 9:00 AM
        ReminderScheduler.scheduleDailyReminder(
            context = this,
            hour = 9,
            minute = 0
        )
    }
}