package com.example.weatherapp.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.FavWeather

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM fav_weather")
    suspend fun getAllLocal(): List<FavWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favWeather: FavWeather)

    @Delete
    suspend fun delete(favWeather: FavWeather)
}