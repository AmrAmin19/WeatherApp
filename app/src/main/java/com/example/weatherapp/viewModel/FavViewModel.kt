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


    init {
        getFavWeather()
    }



    fun getFavWeather()
    {
        viewModelScope.launch {
            repo.getAllLocal().collect{
                _favWeather.value=it
            }
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

    }
}