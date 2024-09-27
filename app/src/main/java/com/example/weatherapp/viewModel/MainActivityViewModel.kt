package com.example.weatherapp.viewModel


import android.location.Location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.Irepo
import com.example.weatherapp.model.LOCATION_KEYS
import com.example.weatherapp.model.Repo
import com.example.weatherapp.model.SharedPreferencesKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.osmdroid.util.GeoPoint


class MainActivityViewModel (val repo: Irepo) : ViewModel() {



    private val _locationLiveData = MutableLiveData<Location>()
    val locationLiveData: LiveData<Location>
        get() = _locationLiveData

    private val _favlocationLiveData = MutableLiveData<Pair<Double, Double>>()
    val favlocationLiveData: LiveData<Pair<Double, Double>>
        get() = _favlocationLiveData

    private val _locationSettingsSharedData = MutableStateFlow<String>(repo.getSettingsPrefs(SharedPreferencesKeys.Location_key,"gps"))
    val locationSettingsSharedData : StateFlow<String> = _locationSettingsSharedData


    private val _settingsSharedData = MutableStateFlow<String>(repo.getSettingsPrefs(SharedPreferencesKeys.Language_key,"en"))
    val settingsSharedData : StateFlow<String> = _settingsSharedData

//    private val _languageSharedData = MutableStateFlow<String>("en")
//    val languageSharedData : StateFlow<String> = _languageSharedData



    fun updateLocation(location: Location) {
        _locationLiveData.value = location
    }

//    fun updateLanguage(lang:String)
//    {
//        _languageSharedData.value=lang
//    }


    fun updateFavLocation(lat:Double,lon:Double)
    {
        _favlocationLiveData.value=Pair(lat,lon)
    }

    fun getSettingsPrefs():String
    {
      return repo.getSettingsPrefs(SharedPreferencesKeys.Location_key,"gps")
    }

    fun getLocationPrefs():Pair<Double,Double>
    {
        return Pair(repo.getLocationPrefs(LOCATION_KEYS.LATITUDE,0.0),
            repo.getLocationPrefs(LOCATION_KEYS.LONGITUDE,0.0))
    }

    fun updateLanguage()
    {
        _settingsSharedData.value=repo.getSettingsPrefs(SharedPreferencesKeys.Language_key,"en")
    }

    fun updateLocationShared()
    {
        _locationSettingsSharedData.value=repo.getSettingsPrefs(SharedPreferencesKeys.Location_key,"gps")
    }


}