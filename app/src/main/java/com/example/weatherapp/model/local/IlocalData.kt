package com.example.weatherapp.model.local

import com.example.weatherapp.model.CurrentWeatherResponse

interface IlocalData {
    suspend fun getAllLocal(): List<CurrentWeatherResponse>
    suspend fun insert(currentWeather: CurrentWeatherResponse)
    suspend fun delete(currentWeather: CurrentWeatherResponse)
}