package com.example.weatherapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeBinding
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



    lateinit var binding:FragmentHomeBinding
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

        binding=FragmentHomeBinding.inflate(inflater,container,false)

      

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       mainViewModel.locationLiveData.observe(viewLifecycleOwner, Observer {
           lat=it.latitude
           lon=it.longitude

           if (!isDataFetched) {
               fetchWeatherData()
               isDataFetched = true
           }
       })


        homeViewModel.currentWeather.observe(viewLifecycleOwner, Observer {


           binding.updatedTime.text= convertTimestampToDate(it.dt)

            Log.d("AmrDataTest", "${it.weather[0].icon} ")

            Glide.with(this.requireContext())
                .load("https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png")
                .into(binding.weatherIcon)


            binding.weatherCondition.text=it.weather[0].main

            binding.temperatureText.text=it.main.temp.toString()

            binding.humadityVal.text=it.main.humidity.toString()
            binding.windVal.text=it.wind.speed.toString()
            binding.fellLikeVal.text=it.main.feels_like.toString()

//            Toast.makeText(context, "${convertTimestampToDate(it.dt)}  ${it.main.temp}  ${it.wind.speed}", Toast.LENGTH_SHORT).show()
//
//            Log.d("AmrDataTest", "${convertTimestampToDate(it.dt)}  ${it.main.temp}  ${it.wind.speed} ")

        })

        homeViewModel.forecastWeather.observe(viewLifecycleOwner, Observer {

            binding.locationText.text=it.city.name



            Log.d("AmrDataTest", " ${it.city.name}  ${it.list[0].dt_txt}  ${it.list[1].dt_txt}  ${it.list[2].dt_txt} " +
                    "${it.list[3].dt_txt} ${it.list[4].dt_txt} ${it.list[5].dt_txt} \n" +
                    "${it.list[6].dt_txt} ${it.list[7].dt_txt} ${it.list[8].dt_txt} \n" +
                    "${it.list[9].dt_txt} ${it.list[10].dt_txt} ${it.list[11].dt_txt} \n " +
                    "${it.list[12].dt_txt} ${it.list[13].dt_txt} ${it.list[14].dt_txt} \n " +
                    "${it.list[15].dt_txt} ${it.list[16].dt_txt} ${it.list[17].dt_txt} \n" +
                    "${it.list[18].dt_txt} ${it.list[19].dt_txt} ${it.list[20].dt_txt} \n" +
                    "${it.list[21].dt_txt} ${it.list[22].dt_txt} ${it.list[23].dt_txt} \n" +
                    "${it.list[24].dt_txt} ${it.list[25].dt_txt} ${it.list[26].dt_txt} \n")
//
//            Toast.makeText(context, "${it.city.name}  ${it.list.get(0).main.temp}  ${it.list[1].main.temp}", Toast.LENGTH_SHORT).show()
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

