package com.example.weatherapp.model.local

class FakeSharedPrefs(
    val settingsCheck:String,
    val locationCheck:Double,
    val alertCheck:Int
) :IsharedPrefs {
    override fun AddsettingsPrefs(key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun getSettingsPrefs(key: String, default: String): String {
       return  settingsCheck
    }

    override fun AddLocationPrefs(key: String, value: Double) {
        TODO("Not yet implemented")
    }

    override fun getLocationPrefs(key: String, default: Double): Double {
        return locationCheck
    }

    override fun AddAlertPrefs(key: String, value: Int) {
        TODO("Not yet implemented")
    }

    override fun getAlertPrefs(key: String, default: Int): Int {
       return alertCheck
    }
}