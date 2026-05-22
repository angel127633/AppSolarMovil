package com.example.appsolar.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsolar.Model.ForecastResponse
import com.example.appsolar.Model.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForecastViewModel : ViewModel() {

    private val _forecast =
        MutableStateFlow<ForecastResponse?>(null)

    val forecast: StateFlow<ForecastResponse?> =
        _forecast

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error

    init {
        getForecast()
    }

    fun getForecast(days: Int = 16) {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                val response = RetrofitClient.api.getForecast(days)

                if (response.isSuccessful) {

                    _forecast.value = response.body()

                } else {

                    _error.value =
                        "Error ${response.code()}"

                }

            } catch (e: Exception) {

                _error.value =
                    e.message ?: "Error desconocido"

            } finally {

                _isLoading.value = false

            }

        }

    }

}