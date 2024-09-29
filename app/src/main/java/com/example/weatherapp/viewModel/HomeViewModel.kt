package com.example.weatherapp.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.ApiState
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.DailyForecast
import com.example.weatherapp.model.HourlyForecast
import com.example.weatherapp.model.Irepo
import com.example.weatherapp.model.SharedPreferencesKeys
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeViewModel(val repo :Irepo) : ViewModel() {


    private val _currentWeatherState = MutableStateFlow<ApiState<CurrentWeatherResponse>>(ApiState.Loading)
    val currentWeatherState: StateFlow<ApiState<CurrentWeatherResponse>> = _currentWeatherState


    private val _forecastWeather = MutableStateFlow<ApiState<WeatherResponse>>(ApiState.Loading)
    val forecastWeather : StateFlow<ApiState<WeatherResponse>> = _forecastWeather


    private val _dailyForecast=MutableStateFlow<List<DailyForecast>>(emptyList())
    val dailyForecast : StateFlow<List<DailyForecast>> = _dailyForecast

    private val _hourlyForecast=MutableStateFlow<List<HourlyForecast>>(emptyList())
    val hourlyForecast : StateFlow<List<HourlyForecast>> = _hourlyForecast

    private val _localCurrentWeather = MutableStateFlow<List<CurrentWeather>>(emptyList())
    val localCurrentWeather: StateFlow<List<CurrentWeather>> = _localCurrentWeather

    

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


    fun changetToDaily(weatherResponse: WeatherResponse)
    {
        _dailyForecast.value= repo.getDailyForecasts(weatherResponse)
    }

    fun changetToHourly(weatherResponse: WeatherResponse){
        _hourlyForecast.value =repo.getHourlyForecastForToday(weatherResponse)
    }

    fun fetchForecastWeather(lat: Double,lon: Double)
    {
        viewModelScope.launch {

            repo.getForecastWeather(lat,lon,getSettingsPrefs(SharedPreferencesKeys.Language_key,"en")).collect{


                _forecastWeather.value=it
            }

        }
    }

    fun changeToCurrent(weatherResponse: WeatherResponse,currentWeatherResponse: CurrentWeatherResponse){
       viewModelScope.launch {
           repo.deleteAllCurrentWeather()
           repo.insertCurrentWeather(repo.getCurrentWeatherLocal(weatherResponse,currentWeatherResponse))

       }
    }


    fun getlocalWeatherData(){
        viewModelScope.launch {
            repo.getLocalCurrentWeather().collect{
                _localCurrentWeather.value=it
            }
        }
    }




}