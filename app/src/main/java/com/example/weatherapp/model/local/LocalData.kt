package com.example.weatherapp.model.local

import android.content.Context
import com.example.weatherapp.model.CurrentWeatherResponse

class LocalData(context : Context) :IlocalData {

    val dao:WeatherDAO by lazy {
        val datbase:AppDatabase = AppDatabase.getInstance(context)
        datbase.getWeatherDao()
    }

    override suspend fun getAllLocal(): List<CurrentWeatherResponse> {
       return dao.getAllLocal()
    }

    override suspend fun insert(currentWeather: CurrentWeatherResponse) {
        dao.insert(currentWeather)
    }

    override suspend fun delete(currentWeather: CurrentWeatherResponse) {
        dao.delete(currentWeather)
    }


}