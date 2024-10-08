package com.example.weatherapp.model

import android.util.Log
import com.example.weatherapp.model.local.IlocalData
import com.example.weatherapp.model.local.IsharedPrefs
import com.example.weatherapp.model.local.SharedPreferences
import com.example.weatherapp.model.remote.IremoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Repo private constructor(
    private var remoteData : IremoteData,
    private var localData : IlocalData,
    private var sharedPreferences: IsharedPrefs
):Irepo
{
    companion object
    {
        private var instance : Repo? = null

        fun getInstance( remoteData : IremoteData,localData: IlocalData,sharedPreferences: IsharedPrefs):Repo
        {
            return instance ?: synchronized(this)
            {
                val temp = Repo(remoteData,localData,sharedPreferences)
                instance=temp
                temp
            }
        }
    }

    // Remote

    override fun getCurrentWeather(lat: Double, lon: Double, lang: String): Flow<ApiState<CurrentWeatherResponse>> {
        return remoteData.getCurrentWeather(lat,lon,lang)
    }
    override fun getForecastWeather(lat: Double, lon: Double, lang: String): Flow<ApiState<WeatherResponse>>
    {
        return remoteData.getForecastWeather(lat,lon,lang)
    }

    override fun getDailyForecasts(weatherResponse: WeatherResponse): List<DailyForecast>
    {
        return  remoteData.getDailyForecasts(weatherResponse)
    }

   override fun getHourlyForecastForToday(weatherResponse: WeatherResponse): List<HourlyForecast>
    {
        return remoteData.getHourlyForecastForToday(weatherResponse)
    }

   override fun getLocationByName(name: String): Flow<LocationResponce> = flow {
        try {
            // Call the suspend function to get the list
            val locationList = remoteData.getLocationByName(name) // This should refer to your suspend function

            // Emit the list of locations
            emit(locationList.first())
        } catch (e: Exception) {
            // Handle error
          //  Log.e("API Error", "Error fetching location data: ${e.message}")
            // Optionally, emit an empty list or handle the error appropriately
            emit(LocationResponce(" ${e.message}",0.0,0.0))
        }
    }



    // Local

  override  fun getCurrentWeatherLocal(weatherResponse: WeatherResponse,currentWeatherResponse: CurrentWeatherResponse):CurrentWeather
    {
        val currentWeather = CurrentWeather(
            id = currentWeatherResponse.id,
            lon = currentWeatherResponse.coord.lon,
            lat = currentWeatherResponse.coord.lat,
            weatherCondition = currentWeatherResponse.weather[0].main,
            dt = currentWeatherResponse.dt,
            city = weatherResponse.city.name,
            icon = currentWeatherResponse.weather[0].icon,
            temp = currentWeatherResponse.main.temp,
            speed = currentWeatherResponse.wind.speed,
            humidity = currentWeatherResponse.main.humidity,
            feels_like = currentWeatherResponse.main.feels_like,
            hourlyForecast = remoteData.getHourlyForecastForToday(weatherResponse),
            dailyForecast = remoteData.getDailyForecasts(weatherResponse),
            clouds = currentWeatherResponse.clouds.all,
            pressure = currentWeatherResponse.main.pressure
        )
        return currentWeather
    }

   override fun getFavWeather(weatherResponse: WeatherResponse):FavWeather
    {
        val favWeather=FavWeather(
            name = weatherResponse.city.name,
            lat = weatherResponse.city.coord.lat,
            lon = weatherResponse.city.coord.lon
        )
        return favWeather
    }

    override  fun getAllLocal(): Flow<List<FavWeather>> {
      return  localData.getAllLocal()
    }

    override suspend fun insert(favWeather: FavWeather) :Long{
     return  localData.insert(favWeather)
    }

    override suspend fun delete(favWeather: FavWeather) {
       localData.delete(favWeather)
    }




    override fun getAllLocalAlarm(): Flow<List<AlarmData>> {
        return localData.getAllLocalAlarm()
    }

    override suspend fun insertAlarmData(alarmData: AlarmData) {
        localData.insertAlarmData(alarmData)
    }

    override suspend fun deletAlarm(alarmData: AlarmData) {
         localData.deletAlarm(alarmData)
    }

    override suspend fun deleteOldAlarms(currentTimeMillis: Long) {
      localData.deleteOldAlarms(currentTimeMillis)
    }



    override fun getLocalCurrentWeather(): Flow<List<CurrentWeather>> {
       return localData.getCurrentWeather()
    }

    override suspend fun insertCurrentWeather(currentWeather: CurrentWeather) {
        localData.insertCurrentWeather(currentWeather)
    }

    override suspend fun deleteCurrentWeather(currentWeather: CurrentWeather) {
        localData.deleteCurrentWeather(currentWeather)
    }

    override suspend fun deleteAllCurrentWeather() {
        localData.deleteAllCurrentWeather()
    }


    //Shared Preferences

    override fun AddsettingsPrefs(key:String,value:String)
    {
        sharedPreferences.AddsettingsPrefs(key,value)
    }
    override fun getSettingsPrefs(key:String,default:String):String
    {
        return sharedPreferences.getSettingsPrefs(key, default)
    }

    override fun AddLocationPrefs(key: String,value: Double)
    {
        sharedPreferences.AddLocationPrefs(key, value)
    }
    override fun getLocationPrefs(key: String, default: Double): Double
    {
        return sharedPreferences.getLocationPrefs(key, default)
    }

     override  fun AddAlertPrefs(key: String, value: Int) {
        sharedPreferences.AddAlertPrefs(key, value)
    }
   override fun getAlertPrefs(key: String, default: Int): Int {
        return sharedPreferences.getAlertPrefs(key, default)
    }


}