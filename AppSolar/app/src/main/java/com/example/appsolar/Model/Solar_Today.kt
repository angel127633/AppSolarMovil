package com.example.appsolar.Model

data class SolarToday(
    val success: Boolean,
    val data: SolarData
)

data class SolarData(
    val date : String,
    val location : String,
    val radiationKwhM2 : Double,
    val temperatureC : Double,
    val windSpeedKmh : Double,
    val uvIndex : Double,
    val solarIndex : Int,
    val solarIndexLabel : String,
    val sunrise : String,
    val sunset : String,
    val optimalHours : List<String>,
    val peakCostHours : List<String>,
    val cached : Boolean
)

data class SolarScore(
    val success : Boolean,
    val data : ScoreData
)

data class ScoreData(
    val summary : String
)