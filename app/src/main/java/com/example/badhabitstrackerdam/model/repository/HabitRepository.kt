package com.example.badhabitstrackerdam.model.repository

import com.example.badhabitstrackerdam.model.local.CheckInDao
import com.example.badhabitstrackerdam.model.local.CheckInEntity
import com.example.badhabitstrackerdam.model.local.HabitDao
import com.example.badhabitstrackerdam.model.local.HabitEntity
import com.example.badhabitstrackerdam.model.remote.QuoteDto
import com.example.badhabitstrackerdam.model.remote.ZenQuotesApi
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject

class HabitRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val checkInDao: CheckInDao,
    private val api: ZenQuotesApi
) {
    // --- Habits ---
    fun getAllHabits(): Flow<List<HabitEntity>> = habitDao.getAllHabits()

    suspend fun getHabitById(id: Int): HabitEntity? = habitDao.getHabitById(id)

    suspend fun insertHabit(habit: HabitEntity) = habitDao.insertHabit(habit)

    suspend fun updateHabit(habit: HabitEntity) = habitDao.updateHabit(habit)

    suspend fun deleteHabit(habit: HabitEntity) = habitDao.deleteHabit(habit)

    // --- Check-ins ---
    suspend fun checkInToday(habitId: Int) {
        val (dayStart, dayEnd) = getTodayBounds()
        val alreadyCheckedIn = checkInDao.hasCheckInToday(habitId, dayStart, dayEnd)

        if (!alreadyCheckedIn) {
            val checkIn = CheckInEntity(
                habitId = habitId,
                timestamp = System.currentTimeMillis()
            )
            checkInDao.insertCheckIn(checkIn)
        }
        // Dacă există deja, nu facem nimic (prevent spam)
    }

    suspend fun hasCheckInToday(habitId: Int): Boolean {
        val (dayStart, dayEnd) = getTodayBounds()
        return checkInDao.hasCheckInToday(habitId, dayStart, dayEnd)
    }

    suspend fun deleteCheckInToday(habitId: Int) {
        val (dayStart, dayEnd) = getTodayBounds()
        // Folosim metoda nouă care șterge doar 1 check-in, nu toate
        checkInDao.deleteOneCheckInToday(habitId, dayStart, dayEnd)
    }

    fun getCheckInsForHabit(habitId: Int): Flow<List<CheckInEntity>> {
        return checkInDao.getCheckInsForHabit(habitId)
    }

    suspend fun getCheckInCount(habitId: Int): Int {
        // Folosim count de zile unice în loc de count total
        // Astfel, chiar dacă există duplicate (din bug-uri vechi), numără corect
        return checkInDao.getUniqueDaysCheckInCount(habitId)
    }

    // --- Cleanup Methods (opțional, pentru curățarea duplicate-urilor vechi) ---
    suspend fun cleanupDuplicates(habitId: Int) {
        checkInDao.cleanupDuplicateCheckIns(habitId)
    }

    suspend fun cleanupAllDuplicates() {
        checkInDao.cleanupAllDuplicates()
    }

    // --- Helper: Get timestamp bounds for today ---
    private fun getTodayBounds(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val dayStart = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val dayEnd = calendar.timeInMillis

        return Pair(dayStart, dayEnd)
    }

    // --- Remote (Retrofit) ---
    suspend fun getDailyQuote(): QuoteDto? {
        return try {
            val response = api.getQuoteOfTheDay()
            if (response.isNotEmpty()) response[0] else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}