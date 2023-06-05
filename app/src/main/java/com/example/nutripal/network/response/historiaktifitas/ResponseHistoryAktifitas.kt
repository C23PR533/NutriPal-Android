package com.example.nutripal.network.response.historiaktifitas

data class ResponseHistoryAktifitas(
    val code: Int,
    val error: Boolean,
    val listHistoryActivity: ListHistoryActivity,
    val message: String
)