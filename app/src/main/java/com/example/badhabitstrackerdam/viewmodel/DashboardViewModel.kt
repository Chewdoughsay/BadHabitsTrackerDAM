package com.example.badhabitstrackerdam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.badhabitstrackerdam.model.local.HabitEntity
import com.example.badhabitstrackerdam.model.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    // Expunem lista de obiceiuri ca un StateFlow.
    // .stateIn convertește Flow-ul "rece" de la Room într-un StateFlow "fierbinte" pentru UI.
    val habits: StateFlow<List<HabitEntity>> = repository.getAllHabits()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Oprește ascultarea dacă plecăm din ecran > 5 sec
            initialValue = emptyList()
        )
}