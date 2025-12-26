package com.example.badhabitstrackerdam.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.badhabitstrackerdam.viewmodel.DashboardViewModel
import com.example.badhabitstrackerdam.viewmodel.QuoteState // Importăm starea
import com.example.badhabitstrackerdam.view.screens.components.HabitItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToDetail: (Int) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val habits by viewModel.habits.collectAsState()
    val quoteState by viewModel.quoteState.collectAsState() // Ascultăm starea citatului

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
            // --- SECTIUNEA CITAT (Dinamică) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp), // Înălțime minimă ca să nu "sară" UI-ul
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (val state = quoteState) {
                        is QuoteState.Loading -> {
                            // Feedback vizual pentru încărcare
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        is QuoteState.Success -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "\"${state.text}\"",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontStyle = FontStyle.Italic,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "- ${state.author}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                                )
                            }
                        }
                        is QuoteState.Error -> {
                            // Feedback vizual pentru eroare (Fallback)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Warning, contentDescription = "Error")
                                Text(
                                    text = "Could not load quote.\nStay strong regardless!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
            // --- FINAL SECTIUNE CITAT ---

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Your Habits", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            if (habits.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No habits yet. Add one!", color = MaterialTheme.colorScheme.secondary)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(habits) { habit ->
                        HabitItem(
                            title = habit.title,
                            daysGoal = habit.goalDays,
                            currentProgress = 0.1f,
                            onClick = { onNavigateToDetail(habit.id) }
                        )
                    }
                }
            }
        }
    }
}