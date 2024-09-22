package com.example.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.Irepo
import kotlinx.coroutines.launch

class FavViewModel(val repo:Irepo):ViewModel() {
    private val _favWeather=MutableLiveData<List<CurrentWeather>>()
    val favWeather :LiveData<List<CurrentWeather>>
        get() = _favWeather

    private val _currentWeather=MutableLiveData<CurrentWeather>()
    val currentWeather :LiveData<CurrentWeather>
        get() = _currentWeather


    fun fetchDataFromApi(lat:Double,lon:Double)
    {
        viewModelScope.launch {
            val currentWeatherResponse=repo.getCurrentWeather(lat,lon)
            val weatherResponse= repo.getForecastWeather(lat,lon)

         val temp = repo.getCurrentWeatherLocal(weatherResponse,currentWeatherResponse)
            _currentWeather.postValue(temp)

        }
    }

    fun getFavWeather()
    {
        viewModelScope.launch {
          val temp= repo.getAllLocal()
            _favWeather.postValue(temp)
        }
    }

    fun insert(currentWeather: CurrentWeather)
    {
        viewModelScope.launch {
            repo.insert(currentWeather)
        }
    }

    fun delet(currentWeather: CurrentWeather)
    {
        viewModelScope.launch {
            repo.delete(currentWeather)
        }
    }
}