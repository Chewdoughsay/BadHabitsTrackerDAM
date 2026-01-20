package com.example.badhabitstrackerdam.notifications

import android.content.Context
import androidx.work.*
import java.util.Calendar
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    private const val REMINDER_WORK_NAME = "daily_habit_reminder"

    /**
     * Programează o notificare zilnică la ora specificată
     * @param context Context-ul aplicației
     * @param hour Ora la care să trimită notificarea (0-23)
     * @param minute Minutul la care să trimită notificarea (0-59)
     */
    fun scheduleDailyReminder(context: Context, hour: Int = 9, minute: Int = 0) {
        // Calculăm delay-ul până la următoarea oră de notificare
        val currentTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)

            // Dacă ora țintă a trecut deja astăzi, programăm pentru mâine
            if (before(currentTime)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val initialDelay = targetTime.timeInMillis - currentTime.timeInMillis

        // Creăm PeriodicWorkRequest (se repetă la fiecare 24 ore)
        val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true) // Optional: doar când bateria nu e scăzută
                    .build()
            )
            .build()

        // Programăm work-ul (dacă există deja unul cu același nume, îl înlocuiește)
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            dailyWorkRequest
        )
    }

    /**
     * Anulează notificările programate
     */
    fun cancelDailyReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(REMINDER_WORK_NAME)
    }
}