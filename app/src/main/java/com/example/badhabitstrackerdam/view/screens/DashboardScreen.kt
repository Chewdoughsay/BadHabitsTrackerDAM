package com.example.badhabitstrackerdam.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.badhabitstrackerdam.view.screens.components.HabitItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToDetail: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToDetail(-1) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // --- Placeholder Citatul Zilei ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Quote of the Day",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"The only way to break a bad habit is to drop it.\"",
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Your Habits",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- Lista de Obiceiuri (LazyColumn) ---
            // Momentan date HARDCODATE pentru a testa UI-ul
            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp) // Spatiu pentru FAB
            ) {
                items(5) { index ->
                    HabitItem(
                        title = "Fake Habit #$index",
                        daysGoal = 30,
                        currentProgress = (index + 1) * 0.15f, // Progres simulat
                        onClick = { onNavigateToDetail(index) }
                    )
                }
            }
        }
    }
}