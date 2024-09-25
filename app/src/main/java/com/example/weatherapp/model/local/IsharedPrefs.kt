package com.example.weatherapp.model.local

interface IsharedPrefs {
    fun AddsettingsPrefs(key:String,value:String)
    fun getSettingsPrefs(key:String,default:String):String
}