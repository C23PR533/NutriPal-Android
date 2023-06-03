package com.example.nutripal.network.response.historiaktifitas


import com.google.gson.annotations.SerializedName

data class Aktifitas(
    @SerializedName("kalori_keluar")
    val kaloriKeluar: List<KaloriKeluar>,
    @SerializedName("kalori_masuk")
    val kaloriMasuk: List<KaloriMasuk>

)