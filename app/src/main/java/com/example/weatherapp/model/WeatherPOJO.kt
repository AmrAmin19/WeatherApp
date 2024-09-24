package com.example.weatherapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherapp.model.local.CloudsConverter
import com.example.weatherapp.model.local.CoordConverter
import com.example.weatherapp.model.local.DailyForecastConverter
import com.example.weatherapp.model.local.HourlyForecastConverter
import com.example.weatherapp.model.local.MainConverter
import com.example.weatherapp.model.local.Rain1hConverter
import com.example.weatherapp.model.local.SysConverter
import com.example.weatherapp.model.local.WeatherConverter
import com.example.weatherapp.model.local.WindConverter
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class WeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherInfo>,
    val city: City
)


data class CurrentWeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val rain: Rain1h?,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

data class Rain1h(
    @SerializedName("1h") val volume: Double
)

data class WeatherInfo(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val rain: Rain?,
    val sys: Sys,
    val dt_txt: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val sea_level: Int,
    val grnd_level: Int,
    val humidity: Int,
    val temp_kf: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Clouds(
    val all: Int
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)

data class Rain(
    @SerializedName("3h") val volume: Double
)

data class Sys(
    val pod: String
)


data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

data class Coord(
    val lat: Double,
    val lon: Double
)


data class DailyForecast(
    val date: String,
    val dayName: String,
    val minTemp: Double,
    val maxTemp: Double,
    val avgHumidity: Double,
    val weatherDescription: String,
    val icon: String
)


data class HourlyForecast(
    val time: String,  // Time of the forecast (e.g., 15:00)
    val temp: Double,
    val weatherDescription: String,
    val icon: String
)

//@Entity(tableName = "current_weather_table")
data class CurrentWeather(
    val id: Int,
     val lat: Double,
    val lon: Double,
    val weatherCondition:String,
    val city:String,
    val dt: Long,
    val icon : String,
    val temp: Double,
    val feels_like: Double,
    val humidity: Int,
    val speed: Double,
    val clouds: Int,
    val pressure:Int,
//    @TypeConverters(HourlyForecastConverter::class) val hourlyForecast: List<HourlyForecast>,
//    @TypeConverters(DailyForecastConverter::class) val dailyForecast: List<DailyForecast>
)

@Entity(tableName = "fav_weather")
@Parcelize
data class FavWeather(
   @PrimaryKey val name: String,
    val lat: Double,
    val lon: Double
) : Parcelable

@Entity(tableName = "alarm_table")
data class AlarmData(
   @PrimaryKey val requestCode :Int,
    val time:Long
)

data class LocationResponce (
    val name:String,
    val lat:Double,
    val lon:Double
)

