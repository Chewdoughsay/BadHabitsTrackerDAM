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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    habitId: Int,
    onSave: (String, String, Int) -> Unit, // Callback pentru salvare (Titlu, Descriere, Zile)
    onBack: () -> Unit
) {
    // Determinăm modul: Adăugare sau Editare
    val isEditing = habitId != -1

    // State-uri locale pentru formular (temporar, până adăugăm ViewModel)
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var goalDaysText by remember { mutableStateOf("") }

    // Simulare: Dacă e editare, pre-completăm cu date fake (doar vizual, de test)
    LaunchedEffect(habitId) {
        if (isEditing) {
            title = "Existing Habit"
            description = "This is a description loaded for editing."
            goalDaysText = "30"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Habit" else "Add New Habit") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
                label = { Text("Habit Title") },
                placeholder = { Text("e.g., No Sugar") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            // 2. Descriere (mai înaltă)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Motivation / Description") },
                placeholder = { Text("Why do you want to break this habit?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            // 3. Zile Țintă (Număr)
            OutlinedTextField(
                value = goalDaysText,
                onValueChange = {
                    // Permitem doar cifre
                    if (it.all { char -> char.isDigit() }) {
                        goalDaysText = it
                    }
                },
                label = { Text("Goal Days") },
                placeholder = { Text("e.g., 30") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.weight(1f)) // Împinge butonul jos

            // 4. Buton Save
            Button(
                onClick = {
                    val days = goalDaysText.toIntOrNull() ?: 0
                    if (title.isNotEmpty() && days > 0) {
                        onSave(title, description, days)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = title.isNotEmpty() && goalDaysText.isNotEmpty()
            ) {
                Text(text = if (isEditing) "Update Habit" else "Create Habit")
            }
        }
    }
}