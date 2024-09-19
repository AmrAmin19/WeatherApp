package com.example.weatherapp.model.remote

import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.WeatherResponse

interface IremoteData {
    suspend fun getCurrentWeather(lat: Double,lon: Double): CurrentWeatherResponse
    suspend fun getForecastWeather(lat: Double,lon: Double): WeatherResponse

}