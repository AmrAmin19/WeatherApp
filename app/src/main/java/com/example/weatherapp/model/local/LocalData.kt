package com.example.weatherapp.model.local

import android.content.Context
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.CurrentWeatherResponse

class LocalData(context : Context) :IlocalData {

    val dao:WeatherDAO by lazy {
        val datbase:AppDatabase = AppDatabase.getInstance(context)
        datbase.getWeatherDao()
    }

    override suspend fun getAllLocal(): List<CurrentWeather> {
       return dao.getAllLocal()
    }

    override suspend fun insert(currentWeather: CurrentWeather) {
        dao.insert(currentWeather)
    }

    override suspend fun delete(currentWeather: CurrentWeather) {
        dao.delete(currentWeather)
    }


}