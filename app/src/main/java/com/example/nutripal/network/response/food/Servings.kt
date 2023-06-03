package com.example.nutripal.network.response.food


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Servings(
    @SerializedName("serving")
    val serving: List<Serving>
):Parcelable