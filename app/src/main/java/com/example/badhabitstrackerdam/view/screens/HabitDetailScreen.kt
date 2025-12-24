package com.example.badhabitstrackerdam.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.badhabitstrackerdam.viewmodel.HabitDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    habitId: Int, // Păstrăm parametrul doar pentru titlul UI, logica e în VM
    onBack: () -> Unit,
    onSave: (String, String, Int) -> Unit, // Nu îl mai folosim direct, dar îl lăsăm pt compatibilitate momentan sau îl putem șterge
    // Injectăm ViewModel-ul
    viewModel: HabitDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isEditing = habitId != -1

    // State-uri locale pentru editare text
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var goalDaysText by remember { mutableStateOf("") }

    // Un flag ca să știm dacă am încărcat deja datele inițiale
    var isDataLoaded by remember { mutableStateOf(false) }

    // Când vine starea din DB (la Editare), populăm câmpurile o singură dată
    LaunchedEffect(uiState) {
        if (!isDataLoaded && (uiState.title.isNotEmpty() || isEditing)) {
            title = uiState.title
            description = uiState.description
            goalDaysText = uiState.goalDays
            if (uiState.title.isNotEmpty()) isDataLoaded = true
        }
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
            // Titlu
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Habit Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            // Descriere
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Motivation") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            // Zile
            OutlinedTextField(
                value = goalDaysText,
                onValueChange = { if (it.all { c -> c.isDigit() }) goalDaysText = it },
                label = { Text("Goal Days") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Buton Save conectat la ViewModel
            Button(
                onClick = {
                    val days = goalDaysText.toIntOrNull() ?: 0
                    if (title.isNotEmpty() && days > 0) {
                        // Apelăm funcția din ViewModel
                        viewModel.saveHabit(title, description, days) {
                            // Callback: Când s-a terminat salvarea, navigăm înapoi
                            onBack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = title.isNotEmpty() && goalDaysText.isNotEmpty()
            ) {
                Text(text = "Save Habit")
            }
        }
    }
}