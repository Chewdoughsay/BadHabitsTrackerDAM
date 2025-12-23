package com.example.badhabitstrackerdam.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,             // ID unic, generat automat de Room
    val title: String,           // Titlul obiceiului (ex: "Fără Zahăr")
    val description: String,     // Descriere motivațională
    val goalDays: Int,           // Ținta de zile (ex: 30 de zile)
    val startTimestamp: Long     // Data începerii (în milisecunde)
)