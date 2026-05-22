package com.example.appsolar.Model

data class ForecastData(
    val location: String,
    val source: String,
    val days: Int,
    val averageRadiation: Double,
    val maxRadiation: Double,
    val minRadiation: Double,
    val data: List<SolarDay>
)

data class SolarDay(
    val date: String,
    val radiationKwhM2: Double,
    val temperatureC: Double,
    val windSpeedKmh: Double,
    val solarIndex: Int,
    val solarIndexLabel: String
)

data class ForecastResponse(
    val success: Boolean,
    val data: ForecastData
)