package com.example.badhabitstrackerdam.model.remote

import com.google.gson.annotations.SerializedName

// API-ul returnează o listă de obiecte, noi definim obiectul
data class QuoteDto(
    @SerializedName("q") val text: String,   // Mapăm "q" din JSON la "text"
    @SerializedName("a") val author: String  // Mapăm "a" din JSON la "author"
)