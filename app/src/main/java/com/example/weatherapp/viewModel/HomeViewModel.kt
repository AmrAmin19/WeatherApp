package com.example.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.DailyForecast
import com.example.weatherapp.model.Irepo
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



    fun fetchCurrentWeather(lat: Double,lon: Double)
    {
       viewModelScope.launch {
         val weather= repo.getCurrentWeather(lat,lon)
           _currentWeather.postValue(weather)
       }
    }

    fun fetchForecastWeather(lat: Double,lon: Double)
    {
        viewModelScope.launch {
            val fWeather=repo.getForecastWeather(lat,lon)
            val dailyWeather = repo.getDailyForecasts(fWeather)

           _dailyForecast.postValue(dailyWeather)
            _forecastWeather.postValue(fWeather)

        }
    }





}