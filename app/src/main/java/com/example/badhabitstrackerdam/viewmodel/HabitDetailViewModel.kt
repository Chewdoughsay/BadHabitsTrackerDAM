package com.example.badhabitstrackerdam.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.badhabitstrackerdam.model.local.HabitEntity
import com.example.badhabitstrackerdam.model.repository.HabitRepository
import com.example.badhabitstrackerdam.view.navigation.AppRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitDetailViewModel @Inject constructor(
    private val repository: HabitRepository,
    savedStateHandle: SavedStateHandle // Prinde automat argumentele din Navigație
) : ViewModel() {

    // Extragem ID-ul din argumentele rutei
    private val habitId: Int = savedStateHandle[AppRoutes.HABIT_ID_ARG] ?: -1

    // State pentru câmpurile din UI (îl vom observa în Ecran)
    private val _uiState = MutableStateFlow(HabitUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Dacă avem un ID valid (nu e -1), înseamnă că EDITĂM. Încărcăm datele.
        if (habitId != -1) {
            loadHabit()
        }
    }

    private fun loadHabit() {
        viewModelScope.launch {
            val habit = repository.getHabitById(habitId)
            habit?.let {
                // Actualizăm starea UI-ului cu datele din DB
                _uiState.value = HabitUiState(
                    title = it.title,
                    description = it.description,
                    goalDays = it.goalDays.toString(),
                    isLoading = false
                )
            }
        }
    }

    fun saveHabit(title: String, description: String, goalDays: Int, onSaveSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()

            if (habitId == -1) {
                // --- CREATE NEW ---
                val newHabit = HabitEntity(
                    title = title,
                    description = description,
                    goalDays = goalDays,
                    startTimestamp = currentTime
                )
                repository.insertHabit(newHabit)
            } else {
                // --- UPDATE EXISTING ---
                // Trebuie să păstrăm data începerii originală!
                val existingHabit = repository.getHabitById(habitId)
                val updatedHabit = existingHabit?.copy(
                    title = title,
                    description = description,
                    goalDays = goalDays
                ) ?: return@launch // Safety check

                repository.updateHabit(updatedHabit)
            }

            // Anunțăm UI-ul că am terminat (pentru a naviga înapoi)
            onSaveSuccess()
        }
    }
}

// O clasă simplă care ține starea formularului
data class HabitUiState(
    val title: String = "",
    val description: String = "",
    val goalDays: String = "",
    val isLoading: Boolean = false
)