package com.example.weatherapp.model

interface Irepo {
    // Remote
    suspend fun getCurrentWeather(lat: Double,lon: Double) : CurrentWeatherResponse
    suspend fun getForecastWeather(lat: Double,lon: Double) : WeatherResponse
    fun getDailyForecasts(weatherResponse: WeatherResponse): List<DailyForecast>
    fun getHourlyForecastForToday(weatherResponse: WeatherResponse): List<HourlyForecast>

    // Local

//    suspend fun getAllLocal(): List<CurrentWeatherResponse>
//    suspend fun insert(currentWeather: CurrentWeatherResponse)
//    suspend fun delete(currentWeather: CurrentWeatherResponse)

}