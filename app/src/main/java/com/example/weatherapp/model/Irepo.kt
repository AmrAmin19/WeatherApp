package com.example.weatherapp.model

import kotlinx.coroutines.flow.Flow

interface Irepo {
    // Remote
    suspend fun getCurrentWeather(lat: Double,lon: Double) : CurrentWeatherResponse
    suspend fun getForecastWeather(lat: Double,lon: Double) : WeatherResponse
    fun getDailyForecasts(weatherResponse: WeatherResponse): List<DailyForecast>
    fun getHourlyForecastForToday(weatherResponse: WeatherResponse): List<HourlyForecast>

    fun getLocationByName(name: String): Flow<LocationResponce>

    // Local

    fun getAllLocal(): Flow<List<FavWeather>>
    suspend fun insert(favWeather: FavWeather):Long
    suspend fun delete(favWeather: FavWeather)


    // Alarm
    fun getAllLocalAlarm() : Flow<List<AlarmData>>
    suspend fun insertAlarmData(alarmData: AlarmData)
    suspend fun deletAlarm(alarmData: AlarmData)

    suspend fun deleteOldAlarms(currentTimeMillis: Long)


//    fun getCurrentWeatherLocal(weatherResponse: WeatherResponse,currentWeatherResponse: CurrentWeatherResponse):CurrentWeather
fun getFavWeather(weatherResponse: WeatherResponse):FavWeather
}