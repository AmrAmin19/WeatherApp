package com.example.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.Irepo
import com.example.weatherapp.model.LocationResponce
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MapViewModel(var repo:Irepo):ViewModel() {

    private val _resultInsert=MutableLiveData<Long>()
     val resultInsert:LiveData<Long> = _resultInsert

    private val _cityLocation = MutableStateFlow<LocationResponce?>(null)
    val cityLocatio :StateFlow<LocationResponce?>
        get() = _cityLocation


    private val _filteredCities = MutableLiveData<List<String>>()
    val filteredCities: LiveData<List<String>> = _filteredCities


    private val cityList = arrayListOf(
        "Cairo", "Alexandria", "Giza", "Shubra El-Kheima", "Port Said",
        "Suez", "Luxor", "Asyut", "Mansoura", "Tanta",
        "Ismailia", "Faiyum", "Zagazig", "Damietta", "Aswan",
        "Minya", "Beni Suef", "Qena", "Sohag", "Hurghada",
        "New York", "Los Angeles", "London", "Paris", "Tokyo",
        "Berlin", "Moscow", "Sydney", "Toronto", "Rome",
        "Mumbai", "Beijing", "Dubai", "Mexico City", "Bangkok",
        "Buenos Aires", "Istanbul", "Seoul", "Sao Paulo", "Jakarta",
        "Cape Town", "Madrid", "Vienna", "Barcelona", "Athens",
        "Lisbon", "Prague", "Warsaw", "Amsterdam", "Brussels",
        "Hong Kong", "Shanghai", "Kuala Lumpur", "Singapore", "Lagos",
        "Dublin", "Copenhagen", "Stockholm", "Helsinki", "Oslo",
        "Vancouver", "Melbourne", "Zurich", "Geneva", "Edinburgh",
        "Brisbane", "Kolkata", "Karachi", "Riyadh", "Tel Aviv",
        "Casablanca", "Manila", "Lima", "Havana", "Kyiv",
        "Nairobi", "Hanoi", "Vienna", "Budapest", "Munich",
        "Venice", "Florence", "Salvador", "Rio de Janeiro", "Lyon",
        "Marseille", "Krakow", "Copenhagen", "Montreal", "Osaka",
        "Bucharest", "Belgrade", "Sofia", "Ankara", "Tbilisi"
    )



    private val searchFlow = MutableSharedFlow<String>(replay = 1)

    init {
        // Launch coroutine to listen to searchFlow
        viewModelScope.launch {
            searchFlow.collectLatest { query ->
                _filteredCities.postValue(filterCities(query))
            }
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            searchFlow.emit(query.trim().lowercase())
        }
    }

    private fun filterCities(query: String): List<String> {
        return if (query.isNotEmpty()) {
            cityList.filter { it.lowercase().startsWith(query) }
        } else {
            emptyList()
        }
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

            val weatherResponse= repo.getForecastWeather(lat,lon)

            val temp = repo.getFavWeather(weatherResponse)


          val result=  repo.insert(temp)
            _resultInsert.postValue(result)


        }

    }

    fun getLocation(name:String)
    {
        viewModelScope.launch {
            repo.getLocationByName(name).collect{
                _cityLocation.value=it
            }
        }
    }



}