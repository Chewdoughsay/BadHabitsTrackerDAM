package com.example.badhabitstrackerdam.model.remote

import retrofit2.http.GET

interface ZenQuotesApi {
    // Endpoint-ul este "https://zenquotes.io/api/today"
    // Base URL va fi "https://zenquotes.io/", deci aici punem doar restul
    @GET("api/today")
    suspend fun getQuoteOfTheDay(): List<QuoteDto>
}