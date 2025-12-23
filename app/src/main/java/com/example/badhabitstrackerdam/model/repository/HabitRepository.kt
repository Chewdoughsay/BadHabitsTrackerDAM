package com.example.badhabitstrackerdam.model.repository

import com.example.badhabitstrackerdam.model.local.HabitDao
import com.example.badhabitstrackerdam.model.local.HabitEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// @Inject îi spune lui Hilt cum să creeze singur acest Repository.
// Hilt va căuta un HabitDao (pe care îl oferim în AppModule) și îl va injecta aici.
class HabitRepository @Inject constructor(
    private val habitDao: HabitDao
) {
    // Expunem datele sub formă de Flow (reactive)
    fun getAllHabits(): Flow<List<HabitEntity>> = habitDao.getAllHabits()

    suspend fun getHabitById(id: Int): HabitEntity? = habitDao.getHabitById(id)

    suspend fun insertHabit(habit: HabitEntity) = habitDao.insertHabit(habit)

    suspend fun updateHabit(habit: HabitEntity) = habitDao.updateHabit(habit)

    suspend fun deleteHabit(habit: HabitEntity) = habitDao.deleteHabit(habit)
}