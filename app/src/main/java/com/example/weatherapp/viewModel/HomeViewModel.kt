package com.example.weatherapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.DailyForecast
import com.example.weatherapp.model.HourlyForecast
import com.example.weatherapp.model.Irepo
import com.example.weatherapp.model.SharedPreferencesKeys
import com.example.weatherapp.model.WeatherInfo
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(val repo :Irepo) : ViewModel() {

    private val _currentWeather = MutableLiveData<CurrentWeatherResponse>()
    val currentWeather :LiveData<CurrentWeatherResponse>
        get() = _currentWeather


    private val _forecastWeather = MutableLiveData<WeatherResponse>()
    val forecastWeather :LiveData<WeatherResponse>
        get() = _forecastWeather

    private val _dailyForecast=MutableLiveData<List<DailyForecast>>()
    val dailyForecast : LiveData<List<DailyForecast>>
        get() = _dailyForecast

    private val _hourlyForecast=MutableLiveData<List<HourlyForecast>>()
    val hourlyForecast : LiveData<List<HourlyForecast>>
        get() = _hourlyForecast



    fun getSettings():Map<String,String>{
        return mapOf(
            SharedPreferencesKeys.Location_key to getSettingsPrefs(SharedPreferencesKeys.Location_key,"gps"),
            SharedPreferencesKeys.Language_key to getSettingsPrefs(SharedPreferencesKeys.Language_key,"en"),
            SharedPreferencesKeys.Temprature_key to getSettingsPrefs(SharedPreferencesKeys.Temprature_key,"C"),
            SharedPreferencesKeys.Speed_key to getSettingsPrefs(SharedPreferencesKeys.Speed_key,"mps"),
            SharedPreferencesKeys.Notification_key to getSettingsPrefs(SharedPreferencesKeys.Notification_key,"enable")
        )
    }

    fun getSettingsPrefs(key:String,default:String):String{
        return repo.getSettingsPrefs(key,default)
    }



    fun fetchCurrentWeather(lat: Double,lon: Double)
    {
       viewModelScope.launch {

         val weather= repo.getCurrentWeather(lat,lon,getSettingsPrefs(SharedPreferencesKeys.Language_key,"en"))

//           weather.main.temp = convertTemperature(weather.main.temp, getSettingsPrefs(SharedPreferencesKeys.Temprature_key,"C"))
//
//           weather.main.feels_like=convertTemperature(weather.main.feels_like, getSettingsPrefs(SharedPreferencesKeys.Temprature_key,"C"))
//
//           weather.wind.speed = convertTemperature(weather.wind.speed, getSettingsPrefs(SharedPreferencesKeys.Speed_key,"mps"))


           _currentWeather.postValue(weather)
       }
    }

    fun fetchForecastWeather(lat: Double,lon: Double)
    {
        viewModelScope.launch {
            val fWeather=repo.getForecastWeather(lat,lon,getSettingsPrefs(SharedPreferencesKeys.Language_key,"en"))
            val dailyWeather = repo.getDailyForecasts(fWeather)
            val hourlyweather =repo.getHourlyForecastForToday(fWeather)

            Log.d("AmrDataTest", "${hourlyweather.size}")

//            val dailyWeather = repo.getDailyForecasts(fWeather).map { daily ->
//                daily.copy(maxTemp = convertTemperature(daily.temp, temperatureUnit))
//            }

           _dailyForecast.postValue(dailyWeather)
            _forecastWeather.postValue(fWeather)
            _hourlyForecast.postValue(hourlyweather)

        }
    }


//    fun convertTemperature(tempInCelsius: Double, unit: String): Double {
//        return when (unit) {
//            "K" -> tempInCelsius + 273.15  // Celsius to Kelvin
//            "F" -> (tempInCelsius * 9/5) + 32  // Celsius to Fahrenheit
//            else -> tempInCelsius  // Default is Celsius
//        }
//    }


}