package com.example.weatherapp.model.local

import com.example.weatherapp.model.AlarmData
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.FavWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalData(
    var favWeathers:List<FavWeather>,
    var alarmDatas:List<AlarmData>,
    val insertChek:Long
):IlocalData {
    override fun getAllLocal(): Flow<List<FavWeather>> = flow {
      emit(favWeathers)
    }

    override suspend fun insert(favWeather: FavWeather): Long {
       return insertChek
    }

    override suspend fun delete(favWeather: FavWeather) {
        TODO("Not yet implemented")
    }

    override fun getAllLocalAlarm(): Flow<List<AlarmData>> = flow {
        emit(alarmDatas)
    }

    override suspend fun insertAlarmData(alarmData: AlarmData) {
        TODO("Not yet implemented")
    }

    override suspend fun deletAlarm(alarmData: AlarmData) {
        TODO("Not yet implemented")
    }

    override fun getCurrentWeather(): Flow<List<CurrentWeather>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCurrentWeather(currentWeather: CurrentWeather) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCurrentWeather(currentWeather: CurrentWeather) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllCurrentWeather() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOldAlarms(currentTimeMillis: Long) {
        TODO("Not yet implemented")
    }
}