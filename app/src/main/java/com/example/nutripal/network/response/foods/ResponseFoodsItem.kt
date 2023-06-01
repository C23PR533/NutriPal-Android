package com.example.nutripal.network.response.foods

data class ResponseFoodsItem(
    val brand_name: String,
    val food_id: String,
    val food_name: String,
    val food_type: List<String>,
    val food_url: String,
    val id: String,
    val img_url: String,
    val servings: Servings
)