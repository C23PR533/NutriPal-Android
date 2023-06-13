package com.example.nutripal.network.response

import com.google.gson.annotations.SerializedName

data class ResponsStatus(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("error")
    val error:Boolean
)