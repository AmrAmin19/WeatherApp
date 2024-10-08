package com.example.weatherapp.model.remote

import com.example.weatherapp.model.ApiState
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.DailyForecast
import com.example.weatherapp.model.HourlyForecast
import com.example.weatherapp.model.LocationResponce
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IremoteData {

   fun getCurrentWeather(lat: Double, lon: Double, lang: String): Flow<ApiState<CurrentWeatherResponse>>
//    suspend fun getForecastWeather(lat: Double,lon: Double,lang:String): WeatherResponse
fun getForecastWeather(lat: Double, lon: Double, lang: String): Flow<ApiState<WeatherResponse>>

    fun getDailyForecasts(weatherResponse: WeatherResponse): List<DailyForecast>
    fun getHourlyForecastForToday(weatherResponse: WeatherResponse): List<HourlyForecast>
    suspend fun getLocationByName (name:String):List<LocationResponce>

}