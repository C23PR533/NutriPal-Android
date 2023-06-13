package com.example.nutripal.network.response.login


import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    @SerializedName("code")
    val code: Int,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String
)