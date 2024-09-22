package com.example.weatherapp.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.CurrentWeatherResponse

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM current_weather_table")
    suspend fun getAllLocal(): List<CurrentWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeather: CurrentWeather)

    @Delete
    suspend fun delete(currentWeather: CurrentWeather)
}