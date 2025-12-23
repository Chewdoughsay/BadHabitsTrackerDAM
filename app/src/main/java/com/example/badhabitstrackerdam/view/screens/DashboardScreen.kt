package com.example.badhabitstrackerdam.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.badhabitstrackerdam.viewmodel.DashboardViewModel
import com.example.badhabitstrackerdam.view.screens.components.HabitItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToDetail: (Int) -> Unit,
    // Injectăm ViewModel-ul automat folosind Hilt
    viewModel: DashboardViewModel = hiltViewModel()
) {
    // "Ascultăm" datele din baza de date
    val habits by viewModel.habits.collectAsState()

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
            // --- Placeholder Citatul Zilei (Rămâne neschimbat momentan) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Quote of the Day", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"Start where you are. Use what you have. Do what you can.\"",
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Your Habits", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            // --- LISTA REALA ---
            if (habits.isEmpty()) {
                // Mesaj pentru listă goală
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("No habits yet. Add one!", color = MaterialTheme.colorScheme.secondary)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    // Iterăm prin lista reală din DB
                    items(habits) { habit ->
                        HabitItem(
                            title = habit.title,
                            daysGoal = habit.goalDays,
                            currentProgress = 0.1f, // Vom calcula asta mai târziu
                            onClick = { onNavigateToDetail(habit.id) }
                        )
                    }
                }
            }
        }
    }
}