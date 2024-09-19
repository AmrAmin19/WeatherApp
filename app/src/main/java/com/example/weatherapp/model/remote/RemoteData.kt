package com.example.weatherapp.model.remote

import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.WeatherResponse

class RemoteData :IremoteData {
    val services :ApiServices = RetrofitClient.service

   override suspend fun getCurrentWeather(lat: Double,lon: Double):CurrentWeatherResponse
    {
      return  services.getCurrentWeather(lat = lat,
            lon = lon)
    }

    override suspend fun getForecastWeather(lat: Double,lon: Double):WeatherResponse
    {
        return  services.getWeatherForecast(lat = lat,
            lon = lon)
    }
}