package com.example.weatherapp.model.local

import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.FavWeather


interface IlocalData {
    suspend fun getAllLocal(): List<FavWeather>
    suspend fun insert(favWeather: FavWeather)
    suspend fun delete(favWeather: FavWeather)
}