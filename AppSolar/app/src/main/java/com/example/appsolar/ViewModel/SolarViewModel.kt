package com.example.appsolar.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsolar.Model.RetrofitClient
import com.example.appsolar.Model.ScoreData
import com.example.appsolar.Model.SolarData
import com.example.appsolar.Model.SolarScore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SolarViewModel : ViewModel() {

    private val _data = MutableStateFlow<SolarData?>(null)
    val data: StateFlow<SolarData?> = _data

    private val _dataScore = MutableStateFlow<ScoreData?>(null)
    val dataScore : StateFlow<ScoreData?> = _dataScore

    private val _success = MutableStateFlow("hola")
    val success: StateFlow<String> = _success
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000)
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.api.getSolarToday()
                }

                val responseScore = withContext(Dispatchers.IO){
                    RetrofitClient.api.getSolarScore()
                }

                println("Mira esto ${response.data}")
                println("Mira esto un success ${response.success}")
                if (response.success) {
                    _data.value = response.data
                    _dataScore.value = responseScore.data
                    _success.value = "todo ah salido bien"
                } else {
                    _success.value = "Hay problemas con la api"
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }
        }
    }

}