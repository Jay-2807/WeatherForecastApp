package com.example.weatherforecastapp.data.viewmodel

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.data.model.WeatherApiDbModel
import com.example.weatherforecastapp.data.repo.WeatherApiRepo
import kotlinx.coroutines.launch

class WeatherApiViewModel(
        private val repository: WeatherApiRepo
    ) : ViewModel() {

    var weatherList by mutableStateOf<List<WeatherApiDbModel>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                var result  = repository.getWeather(city)
                if (result.isEmpty()) {
                    errorMessage =
                        "No offline data available for \"$city\".\nConnect to the internet and try again."

                } else {
                    weatherList = result
                }
            } catch (e: Exception) {
                errorMessage = "Something went wrong"
                println("Weather api fetched wrong")
            }

            isLoading = false
        }
    }
    }








