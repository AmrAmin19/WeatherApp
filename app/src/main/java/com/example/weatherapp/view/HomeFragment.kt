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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
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
    lateinit var myAdapter: DailyForcastAdapter
    lateinit var hourAdabter:HourlyForcastAdabter

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
        showLoading(true)

        myAdapter= DailyForcastAdapter()
        hourAdabter= HourlyForcastAdabter()

        binding.forecastRecyclerView.layoutManager= LinearLayoutManager(context)
        binding.forecastRecyclerView.adapter=myAdapter

        binding.recycleHourView.layoutManager=LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        binding.recycleHourView.adapter=hourAdabter


       mainViewModel.locationLiveData.observe(viewLifecycleOwner, Observer {
           lat=it.latitude
           lon=it.longitude

           if (!isDataFetched) {
               fetchWeatherData()
               isDataFetched = true
           }
       })


        homeViewModel.currentWeather.observe(viewLifecycleOwner, Observer {

            showLoading(false)


            binding.updatedTime.text=getString(R.string.timeupdate,convertTimestampToTime(it.dt))
            binding.dateText.text=convertTimestampToDate(it.dt)

            Log.d("AmrDataTest", "${it.weather[0].icon} ")

            Glide.with(this.requireContext())
                .load("https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png")
                .into(binding.weatherIcon)


            binding.weatherCondition.text=it.weather[0].main

            binding.temperatureText.text=it.main.temp.toInt().toString()

            binding.humadityVal.text=it.main.humidity.toString()
            binding.windVal.text=getString(R.string.windSpeed,it.wind.speed.toInt().toString())
            binding.fellLikeVal.text=it.main.feels_like.toInt().toString()


        })

        homeViewModel.forecastWeather.observe(viewLifecycleOwner, Observer {

            showLoading(false)


            binding.locationText.text=it.city.name

        })

        homeViewModel.dailyForecast.observe(viewLifecycleOwner, Observer {

            showLoading(false)

            myAdapter.submitList(it)

        })

        homeViewModel.hourlyForecast.observe(viewLifecycleOwner, Observer {

            Log.d("AmrDataTes", "${it.size}  ")

            hourAdabter.submitList(it)
        })

    }


    fun convertTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp * 1000) // Convert seconds to milliseconds
        val sdf = SimpleDateFormat("MMMM dd", Locale.getDefault()) // Format to Month name and day number
        sdf.timeZone=TimeZone.getDefault()
        return sdf.format(date)
    }

    fun convertTimestampToTime(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault()) // Format to hours and minutes only
//        val tz = TimeZone.getTimeZone("GMT${if (timeZone >= 0) "+" else ""}$timeZone")
//        sdf.timeZone = tz
        sdf.timeZone=TimeZone.getDefault()
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

    private fun showLoading(isLoading: Boolean) {
       // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

        if (isLoading)
        {
            binding.progressBar.visibility=View.VISIBLE
        }
        else
        {
            binding.progressBar.visibility=View.GONE

            binding.huadityIcon.visibility=View.VISIBLE
            binding.humadityText.visibility=View.VISIBLE

            binding.windIcon.visibility=View.VISIBLE
            binding.windText.visibility=View.VISIBLE

            binding.feelsLikeIcon.visibility=View.VISIBLE
            binding.feelsLikeText.visibility=View.VISIBLE
        }
    }
}

