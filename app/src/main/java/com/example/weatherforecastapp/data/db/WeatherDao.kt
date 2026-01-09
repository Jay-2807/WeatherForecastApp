package com.example.weatherforecastapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.data.model.WeatherApiDbModel


@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<WeatherApiDbModel>)

    @Query("SELECT * FROM weather_forecast WHERE city = :city")
    suspend fun getWeatherByCity(city: String): List<WeatherApiDbModel>
}