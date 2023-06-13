package com.example.nutripal.network.response.login


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("idToken")
    val idToken: String,
    @SerializedName("uid")
    val uid: String
)