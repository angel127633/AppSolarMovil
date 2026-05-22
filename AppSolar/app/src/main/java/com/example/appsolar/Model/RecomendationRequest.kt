package com.example.appsolar.Model

data class RecommendationRequest(

    val targetType: String,

    val name: String,

    // COMPANY
    val companyType: String? = null,
    val monthlyConsumptionKwh: Int? = null,
    val companySize: Int? = null,
    val peakUsageHours: String? = null,
    val mainLoads: List<String>? = null,

    // COMMUNITY
    val populationEstimate: Int? = null,
    val mainProblems: List<String>? = null,
    val tariffCopKwh : Int? = null,
    val operatingHoursPerDay : Int? = null
)

data class RecommendationResponse(
    val success: Boolean,
    val data: RecommendationData
)

data class RecommendationData(
    val targetType: String,
    val name: String,
    val date: String,
    val radiationToday: Double,
    val solarIndex: Int,
    val reasoning: String,
    val recommendations: List<Recommendation>,
    val totalSavingsCopDay: Int,
    val totalSavingsCopMonth: Int,
    val alert: String?
)

data class Recommendation(
    val title: String,
    val description: String,
    val priority: String,
    val timeWindow: String,
    val savingsCopDay: Int
)
