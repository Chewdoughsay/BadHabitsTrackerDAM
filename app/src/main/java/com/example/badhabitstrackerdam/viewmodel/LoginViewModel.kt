package com.example.badhabitstrackerdam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.badhabitstrackerdam.utils.TestDataPopulator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val testDataPopulator: TestDataPopulator
) : ViewModel() {

    private val _isPopulatingData = MutableStateFlow(false)
    val isPopulatingData = _isPopulatingData.asStateFlow()

    fun populateTestData(onComplete: () -> Unit) {
        viewModelScope.launch {
            _isPopulatingData.value = true
            try {
                testDataPopulator.populateTestData()
                // Give it a moment to finish
                kotlinx.coroutines.delay(500)
                onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isPopulatingData.value = false
            }
        }
    }
}