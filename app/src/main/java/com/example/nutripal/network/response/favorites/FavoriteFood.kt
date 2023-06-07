package com.example.nutripal.network.response.favorites


import com.google.gson.annotations.SerializedName

data class FavoriteFood(
    @SerializedName("calories")
    val calories: String,
    @SerializedName("food_id")
    val foodId: String,
    @SerializedName("food_name")
    val foodName: String
)