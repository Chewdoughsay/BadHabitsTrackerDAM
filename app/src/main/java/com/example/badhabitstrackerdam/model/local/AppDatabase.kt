package com.example.badhabitstrackerdam.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.badhabitstrackerdam.model.local.HabitEntity

// Definim entitățile și versiunea bazei de date
@Database(entities = [HabitEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Expunem DAO-ul pentru a putea fi injectat mai târziu
    abstract fun habitDao(): HabitDao
}