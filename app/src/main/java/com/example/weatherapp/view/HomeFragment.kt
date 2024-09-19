package com.example.weatherapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.model.Irepo
import com.example.weatherapp.model.Repo
import com.example.weatherapp.model.remote.RemoteData
import com.example.weatherapp.viewModel.HomeFactory
import com.example.weatherapp.viewModel.HomeViewModel
import com.example.weatherapp.viewModel.MainActivityViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class HomeFragment : Fragment() {

    lateinit var repo:Irepo

    lateinit var homeViewModel:HomeViewModel
    lateinit var factory: HomeFactory
    lateinit var mainViewModel:MainActivityViewModel
     var lat:Double=0.0
    var lon:Double=0.0
    var isDataFetched = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        factory=HomeFactory(Repo.getInstance(RemoteData()))
        homeViewModel=ViewModelProvider(this,factory).get(HomeViewModel::class.java)

        mainViewModel=ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)


        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       mainViewModel.locationLiveData.observe(viewLifecycleOwner, Observer {
           lat=it.latitude
           lon=it.longitude

           if (!isDataFetched) {
               fetchWeatherData()
               isDataFetched = true // Set flag to prevent fetching multiple times
           }
       })


        homeViewModel.currentWeather.observe(viewLifecycleOwner, Observer {

            Toast.makeText(context, "${convertTimestampToDate(it.dt)}  ${it.main.temp}  ${it.wind.speed}", Toast.LENGTH_SHORT).show()

            Log.d("AmrDataTest", "${convertTimestampToDate(it.dt)}  ${it.main.temp}  ${it.wind.speed} ")

        })

        homeViewModel.forecastWeather.observe(viewLifecycleOwner, Observer {

            Log.d("AmrDataTest", " ${it.city.name}  ${it.list.get(0).main.temp}  ${it.list[1].main.temp}")

            Toast.makeText(context, "${it.city.name}  ${it.list.get(0).main.temp}  ${it.list[1].main.temp}", Toast.LENGTH_SHORT).show()
        })
    }


    fun convertTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp * 1000) // Convert seconds to milliseconds
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault() // Use the default time zone or set a specific one
        return sdf.format(date)
    }


    private fun fetchWeatherData() {
        if (lat != 0.0 && lon != 0.0) {
            homeViewModel.fetchCurrentWeather(lat, lon)

            homeViewModel.fetchForecastWeather(lat,lon)
        } else {
            Log.e("HomeFragment", "Invalid location data.")
        }
    }

}

