package com.example.nutripal.network.response.historiaktifitas


import com.google.gson.annotations.SerializedName

data class KaloriKeluar(
    @SerializedName("duration")
    val duration: String,
    @SerializedName("kalori_terbakar")
    val kaloriTerbakar: String,
    @SerializedName("nama_exercise")
    val namaExercise: String
)