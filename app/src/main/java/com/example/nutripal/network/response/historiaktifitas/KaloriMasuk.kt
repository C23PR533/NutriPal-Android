package com.example.nutripal.network.response.historiaktifitas


import com.google.gson.annotations.SerializedName

data class KaloriMasuk(
    @SerializedName("id_makanan")
    val idMakanan: String,
    @SerializedName("kalori")
    val kalori: String,
    @SerializedName("nama_makanan")
    val namaMakanan: String,
    @SerializedName("waktu")
    val waktu: String
)