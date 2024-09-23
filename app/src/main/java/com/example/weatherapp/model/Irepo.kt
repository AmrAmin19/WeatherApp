package com.example.weatherapp.model

import kotlinx.coroutines.flow.Flow

interface Irepo {
    // Remote
    suspend fun getCurrentWeather(lat: Double,lon: Double) : CurrentWeatherResponse
    suspend fun getForecastWeather(lat: Double,lon: Double) : WeatherResponse
    fun getDailyForecasts(weatherResponse: WeatherResponse): List<DailyForecast>
    fun getHourlyForecastForToday(weatherResponse: WeatherResponse): List<HourlyForecast>

    // Local

    fun getAllLocal(): Flow<List<FavWeather>>
    suspend fun insert(favWeather: FavWeather):Long
    suspend fun delete(favWeather: FavWeather)

//    fun getCurrentWeatherLocal(weatherResponse: WeatherResponse,currentWeatherResponse: CurrentWeatherResponse):CurrentWeather
fun getFavWeather(weatherResponse: WeatherResponse):FavWeather
}