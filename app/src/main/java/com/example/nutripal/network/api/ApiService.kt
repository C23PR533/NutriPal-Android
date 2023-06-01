package com.example.nutripal.network


import com.example.nutripal.network.response.ResponsStatus
import com.example.nutripal.network.response.datadiri.ResponseDataDiri
import com.example.nutripal.network.response.userpreference.ResponseUserPreferences
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @GET("datadiri/{id_user}")
    suspend fun getDatadiri(
        @Path("id_user")id_user:String
    ): Response<ResponseDataDiri>
    @GET("userpreferences/{id_user}")
    suspend fun getUserPreferences(
        @Path("id_user")id_user:String
    ): Response<ResponseUserPreferences>

    @FormUrlEncoded
    @POST("datadiri")
    suspend fun register(
        @Field("id_user")id_user:String,
        @Field("nama")nama:String,
        @Field("nomor_hp")nomor_hp:String,
        @Field("email")email:String,
        @Field("birthdate")birthdate:String?,
        @Field("gender")gender:String?,
        @Field("foto_profile")foto_profile:String?,
    ):Response<ResponsStatus>

    @FormUrlEncoded
    @PUT("datadiri/{id_user}")
    suspend fun editDataDiri(
        @Path("id_user")id:String,
        @Field("id_user")id_user:String,
        @Field("nama")nama:String,
        @Field("nomor_hp")nomor_hp:String,
        @Field("email")email:String,
        @Field("birthdate")birthdate:String?,
        @Field("gender")gender:String?,
        @Field("foto_profile")foto_profile:String?,
    ):Response<ResponsStatus>

    @FormUrlEncoded
    @POST("userpreferences")
    suspend fun postUserPreferences(
        @Field("id_user")id_user:String,
        @Field("goals")goals:String,
        @Field("height")height:String,
        @Field("weight")weight:String,
        @Field("birthdate")birthdate:String,
        @Field("gender")gender:String,
        @Field("activityLevel")activityLevel:String,
        @Field("disease")disease:List<String>,
        @Field("favoriteFood")favoriteFood:List<String>,
    ):Response<ResponsStatus>




}