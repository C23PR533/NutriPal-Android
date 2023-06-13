package com.example.nutripal.network.response.userpreference


import com.google.gson.annotations.SerializedName

data class ResponseUserPreferences(
    @SerializedName("code")
    val code: Int,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listUserPreferences")
    val listUserPreferences: ListUserPreferences,
    @SerializedName("message")
    val message: String
)