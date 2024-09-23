package com.example.weatherapp.model.local

import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.FavWeather
import kotlinx.coroutines.flow.Flow


interface IlocalData {
    fun getAllLocal(): Flow<List<FavWeather>>
    suspend fun insert(favWeather: FavWeather):Long
    suspend fun delete(favWeather: FavWeather)
}