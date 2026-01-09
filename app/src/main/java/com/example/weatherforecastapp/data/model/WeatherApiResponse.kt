package com.example.weatherforecastapp.data.model

data class WeatherApiResponse(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)