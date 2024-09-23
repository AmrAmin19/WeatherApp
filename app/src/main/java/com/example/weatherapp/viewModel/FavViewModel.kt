package com.example.weatherapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.FavWeather
import com.example.weatherapp.model.Irepo
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavViewModel(val repo:Irepo):ViewModel() {
    private val _favWeather=MutableLiveData<List<FavWeather>>()
    val favWeather :LiveData<List<FavWeather>>
        get() = _favWeather
//
//    private val _currentWeather=MutableLiveData<CurrentWeather>()
//    val currentWeather :LiveData<CurrentWeather>
//        get() = _currentWeather


    init {
        getFavWeather()
    }

    fun fetchDataFromApi(lat:Double,lon:Double)
    {



        viewModelScope.launch {

//            try {
//                // Fetch weather data concurrently
//                val currentWeatherResponseDeferred =  async { repo.getCurrentWeather(lat, lon) }
//                val weatherResponseDeferred = async { repo.getForecastWeather(lat, lon) }
//
//                val currentWeatherResponse = currentWeatherResponseDeferred.await()
//                val weatherResponse = weatherResponseDeferred.await()
//
//                // Process and store the data
//                val temp = repo.getCurrentWeatherLocal(weatherResponse, currentWeatherResponse)
//
//                // Insert processed data into the database
//                repo.insert(temp)
//
//                // Update LiveData
//                _currentWeather.postValue(temp)
//
//            } catch (e: Exception) {
//                // Handle any exceptions, e.g., network or database issues
//                Log.e("WeatherViewModel", "Error fetching weather data", e)
//                // Optionally, post a fallback value or notify the UI of the error
//            }

//            val currentWeatherResponse=repo.getCurrentWeather(lat,lon)
            val weatherResponse= repo.getForecastWeather(lat,lon)

            val temp = repo.getFavWeather(weatherResponse)

            //testing the idea
            repo.insert(temp)
            getFavWeather()
//
//            _currentWeather.postValue(temp)

        }

    }

    fun getFavWeather()
    {
        viewModelScope.launch {
          val temp= repo.getAllLocal()
            _favWeather.postValue(temp)
        }
    }

    fun insert(favWeather: FavWeather)
    {
        viewModelScope.launch {
            repo.insert(favWeather)
        }
    }

    fun delet(favWeather: FavWeather)
    {
        viewModelScope.launch {
            repo.delete(favWeather)
        }
        getFavWeather()
    }
}