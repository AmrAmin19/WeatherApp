package com.example.weatherapp.model.local

import com.example.weatherapp.model.CurrentWeather


interface IlocalData {
    suspend fun getAllLocal(): List<CurrentWeather>
    suspend fun insert(currentWeather: CurrentWeather)
    suspend fun delete(currentWeather: CurrentWeather)
}