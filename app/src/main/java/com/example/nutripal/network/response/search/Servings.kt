package com.example.nutripal.network.response.search


import com.google.gson.annotations.SerializedName

data class Servings(
    @SerializedName("serving")
    val serving: List<Serving>
)