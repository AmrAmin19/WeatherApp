package com.example.weatherapp.model.remote

import com.example.weatherapp.model.ApiState
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.DailyForecast
import com.example.weatherapp.model.HourlyForecast
import com.example.weatherapp.model.LocationResponce
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteData(
    var dailyList : List<DailyForecast> ,
    var hourlyList:List<HourlyForecast>,
    var locationList: List<LocationResponce>,
    var currentWeatherResponse: CurrentWeatherResponse,
    var weatherResponse: WeatherResponse,
) :IremoteData {
    override fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): Flow<ApiState<CurrentWeatherResponse>> = flow {
        emit(ApiState.Success(currentWeatherResponse))
    }

    override fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String
    ):Flow<ApiState<WeatherResponse>> = flow {
        emit(ApiState.Success(weatherResponse))
    }

    override fun getDailyForecasts(weatherResponse: WeatherResponse): List<DailyForecast> {
        return dailyList
    }

    override fun getHourlyForecastForToday(weatherResponse: WeatherResponse): List<HourlyForecast> {
        return hourlyList
    }

    override suspend fun getLocationByName(name: String): List<LocationResponce> {
        if (name == "error") {
            throw Exception("Error message")
        } else if (name == "") {
            return locationList
        } else {
            return locationList
        }
    }
}