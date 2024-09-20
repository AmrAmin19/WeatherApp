package com.example.weatherapp.model.remote

import android.util.Log
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.WeatherResponse

class RemoteData :IremoteData {
    val services :ApiServices = RetrofitClient.service

   override suspend fun getCurrentWeather(lat: Double,lon: Double):CurrentWeatherResponse
    {
        Log.d("AmrTestCalls", "getCurrentWeather: ")
      return  services.getCurrentWeather(lat = lat,
            lon = lon)
    }

    override suspend fun getForecastWeather(lat: Double,lon: Double):WeatherResponse
    {
        Log.d("AmrTestCalls", "getForecastWeather: ")
        return  services.getWeatherForecast(lat = lat,
            lon = lon)
    }
}