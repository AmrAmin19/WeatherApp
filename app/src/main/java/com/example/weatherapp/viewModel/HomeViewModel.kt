package com.example.weatherapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.ApiState
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.DailyForecast
import com.example.weatherapp.model.HourlyForecast
import com.example.weatherapp.model.Irepo
import com.example.weatherapp.model.SharedPreferencesKeys
import com.example.weatherapp.model.WeatherInfo
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(val repo :Irepo) : ViewModel() {


    private val _currentWeatherState = MutableStateFlow<ApiState<CurrentWeatherResponse>>(ApiState.Loading)
    val currentWeatherState: StateFlow<ApiState<CurrentWeatherResponse>> = _currentWeatherState


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
          repo.getCurrentWeather(lat,lon,getSettingsPrefs(SharedPreferencesKeys.Language_key,"en")).collect{
             _currentWeatherState.value=it
          }
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
            _forecastWeather.postValue(fWeather)
            _hourlyForecast.postValue(hourlyweather)

        }
    }




}