package com.example.nutripal.network.response.historiaktifitas


import com.google.gson.annotations.SerializedName

data class ResponseHistoryAktivity(
    @SerializedName("code")
    val code: Int,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listHistoryActivity")
    val listHistoryActivity: ListHistoryActivity,
    @SerializedName("message")
    val message: String
)