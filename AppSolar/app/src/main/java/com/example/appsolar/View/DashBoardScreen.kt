package com.example.appsolar.View

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appsolar.Model.ParsedDate
import com.example.appsolar.Model.RecommendationRequest
import com.example.appsolar.Model.getSolarStatus
import com.example.appsolar.Model.parseSolarDate
import com.example.appsolar.ViewModel.ForecastViewModel
import com.example.appsolar.ViewModel.SolarViewModel
import com.example.appsolar.ViewModel.ViewModelRecomendations
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@SuppressLint("ContextCastToActivity")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    modifier: Modifier = Modifier,
    solarViewModel: SolarViewModel = viewModel(),
    viewModelRecomendations: ViewModelRecomendations = viewModel(),
    forecastViewModel: ForecastViewModel = viewModel()
) {

    val data by solarViewModel.data.collectAsState()
    val forecastData by forecastViewModel.forecast.collectAsState()
    val notifications by viewModelRecomendations.notifications.collectAsState()
    val scoreData by solarViewModel.dataScore.collectAsState()
    val recomendationsData by viewModelRecomendations.recomendationsData.collectAsState()
    val isLoading by viewModelRecomendations.isLoading.collectAsState()
    var expandedTipPoblacion by remember { mutableStateOf(false) }
    var valueTipPoblacion by remember { mutableStateOf("Empresa") }
    val list = listOf(
        "Comunidad",
        "Empresa"
    )
    var visible by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    var currentNotificationIndex by remember { mutableStateOf(0) }
    val context = LocalContext.current as Activity
    var show by remember { mutableStateOf(false) }

    LaunchedEffect(notifications) {

        if (notifications.isNotEmpty()) {

            while (currentNotificationIndex < notifications.size) {

                visible = true
                delay(4000) // tiempo visible

                visible = false
                delay(1000) // espera animación

                currentNotificationIndex++
            }
        }
    }

    LaunchedEffect(valueTipPoblacion) {
        val request = RecommendationRequest(
            targetType = "company",
            name = "Hotel Majayura",
            companyType = "hotel",
            monthlyConsumptionKwh = 12000,
            companySize = 18,
            peakUsageHours = "18:00-22:00",
            mainLoads = listOf(
                "aire acondicionado (30% consumo)",
                "lavandería industrial (8%)",
                "refrigeración cocina (12%)",
                "iluminación (15%)"
            ),
            tariffCopKwh = 1050,
            operatingHoursPerDay = 24
        )
        if (valueTipPoblacion == "Empresa") {
            viewModelRecomendations.getRecomendations(request)
        }
    }

    Box(
        modifier
            .fillMaxSize()
            .background(Color(0xFF010B1B))
            .statusBarsPadding()
            .padding(start = 15.dp, end = 15.dp)
            .clickable {
                showNotifications = false
            }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    {
                        context.finishAffinity()
                    },
                    modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.ExitToApp, null,
                        tint = Color.White
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        data?.location ?: "Bogota,Colombia",
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    val fecha = data?.date?.let { formatDate(it) }
                    Text(
                        "Actualizado: ${fecha ?: "21/05/2026"}",
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
                if (notifications.isNotEmpty()) {
                    BadgedBox(badge = {
                        Badge(
                            containerColor = Color.Red,
                            contentColor = Color.White,
                            modifier = modifier.size(17.dp)
                        ) {
                            Text(
                                "${notifications.size}",
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }) {
                        IconButton(
                            {
                                showNotifications = !showNotifications
                            },
                            modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Default.Notifications, null,
                                tint = Color.White
                            )
                        }
                    }
                } else {
                    IconButton(
                        {},
                        modifier.size(28.dp)
                    ) {
                        Icon(
                            Icons.Default.Notifications, null,
                            tint = Color.White
                        )
                    }
                }
            }
            ExposedDropdownMenuBox(
                expanded = expandedTipPoblacion,
                onExpandedChange = { expandedTipPoblacion = !expandedTipPoblacion }
            ) {
                OutlinedTextField(
                    value = valueTipPoblacion,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    label = {
                        Text("Poblacion")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        unfocusedLabelColor = Color.Gray,
                        focusedTrailingIconColor = Color.Gray,
                        unfocusedTrailingIconColor = Color.Gray,
                        focusedBorderColor = Color.Gray.copy(0.5f),
                        unfocusedBorderColor = Color.Gray.copy(0.5f)
                    ),
                    modifier = modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expandedTipPoblacion)
                    }
                )
                ExposedDropdownMenu(
                    expandedTipPoblacion,
                    onDismissRequest = { expandedTipPoblacion = false }) {
                    list.forEach {
                        DropdownMenuItem(text = { Text(it) }, onClick = {
                            expandedTipPoblacion = false
                            valueTipPoblacion = it

                            recomendationsData?.recommendations = emptyList()

                            if (valueTipPoblacion == "Comunidad") {
                                val request = RecommendationRequest(
                                    targetType = "community",
                                    name = "Riohacha",
                                    populationEstimate = 246000,
                                    mainProblems = listOf(
                                        "altos costos eléctricos",
                                        "picos de demanda",
                                        "apagones frecuentes"
                                    )
                                )
                                viewModelRecomendations.getRecomendations(request)
                            } else {
                                val request = RecommendationRequest(
                                    targetType = "company",
                                    name = "Hotel Majayura",
                                    companyType = "hotel",
                                    monthlyConsumptionKwh = 12000,
                                    companySize = 18,
                                    peakUsageHours = "18:00-22:00",
                                    mainLoads = listOf(
                                        "aire acondicionado (30% consumo)",
                                        "lavandería industrial (8%)",
                                        "refrigeración cocina (12%)",
                                        "iluminación (15%)"
                                    ),
                                    tariffCopKwh = 1050,
                                    operatingHoursPerDay = 24
                                )
                                viewModelRecomendations.getRecomendations(request)
                            }
                        })
                    }
                }
            }
            LazyColumn(modifier.fillMaxHeight(0.87f)) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Row() {
                            Box(
                                modifier
                                    .fillMaxWidth(0.65f)
                                    .height(240.dp)
                                    .border(
                                        2.dp, Color.Gray.copy(alpha = 0.5f),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 10.dp,
                                        bottom = 10.dp
                                    )
                            ) {
                                Column(
                                    modifier.fillMaxSize(),
                                    verticalArrangement = Center
                                ) {
                                    Text(
                                        "Pronostico del dia de hoy",
                                        fontSize = 16.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier.height(15.dp))
                                    Row(
                                        modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val solarIndex = data?.solarIndex ?: 0

                                        val animatedProgress by animateFloatAsState(
                                            targetValue = (solarIndex / 100f) * 180f,
                                            animationSpec = tween(
                                                durationMillis = 1500
                                            ),
                                            label = ""
                                        )

                                        val colorAnimated by animateColorAsState(
                                            targetValue = when {
                                                solarIndex <= 30 -> Color.Red
                                                solarIndex <= 70 -> Color.Yellow
                                                else -> Color.Green
                                            },
                                            animationSpec = tween(1500),
                                            label = ""
                                        )

                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(170.dp)
                                                .padding(top = 20.dp, start = 10.dp)
                                        ) {

                                            Canvas(
                                                modifier = Modifier.fillMaxSize()
                                            ) {

                                                val stroke = 30f

                                                // fondo
                                                drawArc(
                                                    color = Color.Gray.copy(alpha = 0.3f),
                                                    startAngle = 180f,
                                                    sweepAngle = 180f,
                                                    useCenter = false,
                                                    style = Stroke(
                                                        width = stroke,
                                                        cap = StrokeCap.Round
                                                    )
                                                )

                                                // progreso animado
                                                drawArc(
                                                    color = colorAnimated,
                                                    startAngle = 180f,
                                                    sweepAngle = animatedProgress,
                                                    useCenter = false,
                                                    style = Stroke(
                                                        width = stroke,
                                                        cap = StrokeCap.Round
                                                    )
                                                )
                                            }

                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {

                                                Text(
                                                    text = solarIndex.toString(),
                                                    fontSize = 45.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )

                                                Text(
                                                    text = data?.solarIndexLabel ?: "Sin datos",
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = colorAnimated
                                                )
                                            }
                                        }
                                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                            Box(
                                                modifier
                                                    .clip(RoundedCornerShape(15.dp))
                                                    .background(Color(0xFF208A42))
                                                    .padding(
                                                        start = 7.dp,
                                                        end = 7.dp,
                                                        top = 1.dp,
                                                        bottom = 1.dp
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    data?.solarIndexLabel ?: "Mal clima",
                                                    fontSize = 15.sp,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Text(
                                                scoreData?.summary ?: "Sin descripcion",
                                                fontSize = 13.sp,
                                                color = Color.White,
                                                modifier = modifier.width(200.dp),
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = TextAlign.Justify
                                            )
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                            ) {
                                                IconsInformation(
                                                    icon = Icons.Default.WbSunny,
                                                    text = "${data?.radiationKwhM2 ?: 0}",
                                                    colorIcons = Color.Yellow
                                                )
                                                IconsInformation(
                                                    icon = Icons.Default.Thermostat,
                                                    text = "${data?.temperatureC ?: 0}°C",
                                                    colorIcons = Color.White
                                                )
                                                IconsInformation(
                                                    icon = Icons.Default.WindPower,
                                                    text = "${data?.windSpeedKmh ?: 0} Iomh",
                                                    colorIcons = Color.Green
                                                )
                                                IconsInformation(
                                                    icon = Icons.Default.WbSunny,
                                                    text = "${data?.uvIndex ?: 0}",
                                                    colorIcons = Color(0xFFFF5722)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier.width(10.dp))
                            Box(
                                modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                                    .border(
                                        2.dp, Color.Gray.copy(alpha = 0.5f),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 10.dp,
                                        bottom = 10.dp
                                    )
                            ) {
                                Column() {
                                    Text(
                                        "Impacto económico",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(modifier.height(10.dp))

                                    Text(
                                        "Ahorro estimado de hoy",
                                        fontSize = 15.sp,
                                        color = Color.Gray
                                    )

                                    Row(
                                        verticalAlignment = Alignment.Bottom,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text(
                                            "$${
                                                formatCurrency(
                                                    value = recomendationsData?.totalSavingsCopDay ?: 80000
                                                )
                                            }",
                                            fontSize = 25.sp,
                                            color = Color.Green,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Text(
                                            "COP",
                                            fontSize = 13.sp,
                                            color = Color.Gray,
                                            modifier = modifier.padding(bottom = 1.dp)
                                        )
                                    }

                                    Spacer(modifier.height(10.dp))

                                    Text(
                                        "Ahorro estimado del mes",
                                        fontSize = 15.sp,
                                        color = Color.Gray
                                    )
                                    Row(
                                        verticalAlignment = Alignment.Bottom,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text(
                                            "$${
                                                formatCurrency(recomendationsData?.totalSavingsCopMonth ?: 2475000)
                                            }",
                                            fontSize = 25.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            "COP",
                                            fontSize = 13.sp,
                                            color = Color.Gray,
                                            modifier = modifier.padding(bottom = 1.dp)
                                        )
                                    }
                                    Spacer(modifier.height(10.dp))
                                    Box(
                                        modifier
                                            .fillMaxWidth()
                                            .height(40.dp)
                                            .border(
                                                2.dp, Color.Green.copy(alpha = 0.5f),
                                                RoundedCornerShape(10.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.TrendingUp, null,
                                                tint = Color.Green, modifier = modifier.size(30.dp)
                                            )
                                            Spacer(modifier.width(5.dp))
                                            Text(
                                                "+18% vs promedio diario",
                                                color = Color.Green,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Column() {
                            Text(
                                recomendationsData?.name ?: "Compañia",
                                fontSize = 20.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            val date = recomendationsData?.date?.let { formatDate(it) }
                            Text(
                                "Fecha: ${date ?: "21/05/2026"}",
                                fontSize = 13.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier.height(10.dp))
                            Box(
                                modifier
                                    .fillMaxWidth()
                                    .height(500.dp)
                                    .border(2.dp, Color.Gray.copy(0.5f), RoundedCornerShape(10.dp))
                                    .padding(
                                        start = 15.dp,
                                        end = 15.dp,
                                        top = 15.dp,
                                        bottom = 15.dp
                                    )
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                                    Row(
                                        modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Recomendaciones",
                                            color = Color.White,
                                            fontSize = 23.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Icon(
                                            Icons.Default.ArrowForward, null,
                                            tint = Color.Green
                                        )
                                    }
                                    if (isLoading) {
                                        Box(
                                            modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    } else {
                                        LazyRow() {
                                            items(
                                                recomendationsData?.recommendations ?: emptyList()
                                            ) {
                                                Card(
                                                    modifier
                                                        .width(232.dp)
                                                        .height(420.dp),
                                                    border = BorderStroke(2.dp, Color.Gray),
                                                    shape = RoundedCornerShape(10.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = Color.Transparent
                                                    )
                                                ) {
                                                    Column(
                                                        modifier
                                                            .fillMaxSize()
                                                            .padding(
                                                                start = 15.dp,
                                                                end = 15.dp,
                                                                top = 20.dp,
                                                                bottom = 10.dp
                                                            ),
                                                        verticalArrangement = Arrangement.spacedBy(
                                                            10.dp
                                                        ),
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        Text(
                                                            it.title,
                                                            fontSize = 14.sp,
                                                            color = Color.White,
                                                            textAlign = TextAlign.Center,
                                                            fontWeight = FontWeight.Bold,
                                                            maxLines = 2,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                        Text(
                                                            it.description,
                                                            fontSize = 12.sp,
                                                            color = Color.White,
                                                            textAlign = TextAlign.Justify,
                                                            maxLines = 5,
                                                            overflow = TextOverflow.Ellipsis,
                                                            modifier = modifier.height(135.dp)
                                                        )
                                                        Box(
                                                            modifier
                                                                .width(70.dp)
                                                                .height(30.dp)
                                                                .clip(RoundedCornerShape(15.dp))
                                                                .background(
                                                                    if (it.priority == "alta") Color(
                                                                        0xFF4CAF50
                                                                    ) else if (it.priority == "media") Color(
                                                                        0xFFB78602
                                                                    ) else Color(0xFF03A9F4)
                                                                ),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Text(
                                                                it.priority,
                                                                color = Color.White,
                                                                fontSize = 15.sp
                                                            )
                                                        }
                                                        Spacer(modifier.height(3.dp))
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(
                                                                10.dp
                                                            )
                                                        ) {
                                                            Icon(
                                                                Icons.Default.AccessTime, null,
                                                                tint = Color.Gray
                                                            )
                                                            Text(
                                                                it.timeWindow,
                                                                color = Color.Gray,
                                                                fontSize = 14.sp
                                                            )
                                                        }
                                                        Box(
                                                            modifier
                                                                .fillMaxWidth()
                                                                .height(2.dp)
                                                                .clip(RoundedCornerShape(10.dp))
                                                                .background(Color.Gray)
                                                        )
                                                        Text(
                                                            "Ahorro estimado",
                                                            fontSize = 15.sp,
                                                            color = Color.Gray
                                                        )
                                                        Row(
                                                            horizontalArrangement = Arrangement.spacedBy(
                                                                5.dp
                                                            ),
                                                            verticalAlignment = Alignment.Bottom
                                                        ) {
                                                            Text(
                                                                "$${
                                                                    formatCurrency(it.savingsCopDay)
                                                                }",
                                                                color = Color.Green,
                                                                fontSize = 23.sp,
                                                                fontWeight = FontWeight.SemiBold
                                                            )
                                                            Text(
                                                                "COP",
                                                                color = Color.Gray,
                                                                fontSize = 14.sp
                                                            )
                                                        }
                                                    }
                                                }
                                                Spacer(modifier.width(20.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Box(
                            modifier
                                .fillMaxWidth()
                                .height(665.dp)
                                .border(2.dp, Color.Gray.copy(0.5f), RoundedCornerShape(10.dp))
                                .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 10.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ) {
                                Row(
                                    modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.WbSunny, null,
                                            modifier.size(50.dp), tint = Color.Yellow
                                        )
                                        Spacer(modifier.width(10.dp))
                                        Column() {
                                            Text(
                                                "Pronóstico de los próximos ${if (show) 16 else 5} días",
                                                fontSize = 23.sp,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Text(
                                                "Información solar para una mejor planificación",
                                                fontSize = 13.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                    Box(
                                        modifier
                                            .width(190.dp)
                                            .height(60.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .border(
                                                2.dp,
                                                Color.Gray.copy(0.3f),
                                                RoundedCornerShape(10.dp)
                                            )
                                            .background(Color(0xFF02162D))
                                    ) {
                                        Row(
                                            modifier.fillMaxSize(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                Icons.Default.DateRange, null,
                                                tint = Color(0xFF61B4F8),
                                                modifier = modifier.size(40.dp)
                                            )
                                            Spacer(modifier.width(10.dp))
                                            Column(verticalArrangement = Arrangement.Center) {
                                                Text(
                                                    "Actualizado",
                                                    fontSize = 11.sp,
                                                    color = Color.White
                                                )
                                                Text(
                                                    formatDate(
                                                        LocalDateTime.now().format(
                                                            DateTimeFormatter.ofPattern(
                                                                "yyyy-MM-dd'T'HH:mm:ss"
                                                            )
                                                        )
                                                    ),
                                                    fontSize = 17.sp,
                                                    color = Color(0xFF61B4F8)
                                                )
                                            }
                                        }
                                    }
                                }
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    items(
                                        forecastData?.data?.data?.take(if (show) 16 else 5)
                                            ?: emptyList()
                                    ) {
                                        Card(
                                            modifier
                                                .width(300.dp)
                                                .height(500.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color.Transparent
                                            ),
                                            border = BorderStroke(2.dp, Color.Gray.copy(0.5f)),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Column(
                                                modifier
                                                    .fillMaxSize()
                                                    .padding(15.dp)
                                            ) {
                                                Row() {
                                                    Column(
                                                        verticalArrangement = Arrangement.spacedBy(
                                                            15.dp
                                                        )
                                                    ) {
                                                        Box(
                                                            modifier
                                                                .width(70.dp)
                                                                .height(122.dp)
                                                                .clip(RoundedCornerShape(10.dp))
                                                                .background(Color(0xFF06213E)),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            val parseDate = parseSolarDate(it.date)
                                                            Column(
                                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                                verticalArrangement = Arrangement.spacedBy(
                                                                    1.dp
                                                                )
                                                            ) {
                                                                Text(
                                                                    parseDate.dayName,
                                                                    fontSize = 15.sp,
                                                                    color = Color(0xFF61B4F8),
                                                                    fontWeight = FontWeight.SemiBold
                                                                )
                                                                Text(
                                                                    parseDate.dayNumber,
                                                                    fontSize = 35.sp,
                                                                    color = Color.White,
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                                Text(
                                                                    parseDate.monthName,
                                                                    fontSize = 15.sp,
                                                                    color = Color(0xFF55769A),
                                                                    fontWeight = FontWeight.SemiBold
                                                                )
                                                            }
                                                        }
                                                    }
                                                    Spacer(modifier.width(20.dp))
                                                    Column() {
                                                        Box(
                                                            modifier
                                                                .clip(RoundedCornerShape(20.dp))
                                                                .background(Color(0xFF097E10))
                                                                .padding(
                                                                    top = 5.dp,
                                                                    bottom = 5.dp,
                                                                    start = 10.dp,
                                                                    end = 10.dp
                                                                ),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Row(
                                                                verticalAlignment = Alignment.CenterVertically,
                                                                horizontalArrangement = Arrangement.spacedBy(
                                                                    5.dp
                                                                )
                                                            ) {
                                                                Icon(
                                                                    Icons.Default.WbSunny, null,
                                                                    tint = Color.Yellow
                                                                )
                                                                Text(
                                                                    it.solarIndexLabel,
                                                                    color = Color.White,
                                                                    fontWeight = FontWeight.SemiBold,
                                                                    fontSize = 14.sp
                                                                )
                                                            }
                                                        }
                                                        Box(
                                                            modifier.padding(start = 20.dp)
                                                        ) {
                                                            Icon(
                                                                Icons.Default.Cloud,
                                                                null,
                                                                modifier
                                                                    .size(120.dp)
                                                                    .padding(
                                                                        end = 50.dp
                                                                    ),
                                                                tint = Color.White.copy(0.5f)
                                                            )
                                                            Icon(
                                                                Icons.Default.Cloud,
                                                                null,
                                                                modifier
                                                                    .size(120.dp)
                                                                    .padding(start = 40.dp),
                                                                tint = Color.White.copy(0.6f)
                                                            )
                                                            Icon(
                                                                Icons.Default.WbSunny,
                                                                null,
                                                                modifier
                                                                    .size(90.dp)
                                                                    .padding(start = 20.dp),
                                                                tint = Color.Yellow
                                                            )
                                                            Icon(
                                                                Icons.Default.Cloud,
                                                                null,
                                                                modifier
                                                                    .size(90.dp)
                                                                    .padding(start = 10.dp),
                                                                tint = Color.White.copy(0.3f)
                                                            )
                                                        }
                                                    }
                                                }
                                                Box(
                                                    modifier
                                                        .fillMaxWidth()
                                                        .height(3.dp)
                                                        .background(Color.Gray.copy(0.5f))
                                                )
                                                Column(modifier.padding(top = 10.dp)) {
                                                    Row() {
                                                        IconsInformationPronostico(
                                                            icon = Icons.Default.WbSunny,
                                                            colorIcons = Color.Yellow,
                                                            encabezado = "kWH/m2",
                                                            text = it.radiationKwhM2.toString()
                                                        )
                                                        Box(
                                                            modifier
                                                                .height(100.dp)
                                                                .width(2.dp)
                                                                .background(Color.Gray.copy(0.3f))
                                                        )
                                                        IconsInformationPronostico(
                                                            icon = Icons.Default.Thermostat,
                                                            colorIcons = Color.White,
                                                            encabezado = "Temperatura",
                                                            text = "${it.temperatureC}°C"
                                                        )
                                                    }
                                                    Box(
                                                        modifier
                                                            .fillMaxWidth()
                                                            .height(2.dp)
                                                            .background(Color.Gray.copy(0.3f))
                                                    )
                                                    Row() {
                                                        IconsInformationPronostico(
                                                            icon = Icons.Default.WindPower,
                                                            colorIcons = Color.Green,
                                                            encabezado = "km/h",
                                                            text = "${it.windSpeedKmh}"
                                                        )
                                                        Box(
                                                            modifier
                                                                .height(100.dp)
                                                                .width(2.dp)
                                                                .background(Color.Gray.copy(0.3f))
                                                        )
                                                        IconsInformationPronostico(
                                                            icon = Icons.Default.WbSunny,
                                                            colorIcons = Color(0xFFFF5722),
                                                            encabezado = "Indice UV",
                                                            text = "${it.solarIndex}"
                                                        )
                                                    }
                                                }
                                                Spacer(modifier.height(20.dp))
                                                Box(
                                                    modifier
                                                        .fillMaxWidth()
                                                        .height(70.dp)
                                                        .clip(RoundedCornerShape(10.dp))
                                                        .background(
                                                            Color(
                                                                0xFF061F3A
                                                            )
                                                        )
                                                        .padding(10.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Row(
                                                        modifier.fillMaxSize(),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                    ) {
                                                        Icon(
                                                            Icons.Default.TrendingUp,
                                                            null,
                                                            modifier.size(30.dp), tint = Color(
                                                                0xFF2196F3
                                                            )
                                                        )
                                                        Spacer(modifier.width(10.dp))
                                                        Column() {
                                                            val solarStatus =
                                                                getSolarStatus(it.solarIndex)
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Text(
                                                                    "Indice Solar: ",
                                                                    fontSize = 13.sp,
                                                                    color = Color.White
                                                                )
                                                                Text(
                                                                    solarStatus.label,
                                                                    fontSize = 15.sp,
                                                                    color = solarStatus.color
                                                                )
                                                            }
                                                            Text(
                                                                solarStatus.description,
                                                                fontSize = 12.sp,
                                                                color = Color.White
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Box(
                                    modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    TextButton({
                                        show = !show
                                    }) {
                                        Text(
                                            "Ver mas",
                                            color = Color(0xFF2196F3),
                                            fontSize = 20.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally { fullWidth -> fullWidth } + fadeIn(
                animationSpec = tween(
                    800
                )
            ),
            exit = slideOutHorizontally { fullWidth -> fullWidth } + fadeOut(
                animationSpec = tween(
                    800
                )
            )
        ) {
            if (notifications.isNotEmpty() &&
                currentNotificationIndex < notifications.size
            ) {

                val notification = notifications[currentNotificationIndex]

                Box(
                    modifier.padding(top = 40.dp)
                ) {
                    Column(
                        modifier
                            .fillMaxWidth()
                            .height(93.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(2.dp, Color.Gray.copy(0.5f), RoundedCornerShape(10.dp))
                            .background(Color.Black.copy(0.7f))
                            .padding(start = 15.dp, top = 10.dp, bottom = 10.dp, end = 10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.AddAlert, null,
                                modifier.size(20.dp), tint = Color.Red
                            )
                            Text(
                                "¡Alerta!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier.height(5.dp))
                        Text(
                            notification.message,
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Justify,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = showNotifications,
            enter = slideInHorizontally { fullWidth -> fullWidth } + fadeIn(
                animationSpec = tween(
                    800
                )
            ),
            exit = slideOutHorizontally { fullWidth -> fullWidth } + fadeOut(
                animationSpec = tween(
                    800
                )
            )
        ) {
            Box(
                modifier
                    .padding(top = 40.dp)
                    .clickable {
                        showNotifications = false
                    }
            ) {
                Column(
                    modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                        .border(2.dp, Color.Gray.copy(0.5f), RoundedCornerShape(10.dp))
                        .background(Color.Black.copy(0.9f))
                        .padding(start = 15.dp, top = 10.dp, bottom = 10.dp, end = 15.dp)
                ) {
                    Text(
                        "Notificaciones",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                    Spacer(modifier.height(10.dp))
                    LazyColumn(modifier.padding(10.dp)) {
                        items(notifications) {
                            Card(
                                modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 100.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    Color.Gray.copy(0.5f)
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(
                                    modifier
                                        .fillMaxSize()
                                        .padding(10.dp)
                                ) {
                                    Row(
                                        modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.AddAlert, null,
                                                modifier.size(20.dp), tint = Color.Red
                                            )
                                            Text(
                                                "¡Alerta!",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                        Text(
                                            "Fecha: ${it.date}",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    }
                                    Spacer(modifier.height(5.dp))
                                    Text(
                                        it.message,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Justify
                                    )
                                }
                            }
                            Spacer(modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IconsInformation(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    colorIcons: Color
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon, null,
            tint = colorIcons
        )
        Text(
            text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }

}

@Composable
fun IconsInformationPronostico(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    encabezado: String,
    colorIcons: Color
) {

    Box(
        modifier
            .width(130.dp)
            .height(100.dp)
            .padding(10.dp)
    ) {
        Row(
            modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon, null,
                tint = colorIcons, modifier = modifier.size(30.dp)
            )
            Spacer(modifier.width(10.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    encabezado,
                    color = Color.White,
                    fontSize = 10.sp,
                    lineHeight = 13.sp
                )
            }
        }
    }
}

fun formatCurrency(value: Int): String {

    return NumberFormat
        .getNumberInstance(Locale("es", "CO"))
        .format(value)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(date: String): String {

    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val localDate = LocalDateTime.parse(date, inputFormatter)

    return localDate.format(outputFormatter)
}