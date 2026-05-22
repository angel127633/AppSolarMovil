package com.example.appsolar.Model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("solar/today")
    suspend fun getSolarToday(): SolarToday

    @GET("solar/score")
    suspend fun getSolarScore(): SolarScore

    @POST("ai/recommendations")
    suspend fun getRecommendations(@Body request: RecommendationRequest): RecommendationResponse

    @POST("ai/chat")
    suspend fun sendMessage(@Body request: ChatRequest): Response<ChatResponse>

    @GET("solar/forecast")
    suspend fun getForecast(
        @Query("days") days: Int
    ): Response<ForecastResponse>

}