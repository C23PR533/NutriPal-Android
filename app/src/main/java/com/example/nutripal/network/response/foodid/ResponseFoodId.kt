package com.example.nutripal.network.response.foodid


import com.google.gson.annotations.SerializedName

data class ResponseFoodId(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("code")
    val code: Int,
)