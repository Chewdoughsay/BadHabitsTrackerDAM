package com.example.badhabitstrackerdam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.badhabitstrackerdam.view.navigation.AppNavigation
import com.example.badhabitstrackerdam.view.theme.BadHabitsTrackerDAMTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BadHabitsTrackerDAMTheme() {
                AppNavigation()
            }
        }
    }
}