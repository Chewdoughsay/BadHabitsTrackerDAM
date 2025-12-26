package com.example.badhabitstrackerdam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.badhabitstrackerdam.model.local.HabitEntity
import com.example.badhabitstrackerdam.model.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// Definim stările posibile pentru UI (Pattern-ul MVI simplificat)
sealed interface QuoteState {
    data object Loading : QuoteState
    data class Success(val text: String, val author: String) : QuoteState
    data object Error : QuoteState
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    // 1. Fluxul pentru Lista de Obiceiuri (din DB)
    val habits: StateFlow<List<HabitEntity>> = repository.getAllHabits()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. Fluxul pentru Citat (din API)
    private val _quoteState = MutableStateFlow<QuoteState>(QuoteState.Loading)
    val quoteState = _quoteState.asStateFlow()

    init {
        fetchDailyQuote()
    }

    private fun fetchDailyQuote() {
        viewModelScope.launch {
            _quoteState.value = QuoteState.Loading
            try {
                // Networking Asincron: Apelăm repository-ul
                val quoteDto = repository.getDailyQuote()

                if (quoteDto != null) {
                    _quoteState.value = QuoteState.Success(quoteDto.text, quoteDto.author)
                } else {
                    _quoteState.value = QuoteState.Error
                }
            } catch (e: Exception) {
                // Gestionăm orice eroare de rețea
                _quoteState.value = QuoteState.Error
            }
        }
    }
}