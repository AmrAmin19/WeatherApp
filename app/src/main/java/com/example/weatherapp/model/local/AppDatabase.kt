package com.example.weatherapp.model.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.CurrentWeatherResponse

@Database(entities = [CurrentWeather::class], version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun getWeatherDao() : WeatherDAO

    companion object{
        @Volatile
        private var Instance : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase{
            return Instance ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,AppDatabase::class.java,"Weather_Database"
                ).build()
                Instance = instance
                instance
            }
        }
    }
}