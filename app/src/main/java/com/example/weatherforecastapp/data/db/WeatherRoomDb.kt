package com.example.weatherforecastapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecastapp.data.model.WeatherApiDbModel

@Database(entities = [WeatherApiDbModel::class], version = 1)
abstract class WeatherRoomDb : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {

        @Volatile
        private var INSTANCE: WeatherRoomDb? = null

        fun getDatabase(context: Context): WeatherRoomDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherRoomDb::class.java,
                    "weather_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
