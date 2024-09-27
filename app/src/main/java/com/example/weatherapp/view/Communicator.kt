package com.example.weatherapp.view

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

interface Communicator {
    fun getFreshLocation()
    fun getMapLocation()
}