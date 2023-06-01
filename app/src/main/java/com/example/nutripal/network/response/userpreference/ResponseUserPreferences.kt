package com.example.nutripal.network.response.userpreference

data class ResponseUserPreferences(
    val error: Boolean,
    val listUserPreferences: ListUserPreferences,
    val message: String
)