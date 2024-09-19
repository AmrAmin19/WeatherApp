package com.example.weatherapp.model

interface Irepo {
    suspend fun getCurrentWeather(lat: Double,lon: Double) : CurrentWeatherResponse
    suspend fun getForecastWeather(lat: Double,lon: Double) : WeatherResponse
}