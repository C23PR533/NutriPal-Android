package com.example.nutripal.network.dummmy

data class ResponseUserPreference(
    val activityLevel: String,
    val birthdate: String,
    val disease: List<String>,
    val favoriteFood: List<String>,
    val gender: String,
    val goals: String,
    val height: String,
    val id_user: String,
    val weight: String
)