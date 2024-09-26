package com.example.weatherapp.model.remote

import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.LocationResponce
import com.example.weatherapp.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("data/2.5/forecast")
  suspend fun getWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String = "en",
        @Query("appid") apiKey: String = "151442ed846d30298934d94031e0b9f0",
        @Query("units") units: String = "metric"
    ): WeatherResponse

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String = "en",
        @Query("appid") apiKey: String = "151442ed846d30298934d94031e0b9f0",
        @Query("units") units: String = "metric"
    ):CurrentWeatherResponse

    @GET("geo/1.0/direct")
    suspend fun getLocationByName(
        @Query("q") cityName: String,
        @Query("appid") appId: String = "151442ed846d30298934d94031e0b9f0"
    ): List<LocationResponce>
}