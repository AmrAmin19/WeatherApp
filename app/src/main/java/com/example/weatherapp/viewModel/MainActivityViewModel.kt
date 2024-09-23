package com.example.weatherapp.viewModel


import android.location.Location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint


class MainActivityViewModel : ViewModel() {

    private val _locationLiveData = MutableLiveData<Location>()
    val locationLiveData: LiveData<Location>
        get() = _locationLiveData

    private val _favlocationLiveData = MutableLiveData<Pair<Double, Double>>()
    val favlocationLiveData: LiveData<Pair<Double, Double>>
        get() = _favlocationLiveData

    fun updateLocation(location: Location) {
        _locationLiveData.value = location
    }

    fun updateFavLocation(lat:Double,lon:Double)
    {
        _favlocationLiveData.value=Pair(lat,lon)
    }
}