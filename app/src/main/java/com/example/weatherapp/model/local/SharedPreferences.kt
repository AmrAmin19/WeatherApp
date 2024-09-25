package com.example.weatherapp.model.local

import android.content.Context

class SharedPreferences(context: Context) :IsharedPrefs{

    private val preferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

   override fun AddsettingsPrefs(key:String,value:String)
    {
        preferences.edit().putString(key, value).apply()
    }

   override fun getSettingsPrefs(key:String,default:String):String
    {
        return preferences.getString(key, default).toString()
    }
//
//    fun saveLocation(language: String) {
//        preferences.edit().putString("location", language).apply()
//    }
//
//    fun getLocation(): String {
//        return preferences.getString("location", "gps").toString()
//    }
//
//    fun saveLanguage(language: String) {
//        preferences.edit().putString("language", language).apply()
//    }
//
//    fun getLanguage(): String {
//        return preferences.getString("language", "en").toString()
//    }
//
//    fun saveSpeed(language: String) {
//        preferences.edit().putString("wind_speed_unit", language).apply()
//    }
//
//    fun getSpeed(): String {
//        return preferences.getString("wind_speed_unit", "mps").toString()
//    }
//    fun saveUnit(language: String) {
//        preferences.edit().putString("unit", language).apply()
//    }
//
//    fun getUnit(): String {
//        return preferences.getString("unit", "C").toString()
//    }
//
//    fun saveNotification(language: String) {
//        preferences.edit().putString("notification", language).apply()
//    }
//
//    fun getNotification(): String {
//        return preferences.getString("notification", "enable").toString()
//    }
}