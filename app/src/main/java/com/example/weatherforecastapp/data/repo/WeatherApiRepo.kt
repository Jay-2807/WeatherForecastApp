package com.example.weatherforecastapp.data.repo

import android.content.Context
import com.example.weatherforecastapp.R
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
                    apiKey = context.getString(R.string.weather_api_key),
                    city = city,
                    days = 3
                )

                val data = response.forecast.forecastday.map {
                    WeatherApiDbModel(
                        city = city.lowercase(),
                        date = it.date,
                        temp = it.day.avgtemp_c,
                        condition = it.day.condition.text
                    )
                }

                dao.insertAll(data)
                println("Exception $data")
                data


            } catch (e: Exception) {
                println("Exception $e")
                dao.getWeatherByCity(city)
            }

        } else {

            dao.getWeatherByCity(city)
        }
    }
}
