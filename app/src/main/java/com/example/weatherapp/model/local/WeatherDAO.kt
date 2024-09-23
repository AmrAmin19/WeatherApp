package com.example.weatherapp.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.FavWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM fav_weather")
     fun getAllLocal(): Flow<List<FavWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favWeather: FavWeather):Long

    @Delete
    suspend fun delete(favWeather: FavWeather)
}