package com.example.nutripal.network.response.foodid


import com.google.gson.annotations.SerializedName

data class ListUserPreferences(
    @SerializedName("brand_name")
    val brandName: String,
    @SerializedName("food_id")
    val foodId: String,
    @SerializedName("food_name")
    val foodName: String,
    @SerializedName("food_type")
    val foodType: List<String>,
    @SerializedName("food_url")
    val foodUrl: String,
    @SerializedName("servings")
    val servings: Servings
)