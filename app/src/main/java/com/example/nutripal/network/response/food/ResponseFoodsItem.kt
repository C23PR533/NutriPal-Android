package com.example.nutripal.network.response.food


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class ResponseFoodsItem(
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
    @SerializedName("id")
    val id: String,
    @SerializedName("servings")
    val servings: Servings
):Parcelable