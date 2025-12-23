package com.example.badhabitstrackerdam.model.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    // Returnează o listă "vie" (Flow). Când se schimbă ceva în DB, UI-ul se actualizează automat.
    // Cerința: Management Asincron [cite: 38]
    @Query("SELECT * FROM habits ORDER BY id DESC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    // Obține un singur habit (pentru editare)
    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Int): HabitEntity?

    // Inserare (sau înlocuire dacă există conflict, deși ID-ul auto-generat previne asta des)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    // Actualizare
    @Update
    suspend fun updateHabit(habit: HabitEntity)

    // Ștergere
    @Delete
    suspend fun deleteHabit(habit: HabitEntity)
}