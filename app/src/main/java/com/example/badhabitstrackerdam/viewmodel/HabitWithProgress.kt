package com.example.badhabitstrackerdam.viewmodel

import com.example.badhabitstrackerdam.model.local.HabitEntity

// Data class care combină HabitEntity cu informații despre progres și check-in
data class HabitWithProgress(
    val habit: HabitEntity,
    val totalCheckIns: Int,
    val hasCheckedInToday: Boolean,
    val progressPercentage: Float // 0.0 to 1.0
) {
    val progressText: String
        get() = "$totalCheckIns / ${habit.goalDays}"
}