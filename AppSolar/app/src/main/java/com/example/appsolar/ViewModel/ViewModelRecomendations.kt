package com.example.appsolar.ViewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsolar.Model.NotificationsItem
import com.example.appsolar.Model.Recommendation
import com.example.appsolar.Model.RecommendationData
import com.example.appsolar.Model.RecommendationRequest
import com.example.appsolar.Model.RetrofitClient
import com.example.appsolar.Model.ScoreData
import com.example.appsolar.Model.SolarData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ViewModelRecomendations : ViewModel() {

    private val _recomendationsData = MutableStateFlow<RecommendationData?>(null)
    val recomendationsData: StateFlow<RecommendationData?> = _recomendationsData
    private val _success = MutableStateFlow("hola")
    val success: StateFlow<String> = _success
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _notifications = MutableStateFlow<List<NotificationsItem>>(emptyList())

    val notifications: StateFlow<List<NotificationsItem>> = _notifications

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRecomendations(request: RecommendationRequest) {

        viewModelScope.launch {

            _isLoading.value = true

            delay(2000)

            try {

                val response = withContext(Dispatchers.IO) {

                    RetrofitClient.api.getRecommendations(request)
                }

                if (response.success) {

                    _recomendationsData.value = response.data
                    addNotification(response.data.alert)
                } else {

                    // fallback local
                    _recomendationsData.value = getLocalRecommendations(request)
                    addNotification(response.data.alert)
                }

            } catch (e: Exception) {

                e.printStackTrace()

                if (isServiceUnavailable(e)) {

                    // fallback local
                    val localData = getLocalRecommendations(request)

                    _recomendationsData.value = localData

                    addNotification(localData.alert)
                }
            } finally {

                _isLoading.value = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addNotification(
        alert: String?
    ) {

        if (alert.isNullOrEmpty()) return

        val exists = _notifications.value.any {
            it.message == alert
        }

        if (!exists) {

            val newNotification = NotificationsItem(
                message = alert,
                date = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm"
                    )
                )
            )

            _notifications.value =
                _notifications.value + newNotification
        }
    }

    private fun isServiceUnavailable(
        error: Exception
    ): Boolean {

        val msg = error.message?.lowercase() ?: ""

        return msg.contains("503") ||
                msg.contains("429") ||
                msg.contains("quota") ||
                msg.contains("rate") ||
                msg.contains("timeout") ||
                msg.contains("unavailable")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLocalRecommendations(
        request: RecommendationRequest
    ): RecommendationData {

        return when (request.targetType.lowercase()) {

            "community" -> {

                RecommendationData(

                    targetType = "community",

                    name = "Riohacha",

                    date = LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern(
                            "yyyy-MM-dd'T'HH:mm:ss"
                        )
                    ),

                    radiationToday = 6.5,

                    solarIndex = 95,

                    reasoning =
                        "Riohacha tiene el mayor potencial solar de Colombia pero enfrenta tarifas altas y apagones.",

                    recommendations = listOf(

                        Recommendation(
                            title = "Campaña masiva de consumo en horas solares",
                            description =
                                "Promover que hogares y negocios concentren lavado, planchado y cocción entre 9:00-14:00 vía redes y emisoras.",
                            priority = "alta",
                            timeWindow = "09:00 - 14:00",
                            savingsCopDay = 850000
                        ),

                        Recommendation(
                            title = "Incentivos para paneles solares",
                            description =
                                "Gestionar subsidios departamentales para PYMES. Con 6.5+ kWh/m², el retorno es menor a 3 años.",
                            priority = "media",
                            timeWindow = "Gestión continua",
                            savingsCopDay = 320000
                        ),

                        Recommendation(
                            title = "Mantenimiento eléctrico comunitario",
                            description =
                                "Jornadas de revisión de instalaciones. El 40% de pérdidas en barrios son por conexiones deficientes.",
                            priority = "baja",
                            timeWindow = "Fines de semana",
                            savingsCopDay = 180000
                        )
                    ),

                    totalSavingsCopDay = 1350000,

                    totalSavingsCopMonth = 40500000,

                    alert = "Con la radiación actual, Riohacha podría cubrir el 60% de su demanda con energía solar."
                )
            }

            else -> {

                RecommendationData(

                    targetType = "company",

                    name = "Restaurante Sazón Guajira",

                    date = LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern(
                            "yyyy-MM-dd'T'HH:mm:ss"
                        )
                    ),

                    radiationToday = 6.2,

                    solarIndex = 88,

                    reasoning =
                        "La cocina industrial y refrigeración dominan el consumo. Se priorizó desplazar cocción fuera del pico tarifario.",

                    recommendations = listOf(

                        Recommendation(
                            title = "Cocinar antes del pico tarifario",
                            description =
                                "Adelanta preparación de bases y fondos a 9:00-12:00. Evita cocina industrial entre 18:00-21:00.",
                            priority = "alta",
                            timeWindow = "09:00 - 12:00",
                            savingsCopDay = 28000
                        ),

                        Recommendation(
                            title = "Optimizar refrigeración nocturna",
                            description =
                                "Baja la temperatura de neveras 2°C antes del cierre para mantener frío sin ciclos activos.",
                            priority = "media",
                            timeWindow = "21:00 - 22:00",
                            savingsCopDay = 12000
                        ),

                        Recommendation(
                            title = "A/C solo en horario de servicio",
                            description =
                                "Enciende el A/C 30 min antes de abrir y apágalo al cerrar. No dejar encendido de noche.",
                            priority = "baja",
                            timeWindow = "10:30 - 22:00",
                            savingsCopDay = 8500
                        )
                    ),

                    totalSavingsCopDay = 48500,

                    totalSavingsCopMonth = 1455000,

                    alert =
                        "La temperatura hoy supera 34°C. El A/C consumirá más — prioriza ventilación cruzada."
                )
            }
        }
    }

}