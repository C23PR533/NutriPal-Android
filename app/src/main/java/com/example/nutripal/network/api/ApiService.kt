package com.example.nutripal.network



import com.example.nutripal.network.response.ResponsStatus
import com.example.nutripal.network.response.datadiri.ResponseDataDiri
import com.example.nutripal.network.response.datadiri.ResponseUploadFoto
import com.example.nutripal.network.response.favorites.ResponseFoodsFavorite
import com.example.nutripal.network.response.food.ResponseFoodsAll
import com.example.nutripal.network.response.foodid.ResponseFoodId
import com.example.nutripal.network.response.historiaktifitas.ResponseHistoryAktifitas
import com.example.nutripal.network.response.login.ResponseLogin
import com.example.nutripal.network.response.search.ResponseSearch
import com.example.nutripal.network.response.userpreference.ResponseUserPreferences
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @GET("foodsFavorite/{id_user}/{id_food}")
    suspend fun getFoodFavorite(
        @Path("id_user")id_user:String,
        @Path("id_food")id_food:String,
    ):Response<ResponseFoodsFavorite>
    @GET("foodsFavorite/{id_user}")
    suspend fun getListFoodFavorite(
        @Path("id_user")id_user:String,
    ):Response<ResponseFoodsFavorite>


    @FormUrlEncoded
    @POST("foodsFavorite/{id_user}")
    suspend fun postFavoriteFoods(
        @Path("id_user")id_user:String,
        @Field("food_id")food_id:String,
        @Field("food_name")food_name:String,
        @Field("calories")calories:String,
    ):Response<ResponsStatus>

    @DELETE("foodsFavorite/{id_user}/{id_food}")
    suspend fun deleteFoodFav(
        @Path("id_user")id_user:String,
        @Path("id_food")id_food: String,
    ):Response<ResponsStatus>

    @GET("foods/get-json-data/search")
    suspend fun getListFood(): Response<ResponseFoodsAll>

    @GET("foods/get-json-data/search/food_id/{food_id}")
    suspend fun getFoodbyId(
        @Path("food_id")food_id:String
    ): Response<ResponseFoodId>

    @GET("foods/get-json-data/search/{food_name}")
    suspend fun getSearchFood(
        @Path("food_name")food_name:String
    ): Response<ResponseSearch>

    @GET("datadiri/{id_user}")
    suspend fun getDatadiri(
        @Path("id_user")id_user:String
    ): Response<ResponseDataDiri>

    @GET("userpreferences/{id_user}")
    suspend fun getUserPreferences(
//        @Header("Authorization") token: String,
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
    @POST("userpreferences/{id}")
    suspend fun postUserPreferences(
        @Path("id")id:String,
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
    @FormUrlEncoded
    @PUT("userpreferences/{id_user}")
    suspend fun editUserPreferences(
        @Path("id_user")id_user:String,
        @Field("id_user")id:String,
        @Field("goals")goals:String,
        @Field("height")height:String,
        @Field("weight")weight:String,
        @Field("birthdate")birthdate:String,
        @Field("gender")gender:String,
        @Field("activityLevel")activityLevel:String,
        @Field("disease")disease:List<String>,
        @Field("favoriteFood")favoriteFood:List<String>,
    ):Response<ResponsStatus>


    @GET("history_aktifitas/{id_user}/{tgl}")
    suspend fun getHistoryAktifitas(
        @Path("id_user")id_user: String,
        @Path("tgl")tgl: String,
    ):Response<ResponseHistoryAktifitas>
    @FormUrlEncoded
    @POST("history_aktifitas")
    suspend fun postHistoryAktifitas(
        @Field("id_user")id_user:String,
        @Field("tanggal")tanggal:String,
        @Field("kalori_harian")kalori_harian:Int,
        @Field("id_makanan")id_makanan:String,
        @Field("nama_makanan")nama_makanan:String,
        @Field("kalori")kalori:String,
        @Field("waktu")waktu:String,
    ):Response<ResponsStatus>

    @Multipart
    @POST("/datadiri/photoprofile/{id_user}")
    suspend fun uploadFoto(
        @Path("id_user")id_user:String,
        @Part foto: MultipartBody.Part
    ):Response<ResponseUploadFoto>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email")email:String,
        @Field("password")password:String,
    ):Response<ResponseLogin>




}