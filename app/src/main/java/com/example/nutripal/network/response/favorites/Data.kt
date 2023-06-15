package com.example.nutripal.network.response.favorites


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("favoriteFoods")
    val favoriteFoods: List<String>,
    @SerializedName("id_user")
    val idUser: String
)