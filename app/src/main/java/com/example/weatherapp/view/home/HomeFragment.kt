package com.example.weatherapp.view.home



import com.example.weatherapp.model.local.LocalData
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeBinding
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
    lateinit var hourAdabter: HourlyForcastAdabter
  //  private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var latOld: Double = 0.0
    private var lonOld: Double = 0.0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        factory=HomeFactory(Repo.getInstance(RemoteData(), LocalData(requireContext())))
        homeViewModel=ViewModelProvider(this,factory).get(HomeViewModel::class.java)

        mainViewModel=ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

        binding=FragmentHomeBinding.inflate(inflater,container,false)

     //   fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

       // startLocationUpdates()

        Log.d("AmrFragment", "onCreateView: ")



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("AmrFragment", "onViewCreated: ")

        showLoading(true)


        myAdapter= DailyForcastAdapter()
        hourAdabter= HourlyForcastAdabter()

        binding.forecastRecyclerView.layoutManager= LinearLayoutManager(context)
        binding.forecastRecyclerView.adapter=myAdapter

        binding.recycleHourView.layoutManager=LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        binding.recycleHourView.adapter=hourAdabter


       mainViewModel.locationLiveData.observe(viewLifecycleOwner, Observer {

               //  showLoading(true)
               fetchWeatherData(it.latitude,it.longitude)

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

    private fun fetchWeatherData(lat: Double, lon: Double) {
        // Check if the latitude and longitude have changed
        if (latOld == lat && lonOld == lon) {
            // If they haven't changed, return early
            return
        }

        // Update the old latitude and longitude with new values
        latOld = lat
        lonOld = lon

        // Fetch the new weather data
        homeViewModel.fetchCurrentWeather(lat, lon)
        homeViewModel.fetchForecastWeather(lat, lon)
    }



//    private fun fetchWeatherData(lat:Double,lon:Double) {
//
//
//      if (lat != 0.0 && lon != 0.0) {
//            homeViewModel.fetchCurrentWeather(lat, lon)
//
//            homeViewModel.fetchForecastWeather(lat,lon)
//        } else {
//            Log.d("AmrData", "Invalid location data.")
//        }
//    }

    private fun showLoading(isLoading: Boolean) {
       // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

        if (isLoading)
        {

         //   binding.swipeRefreshLayout.isRefreshing = true
            binding.progressBar.visibility=View.VISIBLE
        }
        else
        {
            binding.progressBar.visibility=View.GONE
           // binding.swipeRefreshLayout.isRefreshing = false

            binding.huadityIcon.visibility=View.VISIBLE
            binding.humadityText.visibility=View.VISIBLE

            binding.windIcon.visibility=View.VISIBLE
            binding.windText.visibility=View.VISIBLE

            binding.feelsLikeIcon.visibility=View.VISIBLE
            binding.feelsLikeText.visibility=View.VISIBLE
        }
    }


}

