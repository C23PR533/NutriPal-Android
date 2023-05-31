package com.example.nutripal.network


import com.example.nutripal.network.response.ResponsStatus
import com.example.nutripal.network.response.datadiri.ResponseDataDiri
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @GET("datadiri")
    suspend fun getDatadiri(): Response<ResponseDataDiri>

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




}