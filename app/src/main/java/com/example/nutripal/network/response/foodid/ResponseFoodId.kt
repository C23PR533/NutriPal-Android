package com.example.nutripal.network.response.foodid


import com.google.gson.annotations.SerializedName

data class ResponseFoodId(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listUserPreferences")
    val listUserPreferences: ListUserPreferences,
    @SerializedName("message")
    val message: String
)