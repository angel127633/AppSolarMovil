package com.example.appsolar.View

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appsolar.Model.RecommendationRequest
import com.example.appsolar.ViewModel.ForecastViewModel
import com.example.appsolar.ViewModel.SolarViewModel
import com.example.appsolar.ViewModel.ViewModelRecomendations
import kotlinx.coroutines.delay
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PredictiveEnergyScreen(
    solarViewModel: SolarViewModel = viewModel(),
    recommendationViewModel: ViewModelRecomendations = viewModel(),
    forecastViewModel: ForecastViewModel = viewModel()
) {
    val solarDataState by solarViewModel.data.collectAsState()
    val scoreDataState by solarViewModel.dataScore.collectAsState()
    val recommendationState by recommendationViewModel.recomendationsData.collectAsState()
    val forecastState by forecastViewModel.forecast.collectAsState()

    val isSolarLoading by solarViewModel.isLoading.collectAsState()
    val isRecLoading by recommendationViewModel.isLoading.collectAsState()
    val isForecastLoading by forecastViewModel.isLoading.collectAsState()
    val name = recommendationState?.name ?: "Hotel Majayura"
    var selectedTarget by remember { mutableStateOf("Empresa") }
    var selectedTimeline by remember { mutableStateOf("Hoy") }

    // ==========================================================
    // 🤖 MOTOR DE IA: SIMULACIÓN DE ESTADOS DE RED
    // ==========================================================
    var systemStatusMode by remember { mutableStateOf("OPTIMO") }
    var liveMicroFluctuation by remember { mutableStateOf(0) }
    var isAnalyzingText by remember { mutableStateOf("Algoritmo predictivo optimizando red...") }

    LaunchedEffect(Unit) {
        var minutesElapsed = 0
        while (true) {
            liveMicroFluctuation = Random.nextInt(-1, 2)
            when (minutesElapsed) {
                0 -> {
                    systemStatusMode = "OPTIMO"
                    isAnalyzingText = "Sistema estable. Inversores operando a máxima capacidad."
                }

                2 -> {
                    systemStatusMode = "MODERADO"
                    isAnalyzingText =
                        "Aviso: Nubosidad detectada en nodo local. Ajustando inyección."
                }

                4 -> {
                    systemStatusMode = "CRITICO"
                    isAnalyzingText =
                        "¡ALERTA CRÍTICA! Desviación de consumo y caída severa de eficiencia."
                }

                6 -> {
                    minutesElapsed = -1
                }
            }
            delay(60000)
            minutesElapsed++
        }
    }

    LaunchedEffect(selectedTarget) {
        if (selectedTarget == "Comunidad") {
            recommendationViewModel.getRecomendations(
                RecommendationRequest(
                    targetType = "community",
                    name = "Riohacha",
                    populationEstimate = 150000,
                    mainProblems = listOf("Cortes de energía", "Tarifa alta")
                )
            )
        } else {
            recommendationViewModel.getRecomendations(
                RecommendationRequest(
                    targetType = "company",
                    name = "Restaurante Sazón Guajira",
                    companyType = "Gastronomía",
                    monthlyConsumptionKwh = 4500
                )
            )
        }
    }

    // EXTRAER DATOS BASE DE LA API
    val baseApiSolarIndex = recommendationState?.solarIndex ?: solarDataState?.solarIndex ?: 92
    val apiSummary = scoreDataState?.summary ?: "Análisis predictivo de la red de distribución."
    val targetOffset = if (selectedTarget == "Comunidad") -4 else 3

    // 1. Calcular el Score Principal del Centro según las fluctuaciones en vivo
    val computedScore = when (systemStatusMode) {
        "OPTIMO" -> (baseApiSolarIndex + targetOffset + liveMicroFluctuation).coerceIn(82, 100)
        "MODERADO" -> (68 + targetOffset + liveMicroFluctuation)
        "CRITICO" -> (35 + targetOffset + liveMicroFluctuation)
        else -> baseApiSolarIndex
    }

    val animatedScore by animateIntAsState(targetValue = computedScore, label = "scoreAnimation")

    val currentDynamicColor by animateColorAsState(
        targetValue = when {
            animatedScore >= 78 -> Color(0xFF10B981)
            animatedScore >= 60 -> Color(0xFFF59E0B)
            else -> Color(0xFFEF4444)
        },
        label = "colorStatusAnimation"
    )

    val isCriticalState = animatedScore < 60
    val dynamicScoreLabel = when {
        animatedScore >= 78 -> "Rendimiento Óptimo"
        animatedScore >= 60 -> "Precaución / Consumo Alto"
        else -> "Riesgo Crítico de Red"
    }

    // ==========================================================
    // 📊 NUEVO MOTOR MATEMÁTICO EN PASOS PROPORCIONALES AL TIEMPO Y AL CLIMA
    // ==========================================================
    val isSolarIndexFavorable = animatedScore >= 70

    // Factores de crecimiento progresivo por pasos según la pestaña
    val (efficiencyStep, riskStep) = when (selectedTimeline) {
        "Hoy" -> Pair(0, 0)
        "Mañana" -> Pair(2, 3)
        "Mes" -> Pair(5, 8)
        "Año" -> Pair(9, 15) // En un año el impacto acumulado (positivo o negativo) es mayor
        else -> Pair(0, 0)
    }

    val dynamicFollowScore: Int
    val dynamicIgnoreScore: Int

    if (isSolarIndexFavorable) {
        // SI EL SOLAR INDEX ES BUENO (Ej: 93): La eficiencia sube progresivamente y el riesgo baja
        dynamicFollowScore =
            (animatedScore + efficiencyStep + liveMicroFluctuation).coerceIn(40, 100)
        dynamicIgnoreScore = (20 - efficiencyStep + liveMicroFluctuation).coerceIn(
            5,
            100
        ) // Riesgo bajo que cae más con los días
    } else {
        // SI EL SOLAR INDEX ES MALO (Ej: 35 o 50): La eficiencia cae por el clima y el riesgo sube por pasos con los días
        dynamicFollowScore =
            (animatedScore - efficiencyStep + liveMicroFluctuation).coerceIn(10, 100)
        // El riesgo escala: si hoy es 35, en un mes será 43 (35+8), en un año será 50 (35+15)
        dynamicIgnoreScore = (animatedScore + riskStep + liveMicroFluctuation).coerceIn(15, 98)
    }

    // ==========================================================
    // 💰 CALCULADORA FINANCIERA CORRELACIONADA
    // ==========================================================
    val baseSavingsDay = recommendationState?.totalSavingsCopDay ?: 48500
    val baseSavingsMonth = recommendationState?.totalSavingsCopMonth ?: 1455000

    val selectedPeriodBase = when (selectedTimeline) {
        "Hoy" -> baseSavingsDay
        "Mañana" -> (baseSavingsDay * 1.05).toInt()
        "Mes" -> baseSavingsMonth
        "Año" -> baseSavingsMonth * 12
        else -> baseSavingsDay
    }

    val financialEfficiencyFactor = (animatedScore / 100f).coerceIn(0.2f, 1.0f)
    val dynamicHeroSavings = if (!isCriticalState) {
        (selectedPeriodBase * financialEfficiencyFactor).toInt()
    } else {
        (selectedPeriodBase * 0.15).toInt()
    }

    val simulatedGainAmount =
        (selectedPeriodBase * (dynamicFollowScore / 100f).coerceIn(0.5f, 1.2f)).toInt()
    val simulatedLossAmount = (selectedPeriodBase * (1.0f - (dynamicIgnoreScore / 100f)).coerceIn(
        0.2f,
        0.9f
    ) * (if (selectedTarget == "Comunidad") 0.65f else 0.45f)).toInt()
    // ==========================================================

    if (isSolarLoading || isRecLoading || isForecastLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF09090B)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF10B981))
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF09090B))
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            TargetSelector(
                selectedTarget = selectedTarget,
                onTargetSelected = { selectedTarget = it })
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                LiveAiStatusBar(
                    statusText = isAnalyzingText,
                    statusColor = currentDynamicColor
                )
            }

            item {
                HeroEnergySection(
                    target = selectedTarget,
                    score = animatedScore,
                    scoreLabel = dynamicScoreLabel,
                    summary = apiSummary,
                    statusColor = currentDynamicColor,
                    criticalState = isCriticalState,
                    projectedSavings = dynamicHeroSavings,
                    timeline = selectedTimeline,
                    name = name
                )
            }

            item {
                TimelineSimulationSection(
                    selected = selectedTimeline,
                    onSelected = { selectedTimeline = it })
            }

            item {
                PredictionMetricsSection(
                    followScore = dynamicFollowScore,
                    ignoreScore = dynamicIgnoreScore,
                    gainAmount = simulatedGainAmount,
                    lossAmount = simulatedLossAmount,
                    timeline = selectedTimeline
                )
            }

            item {
                ActionPlanSection(
                    criticalState = isCriticalState, statusColor = currentDynamicColor,
                    recommendations = recommendationState?.recommendations ?: emptyList()
                )
            }
        }
    }
}

@Composable
private fun TargetSelector(selectedTarget: String, onTargetSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF18181B), RoundedCornerShape(16.dp))
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val targets = listOf("Empresa", "Comunidad")
        targets.forEach { target ->
            val isSelected = selectedTarget == target
            val bgContainer by animateColorAsState(
                if (isSelected) Color(0xFF27272A) else Color.Transparent,
                label = ""
            )
            val contentColor by animateColorAsState(
                if (isSelected) Color(0xFF10B981) else Color.Gray,
                label = ""
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(bgContainer, RoundedCornerShape(12.dp))
                    .clickable { onTargetSelected(target) }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (target == "Empresa") Icons.Default.Business else Icons.Default.Groups,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = target,
                    color = if (isSelected) Color.White else Color.Gray,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun LiveAiStatusBar(statusText: String, statusColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF18181B)),
        border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .size(8.dp)
                .background(statusColor, RoundedCornerShape(50)))
            Spacer(Modifier.width(10.dp))
            Text(
                text = statusText,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun HeroEnergySection(
    target: String, score: Int, scoreLabel: String, summary: String, statusColor: Color,
    criticalState: Boolean, projectedSavings: Int, timeline: String, name: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF18181B))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${name.uppercase()} - $target",
                color = Color.Gray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))

            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(135.dp)) {
                CircularProgressIndicator(
                    progress = { score / 100f }, modifier = Modifier.fillMaxSize(),
                    color = statusColor, strokeWidth = 10.dp, trackColor = Color(0xFF27272A)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$score%",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(text = "Lectura de Red", color = Color.Gray, fontSize = 10.sp)
                }
            }

            Spacer(Modifier.height(14.dp))
            Text(
                text = scoreLabel,
                color = statusColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = summary,
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF09090B), RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFF27272A), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (criticalState) "Pérdida por Desviación Operativa ($timeline)" else "Retorno Energético Estimado ($timeline)",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "$${String.format("%,d", projectedSavings)} COP",
                            color = statusColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(
                        imageVector = if (criticalState) Icons.Default.TrendingDown else Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineSimulationSection(selected: String, onSelected: (String) -> Unit) {
    val items = listOf("Hoy", "Mañana", "Mes", "Año")
    Column {
        Text(
            text = "PROYECCIONES TEMPORALES INTERACTIVAS",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items.forEach { item ->
                val isSelected = selected == item
                FilterChip(
                    selected = isSelected, onClick = { onSelected(item) },
                    label = { Text(item, color = if (isSelected) Color.Black else Color.White) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(
                            0xFF10B981
                        ), containerColor = Color(0xFF18181B)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = Color(0xFF27272A)
                    )
                )
            }
        }
    }
}

@Composable
private fun PredictionMetricsSection(
    followScore: Int,
    ignoreScore: Int,
    gainAmount: Int,
    lossAmount: Int,
    timeline: String
) {
    Column {
        Text(
            text = "IMPACTO FINANCIERO ESTIMADO",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF111827))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "EFICIENCIA ALTA",
                            color = Color.LightGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "$followScore%",
                        color = Color(0xFF10B981),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Ahorro Esperado ($timeline):",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "+$${String.format("%,d", gainAmount)} COP",
                        color = Color(0xFF10B981),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1212))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "RIESGO RED",
                            color = Color.LightGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "$ignoreScore%",
                        color = Color(0xFFEF4444),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Costo de Oportunidad ($timeline):",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "-$${String.format("%,d", lossAmount)} COP",
                        color = Color(0xFFEF4444),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionPlanSection(
    criticalState: Boolean,
    statusColor: Color,
    recommendations: List<com.example.appsolar.Model.Recommendation>
) {
    val containerBg = if (criticalState) Color(0xFF2D1515) else Color(0xFF18181B)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (criticalState) Icons.Default.Warning else Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = if (criticalState) Color(0xFFEF4444) else Color(0xFFF59E0B)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (criticalState) "PLAN DE CONTINGENCIA DE RED" else "ESTRATEGIA RECOMENDADA POR IA",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (criticalState) "El motor analítico ha desplegado protocolos forzados de mitigación para contrarrestar el pico de consumo y estabilizar los inversores:"
                else "Optimiza tu infraestructura siguiendo estas pautas calculadas para las variables meteorológicas del entorno:",
                color = Color.LightGray, fontSize = 12.sp
            )
            Spacer(Modifier.height(16.dp))
            if (criticalState) {
                val emergencyPlans = listOf(
                    "Desconexión automática de compresores no esenciales",
                    "Aislamiento térmico preventivo de almacenamiento por baterías",
                    "Supresión de cargas reactivas pesadas en horas pico"
                )
                emergencyPlans.forEach { plan -> RecommendationRow(text = plan, isAlert = true) }
            } else {
                recommendations.forEach { recommendation ->
                    RecommendationRow(
                        text = "${recommendation.title} (${recommendation.timeWindow}) -> +$${recommendation.savingsCopDay} COP",
                        isAlert = false
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendationRow(text: String, isAlert: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .background(Color(0xFF09090B).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isAlert) Icons.Default.Cancel else Icons.Default.CheckCircle,
            contentDescription = null,
            tint = if (isAlert) Color(0xFFEF4444) else Color(0xFF10B981),
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(text = text, color = Color.White, fontSize = 12.sp)
    }
}