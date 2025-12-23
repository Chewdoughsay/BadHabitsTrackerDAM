package com.example.badhabitstrackerdam.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.badhabitstrackerdam.view.screens.DashboardScreen
import com.example.badhabitstrackerdam.view.screens.HabitDetailScreen
import com.example.badhabitstrackerdam.view.screens.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN
    ) {
        // --- RUTA 1: LOGIN ---
        composable(route = AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    // Navigăm la Dashboard și distrugem Login din backstack
                    // Astfel, userul nu poate da "Back" la login
                    navController.navigate(AppRoutes.DASHBOARD) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // --- RUTA 2: DASHBOARD ---
        composable(route = AppRoutes.DASHBOARD) {
            DashboardScreen(
                onNavigateToDetail = { selectedId ->
                    // Folosim funcția helper pentru a construi ruta: "habit_detail/ID"
                    navController.navigate(AppRoutes.buildHabitDetailRoute(selectedId))
                }
            )
        }

        // --- RUTA 3: DETAIL (cu argument) ---
        composable(
            route = AppRoutes.HABIT_DETAIL_ROUTE, // "habit_detail/{habitId}"
            arguments = listOf(
                navArgument(AppRoutes.HABIT_ID_ARG) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            // Extragem argumentul din Bundle
            val habitId = backStackEntry.arguments?.getInt(AppRoutes.HABIT_ID_ARG) ?: -1

            // Îl pasăm ecranului
            HabitDetailScreen(habitId = habitId)
        }
    }
}