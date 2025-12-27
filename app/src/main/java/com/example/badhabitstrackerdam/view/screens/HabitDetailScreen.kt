package com.example.badhabitstrackerdam.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.badhabitstrackerdam.viewmodel.HabitDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    habitId: Int,
    onBack: () -> Unit,
    viewModel: HabitDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isEditing = habitId != -1

    // Controller pentru tastatură
    val keyboardController = LocalSoftwareKeyboardController.current

    // State-uri locale
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var goalDaysText by remember { mutableStateOf("") }

    // Încărcare date inițiale
    var isDataLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(uiState) {
        if (!isDataLoaded && (uiState.title.isNotEmpty() || isEditing)) {
            title = uiState.title
            description = uiState.description
            goalDaysText = uiState.goalDays
            if (uiState.title.isNotEmpty()) isDataLoaded = true
        }
    }

    // Dialog de confirmare ștergere (Opțional, pentru UX mai bun)
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Habit?") },
            text = { Text("Are you sure you want to remove this habit? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteHabit {
                            showDeleteDialog = false
                            onBack()
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Habit" else "Add New Habit") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Butonul DELETE apare doar dacă edităm un habit existent
                    if (isEditing) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Habit",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Titlu
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Habit Title *") }, // Marcăm ca obligatoriu
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = title.isEmpty(), // Feedback vizual roșu dacă e gol
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            // 2. Descriere
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Motivation") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            // 3. Zile
            OutlinedTextField(
                value = goalDaysText,
                onValueChange = { if (it.all { c -> c.isDigit() }) goalDaysText = it },
                label = { Text("Goal Days *") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.weight(1f))

            // 4. Buton Save
            Button(
                onClick = {
                    // Ascundem tastatura
                    keyboardController?.hide()

                    val days = goalDaysText.toIntOrNull() ?: 0
                    viewModel.saveHabit(title, description, days) {
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                // VALIDARE: Buton inactiv dacă titlul sau zilele lipsesc
                enabled = title.isNotEmpty() && goalDaysText.isNotEmpty()
            ) {
                Text(text = "Save Habit")
            }
        }
    }
}