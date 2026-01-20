package com.example.badhabitstrackerdam.model.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "check_ins",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE // Când ștergem un habit, ștergem și check-in-urile
        )
    ],
    indices = [Index("habitId")] // Index pentru queries mai rapide
)
data class CheckInEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val habitId: Int,           // ID-ul habitului
    val timestamp: Long         // Când a fost făcut check-in-ul (în milisecunde)
)