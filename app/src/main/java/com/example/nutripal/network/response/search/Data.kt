package com.example.nutripal.network.response.search

data class Data(
    val brand_name: String,
    val food_id: String,
    val food_name: String,
    val food_type: List<String>,
    val food_url: String,
    val id: String,
    val servings: Servings
)