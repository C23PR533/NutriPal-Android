package com.example.nutripal.network.response.favorites


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("favoriteFoods")
    val favoriteFoods: List<FavoriteFood>,
    @SerializedName("id_user")
    val idUser: String
)