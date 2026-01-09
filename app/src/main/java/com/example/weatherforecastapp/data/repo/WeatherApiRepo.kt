package com.example.weatherforecastapp.data.repo

import android.content.Context
import com.example.weatherforecastapp.data.api.WeatherApi
import com.example.weatherforecastapp.data.db.WeatherDao
import com.example.weatherforecastapp.data.model.WeatherApiDbModel
import com.example.weatherforecastapp.util.NetworkUtils

class WeatherApiRepo(
    private val api: WeatherApi,
    private val dao: WeatherDao,
    private val context: Context
) {

    suspend fun getWeather(city: String): List<WeatherApiDbModel> {

        return if (NetworkUtils.isOnline(context)) {


            try {
                val response = api.getForecast(
                    apiKey = "9755fa2427ff46cd8f2160357260801",
                    city = city,
                    days = 3
                )

                val data = response.forecast.forecastday.map {
                    WeatherApiDbModel(
                        city = city,
                        date = it.date,
                        temp = it.day.avgtemp_c,
                        condition = it.day.condition.text
                    )
                }

                dao.insertAll(data)
                data

            } catch (e: Exception) {
                dao.getWeatherByCity(city)
            }

        } else {

            dao.getWeatherByCity(city)
        }
    }
}
