package com.example.nutripal.network.response.historiaktifitas

data class History(
    val sisa_kalori: Int,
    val aktifitas: Aktifitas,
    val kalori_harian: Int,
    val tanggal: String,
    val total_kalori: Int
)