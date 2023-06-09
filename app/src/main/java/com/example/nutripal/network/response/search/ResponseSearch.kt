package com.example.nutripal.network.response.search


import com.google.gson.annotations.SerializedName

data class ResponseSearch(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String
)