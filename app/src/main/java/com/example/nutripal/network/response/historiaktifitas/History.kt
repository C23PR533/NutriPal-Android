package com.example.nutripal.network.response.historiaktifitas

data class History(
    val sisa_kalori: String,
    val aktifitas: Aktifitas,
    val kalori_harian: String,
    val tanggal: String,
    val total_kalori: Int
)