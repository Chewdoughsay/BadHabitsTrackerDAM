package com.example.badhabitstrackerdam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.badhabitstrackerdam.model.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface QuoteState {
    data object Loading : QuoteState
    data class Success(val text: String, val author: String) : QuoteState
    data object Error : QuoteState
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    // Trigger pentru refresh când se face check-in
    private val _refreshTrigger = MutableStateFlow(0)

    // Flow pentru vizibilitatea citatului (toggle cu shake)
    // ⭐ SCHIMBAT: false by default - "Kinder Surprise" style!
    private val _isQuoteVisible = MutableStateFlow(false)
    val isQuoteVisible = _isQuoteVisible.asStateFlow()

    // Flow pentru Lista de Obiceiuri cu Progres
    val habitsWithProgress: StateFlow<List<HabitWithProgress>> = repository.getAllHabits()
        .combine(_refreshTrigger) { habits, _ ->
            habits.map { habit ->
                val checkInCount = repository.getCheckInCount(habit.id)
                val hasCheckedToday = repository.hasCheckInToday(habit.id)
                val progress = if (habit.goalDays > 0) {
                    (checkInCount.toFloat() / habit.goalDays).coerceIn(0f, 1f)
                } else {
                    0f
                }

                HabitWithProgress(
                    habit = habit,
                    totalCheckIns = checkInCount,
                    hasCheckedInToday = hasCheckedToday,
                    progressPercentage = progress
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Flow pentru Citat
    private val _quoteState = MutableStateFlow<QuoteState>(QuoteState.Loading)
    val quoteState = _quoteState.asStateFlow()

    init {
        fetchDailyQuote()
        cleanupDuplicates()
    }

    private fun fetchDailyQuote() {
        viewModelScope.launch {
            _quoteState.value = QuoteState.Loading
            try {
                val quoteDto = repository.getDailyQuote()

                if (quoteDto != null) {
                    _quoteState.value = QuoteState.Success(quoteDto.text, quoteDto.author)
                } else {
                    _quoteState.value = QuoteState.Error
                }
            } catch (e: Exception) {
                _quoteState.value = QuoteState.Error
            }
        }
    }

    private fun cleanupDuplicates() {
        viewModelScope.launch {
            try {
                repository.cleanupAllDuplicates()
                _refreshTrigger.value += 1
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Funcție pentru check-in
    fun checkInHabit(habitId: Int) {
        viewModelScope.launch {
            repository.checkInToday(habitId)
            _refreshTrigger.value += 1
        }
    }

    // Funcție pentru undo check-in
    fun undoCheckIn(habitId: Int) {
        viewModelScope.launch {
            repository.deleteCheckInToday(habitId)
            _refreshTrigger.value += 1
        }
    }

    // 📳 SHAKE FEATURE - Toggle quote visibility
    fun toggleQuoteVisibility() {
        _isQuoteVisible.value = !_isQuoteVisible.value
    }
}