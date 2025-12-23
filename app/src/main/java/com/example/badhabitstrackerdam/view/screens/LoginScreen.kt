package com.example.badhabitstrackerdam.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo / Icon
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "BadHabits Tracker",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Câmp Nume
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter your name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Buton Start
            Button(
                onClick = onLoginSuccess,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = name.isNotEmpty() // Activ doar daca s-a scris ceva
            ) {
                Text(text = "Start Journey")
            }
        }
    }
}