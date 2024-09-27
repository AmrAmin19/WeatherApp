package com.example.weatherapp.model.local

interface IsharedPrefs {
    fun AddsettingsPrefs(key:String,value:String)
    fun getSettingsPrefs(key:String,default:String):String
    fun AddLocationPrefs(key: String,value: Double)
    fun getLocationPrefs(key: String,default: Double):Double
    fun AddAlertPrefs(key:String,value:Int)
    fun getAlertPrefs(key:String,default:Int):Int
}