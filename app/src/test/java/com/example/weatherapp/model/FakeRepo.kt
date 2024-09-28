package com.example.weatherapp.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepo(
    private val currentWeatherResponse : CurrentWeatherResponse
    , private val forecastWeatherResponse : WeatherResponse
    , private val shouldEmitError: Boolean = false
    , private val shouldEmitLoading: Boolean = false
    , private val shouldEmitFavWeather: Boolean = false
    , private val errorMessage: String = "Error message"
    ,private val favWeatherListInsert : MutableList<FavWeather> = mutableListOf()
):Irepo {

    private val fakePreferences = mutableMapOf(
        "unit" to "metric",  // Example key-value pair for unit
        "language" to "en"    // Example key-value pair for language
    )

    private val favWeatherOne = FavWeather(name = "New York", lat = 40.7128, lon = -74.0060)
    private val favWeatherTwo = FavWeather(name = "London", lat = 51.5074, lon = -0.1278)



    override fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): Flow<ApiState<CurrentWeatherResponse>> = flow {
        if (shouldEmitLoading) {
            emit(ApiState.Loading) // Emit loading state
        }
        if (shouldEmitError) {
            emit(ApiState.Error(errorMessage)) // Emit error state
        } else {
            emit(ApiState.Success(currentWeatherResponse)) // Emit success with current weather response
        }
    }

    override fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): Flow<ApiState<WeatherResponse>> = flow {
        if (shouldEmitLoading) {
            emit(ApiState.Loading) // Emit loading state
        }
        if (shouldEmitError) {
            emit(ApiState.Error(errorMessage)) // Emit error state
        } else {
            emit(ApiState.Success(forecastWeatherResponse)) // Emit success with current weather response
        }
    }

    override fun getDailyForecasts(weatherResponse: WeatherResponse): List<DailyForecast> {
        TODO("Not yet implemented")
    }

    override fun getHourlyForecastForToday(weatherResponse: WeatherResponse): List<HourlyForecast> {
        TODO("Not yet implemented")
    }

    override fun getLocationByName(name: String): Flow<LocationResponce> {
        TODO("Not yet implemented")
    }

    override fun getAllLocal(): Flow<List<FavWeather>> = flow {

      if (shouldEmitFavWeather){
          emit(listOf(favWeatherOne,favWeatherTwo))
      }
        else{
            emit(emptyList())
        }
    }

    override suspend fun insert(favWeather: FavWeather): Long {
       favWeatherListInsert.add(favWeather)
        return 1L
    }

    override suspend fun delete(favWeather: FavWeather) {
        favWeatherListInsert.remove(favWeather)
    }

    override fun getAllLocalAlarm(): Flow<List<AlarmData>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlarmData(alarmData: AlarmData) {
        TODO("Not yet implemented")
    }

    override suspend fun deletAlarm(alarmData: AlarmData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOldAlarms(currentTimeMillis: Long) {
        TODO("Not yet implemented")
    }

    override fun getFavWeather(weatherResponse: WeatherResponse): FavWeather {
        TODO("Not yet implemented")
    }

    override fun AddsettingsPrefs(key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun getSettingsPrefs(key: String, default: String): String {
        return fakePreferences[key] ?: default
    }

    override fun AddLocationPrefs(key: String, value: Double) {
        TODO("Not yet implemented")
    }

    override fun getLocationPrefs(key: String, default: Double): Double {
        TODO("Not yet implemented")
    }

    override fun AddAlertPrefs(key: String, value: Int) {
        TODO("Not yet implemented")
    }

    override fun getAlertPrefs(key: String, default: Int): Int {
        TODO("Not yet implemented")
    }
}