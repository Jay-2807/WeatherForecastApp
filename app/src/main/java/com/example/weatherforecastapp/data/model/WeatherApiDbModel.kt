package com.example.weatherforecastapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_forecast" ,primaryKeys = ["city", "date"])
data class WeatherApiDbModel(
   /* @PrimaryKey(autoGenerate = true)
    val id: Int = 0,*/
    val city: String,
    val date: String,
    val temp: Double,
    val condition: String
)
