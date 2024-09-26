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
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.launch

class DetailsViewModel(val repo:Irepo) :ViewModel(){

    private val _currentWeather = MutableLiveData<CurrentWeatherResponse>()
    val currentWeather : LiveData<CurrentWeatherResponse>
        get() = _currentWeather


//    private val _forecastWeather = MutableLiveData<WeatherResponse>()
//    val forecastWeather : LiveData<WeatherResponse>
//        get() = _forecastWeather

    private val _dailyForecast= MutableLiveData<List<DailyForecast>>()
    val dailyForecast : LiveData<List<DailyForecast>>
        get() = _dailyForecast

    private val _hourlyForecast= MutableLiveData<List<HourlyForecast>>()
    val hourlyForecast : LiveData<List<HourlyForecast>>
        get() = _hourlyForecast



    fun getSettingsPrefs(key:String,default:String):String{
        return repo.getSettingsPrefs(key,default)
    }

    fun fetchCurrentWeather(lat: Double,lon: Double)
    {
        viewModelScope.launch {
            val weather= repo.getCurrentWeather(lat,lon,getSettingsPrefs(SharedPreferencesKeys.Language_key,"en"))
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

            _dailyForecast.postValue(dailyWeather)
          //  _forecastWeather.postValue(fWeather)
            _hourlyForecast.postValue(hourlyweather)

        }
    }



}