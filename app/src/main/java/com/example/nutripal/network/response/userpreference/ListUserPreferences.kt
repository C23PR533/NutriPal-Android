package com.example.nutripal.network.response.userpreference


import com.google.gson.annotations.SerializedName

data class ListUserPreferences(
    @SerializedName("activityLevel")
    val activityLevel: String,
    @SerializedName("birthdate")
    val birthdate: String,
    @SerializedName("disease")
    val disease: List<String>,
    @SerializedName("favoriteFood")
    val favoriteFood: List<String>,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("goals")
    val goals: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("id_user")
    val idUser: String,
    @SerializedName("weight")
    val weight: String
)