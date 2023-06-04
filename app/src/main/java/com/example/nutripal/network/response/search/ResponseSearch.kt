package com.example.nutripal.network.response.search

data class ResponseSearch(
    val code: Int,
    val `data`: List<Data>,
    val message: String
)