package com.example.weatherapp.model.remote

import android.util.Log
import com.example.weatherapp.model.ApiState
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.DailyForecast
import com.example.weatherapp.model.HourlyForecast
import com.example.weatherapp.model.LocationResponce
import com.example.weatherapp.model.WeatherInfo
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RemoteData :IremoteData {
    val services :ApiServices = RetrofitClient.service



    override fun getCurrentWeather(lat: Double, lon: Double, lang: String): Flow<ApiState<CurrentWeatherResponse>> = flow {
        emit(ApiState.Loading)  // Emit a loading state

        try {

            val currentWeather = services.getCurrentWeather(lat, lon, lang)


            emit(ApiState.Success(currentWeather))
        } catch (e: Exception) {

            emit(ApiState.Error("Error fetching weather data: ${e.message ?: "Unknown Error"}"))
            Log.e("API Error", "Error fetching weather data: ${e.message}")
        }
    }


    override fun getForecastWeather(lat: Double, lon: Double, lang: String): Flow<ApiState<WeatherResponse>> = flow {
        emit(ApiState.Loading)  // Emit a loading state

        try {

            val forecastWeather = services.getWeatherForecast(lat, lon, lang)


            emit(ApiState.Success(forecastWeather))
        } catch (e: Exception) {

            emit(ApiState.Error("Error fetching forecast data: ${e.message ?: "Unknown Error"}"))
            Log.e("API Error", "Error fetching forecast data: ${e.message}")
        }
    }


    override suspend fun getLocationByName (name:String) : List<LocationResponce>
    {
        return services.getLocationByName(name)
    }


   override fun getDailyForecasts(weatherResponse: WeatherResponse): List<DailyForecast> {
        val dailyForecasts = mutableMapOf<String, MutableList<WeatherInfo>>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // To extract the date
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault()) // To extract the day name (Monday, etc.)

        // Group by day
        for (weatherInfo in weatherResponse.list) {
            val date = Date(weatherInfo.dt * 1000) // Convert dt to Date
            val formattedDate = dateFormat.format(date) // Format as yyyy-MM-dd
            dailyForecasts.computeIfAbsent(formattedDate) { mutableListOf() }.add(weatherInfo)
        }

        // Aggregate data for each day
        val result = mutableListOf<DailyForecast>()
        for ((formattedDate, weatherInfoList) in dailyForecasts) {
            val date = dateFormat.parse(formattedDate)!! // Parse the formatted date back into a Date object
            val dayName = dayFormat.format(date) // Get the day name (e.g., Monday)

            val minTemp = weatherInfoList.minOf { it.main.temp_min }.toString()
            val maxTemp = weatherInfoList.maxOf { it.main.temp_max }.toString()
            val avgHumidity = weatherInfoList.map { it.main.humidity }.average()

            // Choose a representative weather description and icon (e.g., the first entry)
            val representativeWeather = weatherInfoList[0].weather[0]

            result.add(
                DailyForecast(
                    date = formattedDate,
                    dayName = dayName,           // Add the day name
                    minTemp = minTemp,
                    maxTemp = maxTemp,
                    avgHumidity = avgHumidity,
                    weatherDescription = representativeWeather.description,
                    icon = representativeWeather.icon
                )
            )
        }

        return result
    }



    override fun getHourlyForecastForToday(weatherResponse: WeatherResponse): List<HourlyForecast> {
        val hourlyForecasts = mutableListOf<HourlyForecast>()
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())  // Date formatter
        val sdfTime = SimpleDateFormat("HH:mm", Locale.getDefault())  // Time formatter

        // Get the date of the first forecast in the response
        if (weatherResponse.list.isNotEmpty()) {
            val firstForecastDate = sdfDate.format(Date(weatherResponse.list[0].dt * 1000))  // First forecast date

            // Loop through the list and find the entries for the first forecast date
            for (weatherInfo in weatherResponse.list) {
                val forecastDate = sdfDate.format(Date(weatherInfo.dt * 1000))  // Extract the date from dt

                // If the forecast is for the first forecast date
                if (forecastDate == firstForecastDate) {
                    val forecastTime = sdfTime.format(Date(weatherInfo.dt * 1000))  // Extract time
                    val representativeWeather = weatherInfo.weather[0]

                    // Add the forecast for that hour to the list
                    hourlyForecasts.add(
                        HourlyForecast(
                            time = forecastTime,
                            temp = (weatherInfo.main.temp).toString(),
                            weatherDescription = representativeWeather.description,
                            icon = representativeWeather.icon
                        )
                    )
                }
            }
        }

        return hourlyForecasts
    }



}