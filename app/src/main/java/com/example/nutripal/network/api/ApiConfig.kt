package com.example.nutripal.network.api





import com.example.nutripal.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiConfig {

    companion object{
        private val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)


        private val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        fun getApiService(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://nutripal-api-beta-v1-dt3pitfd4q-et.a.run.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}