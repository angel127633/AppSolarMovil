package com.example.appsolar.Model

import androidx.compose.ui.graphics.Color

data class SolarStatus(
    val label: String,
    val description: String,
    val color: Color
)

fun getSolarStatus(index: Int): SolarStatus {

    return when(index) {

        in 0..30 -> SolarStatus(
            label = "Bajo",
            description = "Baja generación solar",
            color = Color.Red
        )

        in 31..60 -> SolarStatus(
            label = "Medio",
            description = "Generación moderada",
            color = Color.Yellow
        )

        in 61..80 -> SolarStatus(
            label = "Alto",
            description = "Alta eficiencia solar",
            color = Color.Green
        )

        else -> SolarStatus(
            label = "Excelente",
            description = "Condiciones óptimas",
            color = Color(0xFF00E676)
        )

    }

}