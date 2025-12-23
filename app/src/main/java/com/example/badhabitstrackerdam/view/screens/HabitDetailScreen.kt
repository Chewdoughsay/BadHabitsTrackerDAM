package com.example.badhabitstrackerdam.view.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HabitDetailScreen(
    habitId: Int // Primim ID-ul ca parametru simplu
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {
            if (habitId == -1) {
                Text(text = "MODE: CREATE NEW HABIT")
            } else {
                Text(text = "MODE: EDIT HABIT (ID: $habitId)")
            }
        }
    }
}