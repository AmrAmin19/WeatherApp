package com.example.weatherapp.model.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.model.AlarmData
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.FavWeather

@Database(entities = [FavWeather::class,AlarmData::class,CurrentWeather::class], version = 1)
@TypeConverters(HourlyForecastConverter::class, DailyForecastConverter::class)
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