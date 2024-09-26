package com.example.weatherapp.model.remote

import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.DailyForecast
import com.example.weatherapp.model.HourlyForecast
import com.example.weatherapp.model.LocationResponce
import com.example.weatherapp.model.WeatherResponse

interface IremoteData {
    suspend fun getCurrentWeather(lat: Double,lon: Double,lang:String): CurrentWeatherResponse
    suspend fun getForecastWeather(lat: Double,lon: Double,lang:String): WeatherResponse

    fun getDailyForecasts(weatherResponse: WeatherResponse): List<DailyForecast>
    fun getHourlyForecastForToday(weatherResponse: WeatherResponse): List<HourlyForecast>
    suspend fun getLocationByName (name:String):List<LocationResponce>

}