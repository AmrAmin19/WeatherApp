package com.example.weatherapp.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.CurrentWeatherResponse

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM weather")
    suspend fun getAllLocal(): List<CurrentWeatherResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeather: CurrentWeatherResponse)

    @Delete
    suspend fun delete(currentWeather: CurrentWeatherResponse)
}