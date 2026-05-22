package com.example.appsolar.Model

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val client =
        OkHttpClient.Builder()

            .connectTimeout(40, TimeUnit.SECONDS)

            .readTimeout(40, TimeUnit.SECONDS)

            .writeTimeout(40, TimeUnit.SECONDS)

            .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.4.61.163:5016/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

}