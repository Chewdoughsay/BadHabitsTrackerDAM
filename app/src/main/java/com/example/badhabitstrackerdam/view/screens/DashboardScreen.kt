package com.example.badhabitstrackerdam.view.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.badhabitstrackerdam.utils.ShakeDetector
import com.example.badhabitstrackerdam.viewmodel.DashboardViewModel
import com.example.badhabitstrackerdam.viewmodel.QuoteState
import com.example.badhabitstrackerdam.view.screens.components.HabitItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToDetail: (Int) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val habitsWithProgress by viewModel.habitsWithProgress.collectAsState()
    val quoteState by viewModel.quoteState.collectAsState()
    val isQuoteVisible by viewModel.isQuoteVisible.collectAsState()
    val context = LocalContext.current

    // 📳 SHAKE DETECTOR - Start/Stop with screen lifecycle
    DisposableEffect(Unit) {
        val shakeDetector = ShakeDetector(context) {
            // On shake detected → toggle quote visibility
            viewModel.toggleQuoteVisibility()
        }
        shakeDetector.start()

        onDispose {
            shakeDetector.stop()
        }
    }

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
            // --- SECTIUNEA CITAT (Animată cu Shake) ---
            AnimatedVisibility(
                visible = isQuoteVisible,
                enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
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
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "📳 Shake to hide",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f)
                                    )
                                }
                            }
                            is QuoteState.Error -> {
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
            }

            // ⭐ IMPROVED: Shake hint când quote-ul e ascuns (mai proeminent)
            if (!isQuoteVisible) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "💬",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Shake for daily motivation!",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "📳 Give your device a shake",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (isQuoteVisible) 24.dp else 8.dp))

            Text(text = "Your Habits", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            if (habitsWithProgress.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No habits yet. Add one!", color = MaterialTheme.colorScheme.secondary)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(habitsWithProgress) { habitWithProgress ->
                        HabitItem(
                            title = habitWithProgress.habit.title,
                            daysGoal = habitWithProgress.habit.goalDays,
                            currentProgress = habitWithProgress.progressPercentage,
                            progressText = habitWithProgress.progressText,
                            hasCheckedInToday = habitWithProgress.hasCheckedInToday,
                            onCheckIn = {
                                if (habitWithProgress.hasCheckedInToday) {
                                    viewModel.undoCheckIn(habitWithProgress.habit.id)
                                } else {
                                    viewModel.checkInHabit(habitWithProgress.habit.id)
                                }
                            },
                            onClick = { onNavigateToDetail(habitWithProgress.habit.id) }
                        )
                    }
                }
            }
        }
    }
}