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

    override fun AddLocationPrefs(key: String,value: Double){
        preferences.edit().putFloat(key, value.toFloat()).apply()
    }

    override fun getLocationPrefs(key: String, default: Double): Double {
       return preferences.getFloat(key, default.toFloat()).toDouble()
    }

   override fun AddAlertPrefs(key:String,value:Int)
    {
        preferences.edit().putInt(key, value).apply()
    }
   override fun getAlertPrefs(key:String,default:Int):Int
    {
        return preferences.getInt(key, default)
    }
}