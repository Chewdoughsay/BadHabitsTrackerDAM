package com.example.badhabitstrackerdam.model.local

import androidx.room.Database
import androidx.room.RoomDatabase

// Definim entitățile și versiunea bazei de date
@Database(entities = [HabitEntity::class, CheckInEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Expunem DAO-urile pentru a putea fi injectate mai târziu
    abstract fun habitDao(): HabitDao
    abstract fun checkInDao(): CheckInDao
}