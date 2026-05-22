package com.example.appsolar.Model

data class ChatRequest(

    val message: String,

    val targetType: String,

    val name: String,

    val companyType: String,

    val monthlyConsumptionKwh: Int,

    val companySize: Int,

    val mainLoads: List<String>,

    val tariffCopKwh: Int,

    val operatingHoursPerDay: Int
)

data class ChatResponse(
    val success: Boolean,
    val data: ChatData
)

data class ChatData(
    val reply: String,
    val timestamp: String
)
