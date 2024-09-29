package com.example.weatherapp.model.local

import com.example.weatherapp.model.AlarmData
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.FavWeather
import kotlinx.coroutines.flow.Flow


interface IlocalData {
    fun getAllLocal(): Flow<List<FavWeather>>
    suspend fun insert(favWeather: FavWeather):Long
    suspend fun delete(favWeather: FavWeather)

    fun getAllLocalAlarm() : Flow<List<AlarmData>>
    suspend fun insertAlarmData(alarmData: AlarmData)
    suspend fun deletAlarm(alarmData: AlarmData)


    fun getCurrentWeather() : Flow<List<CurrentWeather>>
    suspend fun insertCurrentWeather(currentWeather: CurrentWeather)
    suspend fun deleteCurrentWeather(currentWeather: CurrentWeather)
    suspend fun deleteAllCurrentWeather()

    suspend fun deleteOldAlarms(currentTimeMillis: Long)
}