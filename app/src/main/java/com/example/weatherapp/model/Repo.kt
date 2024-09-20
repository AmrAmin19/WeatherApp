package com.example.weatherapp.model

import com.example.weatherapp.model.remote.IremoteData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Repo private constructor( private var remoteData : IremoteData):Irepo
{
    companion object
    {
        private var instance : Repo? = null

        fun getInstance( remoteData : IremoteData):Repo
        {
            return instance ?: synchronized(this)
            {
                val temp = Repo(remoteData)
                instance=temp
                temp
            }
        }
    }

   override suspend fun getCurrentWeather(lat: Double,lon: Double) : CurrentWeatherResponse
    {
      return  remoteData.getCurrentWeather(lat,lon)
    }

   override suspend fun getForecastWeather(lat: Double,lon: Double) : WeatherResponse
    {
        return  remoteData.getForecastWeather(lat,lon)
    }

    override fun getDailyForecasts(weatherResponse: WeatherResponse): List<DailyForecast>
    {
        return  remoteData.getDailyForecasts(weatherResponse)
    }

}