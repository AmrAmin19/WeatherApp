package com.example.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.AlarmData
import com.example.weatherapp.model.FavWeather
import com.example.weatherapp.model.Irepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertViewModel(val repo :Irepo) :ViewModel() {


    private val _alarms= MutableStateFlow<List<AlarmData>>(emptyList())
    val alarms : StateFlow<List<AlarmData>>
        get() = _alarms


    init {
        val currentTimeInMillis = System.currentTimeMillis()
        deleteOldAlarms(currentTimeInMillis)
        getAlarms()
    }

    fun getSettingsPrefs(key:String,default:String):String{
        return repo.getSettingsPrefs(key,default)
    }

    fun addAlertPrefs(key: String, value: Int) {
        repo.AddAlertPrefs(key,value)
    }
    fun getAlertPrefs(key: String, default: Int): Int {
        return repo.getAlertPrefs(key, default)
    }



    fun getAlarms()
    {
        viewModelScope.launch {
           repo.getAllLocalAlarm().collect{

               _alarms.value=it

           }
        }
    }

    fun insertAlarm(alarmData: AlarmData)
    {
        viewModelScope.launch {
           repo.insertAlarmData(alarmData)
        }
    }
    fun deleteAlarm(alarmData: AlarmData)
    {
        viewModelScope.launch {
            repo.deletAlarm(alarmData)
        }
    }

    fun deleteOldAlarms(currentTimeMillis: Long)
    {
        viewModelScope.launch {
            repo.deleteOldAlarms(currentTimeMillis)
        }
    }


}