package com.example.badhabitstrackerdam.utils

import android.util.Log
import com.example.badhabitstrackerdam.model.local.CheckInEntity
import com.example.badhabitstrackerdam.model.local.HabitEntity
import com.example.badhabitstrackerdam.model.repository.HabitRepository
import kotlinx.coroutines.delay
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random

class TestDataPopulator @Inject constructor(
    private val repository: HabitRepository
) {

    suspend fun populateTestData() {
        Log.d("TestDataPopulator", "Starting test data population...")

        // Sample habits with varying progress
        val testHabits = listOf(
            Triple(
                HabitEntity(
                    title = "🚭 No Smoking",
                    description = "Quit smoking for a healthier life",
                    goalDays = 30,
                    startTimestamp = System.currentTimeMillis() - (15 * 24 * 60 * 60 * 1000L)
                ),
                12, // 12 check-ins
                30  // out of 30 days
            ),
            Triple(
                HabitEntity(
                    title = "💪 Morning Workout",
                    description = "Exercise for at least 30 minutes every morning",
                    goalDays = 21,
                    startTimestamp = System.currentTimeMillis() - (10 * 24 * 60 * 60 * 1000L)
                ),
                7,  // 7 check-ins
                21  // out of 21 days
            ),
            Triple(
                HabitEntity(
                    title = "📱 No Social Media",
                    description = "Stay away from social media to increase productivity",
                    goalDays = 14,
                    startTimestamp = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)
                ),
                10, // 10 check-ins (almost done!)
                14  // out of 14 days
            ),
            Triple(
                HabitEntity(
                    title = "📚 Read Daily",
                    description = "Read at least 20 pages of a book every day",
                    goalDays = 60,
                    startTimestamp = System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000L)
                ),
                3,  // 3 check-ins
                60  // out of 60 days (just started)
            ),
            Triple(
                HabitEntity(
                    title = "🧘 Meditation",
                    description = "Meditate for 10 minutes every morning",
                    goalDays = 90,
                    startTimestamp = System.currentTimeMillis() - (20 * 24 * 60 * 60 * 1000L)
                ),
                15, // 15 check-ins
                90  // out of 90 days
            )
        )

        // Insert each habit with its check-ins
        testHabits.forEachIndexed { index, (habit, numCheckIns, maxDaysAgo) ->
            try {
                Log.d("TestDataPopulator", "Inserting habit ${index + 1}: ${habit.title}")

                // Insert habit and get the REAL ID
                val insertedId = repository.insertHabit(habit)
                Log.d("TestDataPopulator", "Habit inserted with ID: $insertedId")

                // Wait a bit to ensure insertion completes
                delay(200)

                // Generate random check-ins for this habit using the REAL ID
                generateRandomCheckIns(insertedId.toInt(), numCheckIns, maxDaysAgo)
                Log.d("TestDataPopulator", "Generated $numCheckIns check-ins for habit $insertedId")

                // Small delay between habits
                delay(200)
            } catch (e: Exception) {
                Log.e("TestDataPopulator", "Error inserting habit ${index + 1}", e)
            }
        }

        Log.d("TestDataPopulator", "Test data population completed!")
    }

    private suspend fun generateRandomCheckIns(habitId: Int, numCheckIns: Int, maxDaysAgo: Int) {
        val usedDays = mutableSetOf<Int>()

        repeat(numCheckIns) {
            try {
                // Generate a random unique day offset
                var daysAgo: Int
                do {
                    daysAgo = Random.nextInt(0, maxDaysAgo.coerceAtMost(20))
                } while (usedDays.contains(daysAgo))

                usedDays.add(daysAgo)

                // Calculate timestamp for that day
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_MONTH, -daysAgo)
                calendar.set(Calendar.HOUR_OF_DAY, Random.nextInt(8, 22))
                calendar.set(Calendar.MINUTE, Random.nextInt(0, 60))
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                val checkIn = CheckInEntity(
                    habitId = habitId,
                    timestamp = calendar.timeInMillis
                )

                // Insert directly (bypasses the "already checked in today" validation)
                repository.insertCheckInDirect(checkIn)

                Log.d("TestDataPopulator", "Check-in created for habit $habitId, $daysAgo days ago")
            } catch (e: Exception) {
                Log.e("TestDataPopulator", "Error creating check-in for habit $habitId", e)
            }
        }
    }
}