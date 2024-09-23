package com.example.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.Irepo
import kotlinx.coroutines.launch

class MapViewModel(var repo:Irepo):ViewModel() {

    private val _resultInsert=MutableLiveData<Long>()
     val resultInsert:LiveData<Long> = _resultInsert



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
          val result=  repo.insert(temp)
            _resultInsert.postValue(result)

//
//            _currentWeather.postValue(temp)

        }

    }



}