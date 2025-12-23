package com.example.badhabitstrackerdam.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    onNavigateToDetail: (Int) -> Unit // Callback care cere un ID
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "DASHBOARD")
            Spacer(modifier = Modifier.height(16.dp))

            // Buton pentru ADAUGARE (trimitem -1 convențional pentru "New")
            Button(onClick = { onNavigateToDetail(-1) }) {
                Text(text = "Add New Habit (+)")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Buton pentru EDITARE (trimitem un ID fictiv, ex: 10)
            Button(onClick = { onNavigateToDetail(10) }) {
                Text(text = "Edit Habit #10")
            }
        }
    }
}