package com.example.weatherapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.Irepo
import com.example.weatherapp.model.LocationResponce
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(val repo:Irepo):ViewModel() {
    private val _sharedData = MutableStateFlow<List<String>>(emptyList())
    val sharedData : StateFlow<List<String>> = _sharedData



    fun getSettings(){
        _sharedData.value = listOf(
           getSettingsPrefs("Location","gps"),
          getSettingsPrefs("Language","en"),
           getSettingsPrefs("Temperature","C"),
           getSettingsPrefs("Speed","mps"),
           getSettingsPrefs("Notification","enable")
        )
    }

    fun getSettingsPrefs(key:String,default:String):String{
     return repo.getSettingsPrefs(key,default)
    }

    fun AddsettingsPrefs(key:String,value:String){
        repo.AddsettingsPrefs(key,value)
    }
}