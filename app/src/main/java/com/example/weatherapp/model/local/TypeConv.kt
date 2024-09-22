package com.example.weatherapp.model.local

import androidx.room.TypeConverter
import com.example.weatherapp.model.Clouds
import com.example.weatherapp.model.Coord
import com.example.weatherapp.model.DailyForecast
import com.example.weatherapp.model.HourlyForecast
import com.example.weatherapp.model.Main
import com.example.weatherapp.model.Rain1h
import com.example.weatherapp.model.Sys
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.Wind
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CoordConverter {
    @TypeConverter
    fun fromCoord(coord: Coord): String {
        return Gson().toJson(coord)
    }

    @TypeConverter
    fun toCoord(value: String): Coord {
        return Gson().fromJson(value, Coord::class.java)
    }
}

class WeatherConverter {
    @TypeConverter
    fun fromWeatherList(list: List<Weather>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toWeatherList(value: String): List<Weather> {
        val type = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(value, type)
    }
}

class MainConverter {
    @TypeConverter
    fun fromMain(main: Main): String {
        return Gson().toJson(main)
    }

    @TypeConverter
    fun toMain(value: String): Main {
        return Gson().fromJson(value, Main::class.java)
    }
}
class WindConverter {
    @TypeConverter
    fun fromWind(wind: Wind): String {
        return Gson().toJson(wind)
    }

    @TypeConverter
    fun toWind(value: String): Wind {
        return Gson().fromJson(value, Wind::class.java)
    }
}
class Rain1hConverter {
    @TypeConverter
    fun fromRain1h(rain: Rain1h?): String {
        return Gson().toJson(rain)
    }

    @TypeConverter
    fun toRain1h(value: String): Rain1h? {
        return Gson().fromJson(value, Rain1h::class.java)
    }
}
class CloudsConverter {
    @TypeConverter
    fun fromClouds(clouds: Clouds): String {
        return Gson().toJson(clouds)
    }

    @TypeConverter
    fun toClouds(value: String): Clouds {
        return Gson().fromJson(value, Clouds::class.java)
    }
}
class SysConverter {
    @TypeConverter
    fun fromSys(sys: Sys): String {
        return Gson().toJson(sys)
    }

    @TypeConverter
    fun toSys(value: String): Sys {
        return Gson().fromJson(value, Sys::class.java)
    }
}

class HourlyForecastConverter {

    @TypeConverter
    fun fromHourlyForecastList(hourlyForecasts: List<HourlyForecast>): String {
        return Gson().toJson(hourlyForecasts)
    }

    @TypeConverter
    fun toHourlyForecastList(data: String): List<HourlyForecast> {
        val listType = object : TypeToken<List<HourlyForecast>>() {}.type
        return Gson().fromJson(data, listType)
    }
}

class DailyForecastConverter {

    @TypeConverter
    fun fromDailyForecastList(dailyForecasts: List<DailyForecast>): String {
        return Gson().toJson(dailyForecasts)
    }

    @TypeConverter
    fun toDailyForecastList(data: String): List<DailyForecast> {
        val listType = object : TypeToken<List<DailyForecast>>() {}.type
        return Gson().fromJson(data, listType)
    }
}





