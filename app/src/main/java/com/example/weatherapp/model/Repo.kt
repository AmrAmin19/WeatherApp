package com.example.weatherapp.model

import com.example.weatherapp.model.local.IlocalData
import com.example.weatherapp.model.remote.IremoteData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Repo private constructor(
    private var remoteData : IremoteData,
    private var localData : IlocalData
):Irepo
{
    companion object
    {
        private var instance : Repo? = null

        fun getInstance( remoteData : IremoteData,localData: IlocalData):Repo
        {
            return instance ?: synchronized(this)
            {
                val temp = Repo(remoteData,localData)
                instance=temp
                temp
            }
        }
    }

    // Remote

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

   override fun getHourlyForecastForToday(weatherResponse: WeatherResponse): List<HourlyForecast>
    {
        return remoteData.getHourlyForecastForToday(weatherResponse)
    }



    // Local

//  override  fun getCurrentWeatherLocal(weatherResponse: WeatherResponse,currentWeatherResponse: CurrentWeatherResponse):CurrentWeather
//    {
//        val currentWeather = CurrentWeather(
//            id = currentWeatherResponse.id,
//            lon = currentWeatherResponse.coord.lon,
//            lat = currentWeatherResponse.coord.lat,
//            weatherCondition = currentWeatherResponse.weather[0].main,
//            dt = currentWeatherResponse.dt,
//            city = weatherResponse.city.name,
//            icon = currentWeatherResponse.weather[0].icon,
//            temp = currentWeatherResponse.main.temp,
//            speed = currentWeatherResponse.wind.speed,
//            humidity = currentWeatherResponse.main.humidity,
//            feels_like = currentWeatherResponse.main.feels_like,
//            hourlyForecast = remoteData.getHourlyForecastForToday(weatherResponse),
//            dailyForecast = remoteData.getDailyForecasts(weatherResponse),
//            clouds = currentWeatherResponse.clouds.all,
//            pressure = currentWeatherResponse.main.pressure
//        )
//        return currentWeather
//    }

   override fun getFavWeather(weatherResponse: WeatherResponse):FavWeather
    {
        val favWeather=FavWeather(
            name = weatherResponse.city.name,
            lat = weatherResponse.city.coord.lat,
            lon = weatherResponse.city.coord.lon
        )
        return favWeather
    }

    override suspend fun getAllLocal(): List<FavWeather> {
      return  localData.getAllLocal()
    }

    override suspend fun insert(favWeather: FavWeather) {
       localData.insert(favWeather)
    }

    override suspend fun delete(favWeather: FavWeather) {
       localData.delete(favWeather)
    }

}