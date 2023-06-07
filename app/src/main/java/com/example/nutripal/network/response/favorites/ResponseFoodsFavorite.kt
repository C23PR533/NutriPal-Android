package com.example.nutripal.network.response.favorites


import com.google.gson.annotations.SerializedName

data class ResponseFoodsFavorite(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)