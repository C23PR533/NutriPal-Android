package com.example.nutripal.network



import com.example.nutripal.network.response.ResponsStatus
import com.example.nutripal.network.response.datadiri.ResponseDataDiri
import com.example.nutripal.network.response.food.ResponseFoods
import com.example.nutripal.network.response.foodid.ResponseFoodId
import com.example.nutripal.network.response.historiaktifitas.ResponseHistoryAktifitas
import com.example.nutripal.network.response.search.ResponseSearch
import com.example.nutripal.network.response.userpreference.ResponseUserPreferences
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @GET("foods")
    suspend fun getListFood(): Response<ResponseFoods>

    @GET("foods/{food_id}")
    suspend fun getFoodbyId(
        @Path("food_id")food_id:String
    ): Response<ResponseFoodId>

    @GET("foods/search/{food_name}")
    suspend fun getSearchFood(
        @Path("food_name")food_name:String
    ): Response<ResponseSearch>

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


    @GET("history_aktifitas/{id_user}")
    suspend fun getHistoryAktifitas(
        @Path("id_user")id_user: String
    ):Response<ResponseHistoryAktifitas>
    @FormUrlEncoded
    @POST("history_aktifitas")
    suspend fun postHistoryAktifitas(
        @Field("id_user")id_user:String,
        @Field("tanggal")tanggal:String,
        @Field("sisa_kalori")sisa_kalori:String,
        @Field("kalori_harian")kalori_harian:String,
        @Field("id_makanan")id_makanan:String,
        @Field("nama_makanan")nama_makanan:String,
        @Field("kalori")kalori:String,
        @Field("waktu")waktu:String,
    ):Response<ResponsStatus>




}