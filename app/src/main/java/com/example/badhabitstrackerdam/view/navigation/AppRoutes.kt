package com.example.badhabitstrackerdam.view.navigation

object AppRoutes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    // Definim ruta cu un placeholder pentru argument
    const val HABIT_DETAIL_BASE = "habit_detail"
    const val HABIT_ID_ARG = "habitId"
    const val HABIT_DETAIL_ROUTE = "$HABIT_DETAIL_BASE/{$HABIT_ID_ARG}"

    // Funcție helper pentru a construi ruta când navigăm (ex: "habit_detail/5")
    fun buildHabitDetailRoute(habitId: Int): String {
        return "$HABIT_DETAIL_BASE/$habitId"
    }
}