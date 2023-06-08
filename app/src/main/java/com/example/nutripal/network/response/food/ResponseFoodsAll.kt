package com.example.nutripal.network.response.food


import com.google.gson.annotations.SerializedName

data class ResponseFoodsAll(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String
)