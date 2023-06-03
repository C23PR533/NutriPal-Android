package com.example.nutripal.network.response.historiaktifitas


import com.google.gson.annotations.SerializedName

data class ListHistoryActivity(
    @SerializedName("aktifitas")
    val aktifitas: Aktifitas,
    @SerializedName("id_user")
    val idUser: String,
    @SerializedName("kalori_harian")
    val kaloriHarian: String,
    @SerializedName("Sisa Kalori")
    val sisaKalori: String,
    @SerializedName("tanggal")
    val tanggal: String,
    @SerializedName("total_kalori")
    val totalKalori: String
)