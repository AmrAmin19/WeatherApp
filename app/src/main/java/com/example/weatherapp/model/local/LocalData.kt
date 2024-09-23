package com.example.weatherapp.model.local

import android.content.Context
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.FavWeather
import kotlinx.coroutines.flow.Flow

class LocalData(context : Context) :IlocalData {

    val dao:WeatherDAO by lazy {
        val datbase:AppDatabase = AppDatabase.getInstance(context)
        datbase.getWeatherDao()
    }

    override  fun getAllLocal(): Flow<List<FavWeather>> {
       return dao.getAllLocal()
    }

    override suspend fun insert(favWeather: FavWeather):Long {
      return  dao.insert(favWeather)
    }

    override suspend fun delete(favWeather: FavWeather) {
        dao.delete(favWeather)
    }


}