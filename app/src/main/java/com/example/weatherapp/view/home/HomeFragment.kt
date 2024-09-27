package com.example.weatherapp.view.home



import com.example.weatherapp.model.local.LocalData
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.model.ApiState
import com.example.weatherapp.model.CurrentWeather
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.NetworkUtils
import com.example.weatherapp.model.Repo
import com.example.weatherapp.model.SharedPreferencesKeys
import com.example.weatherapp.model.local.SharedPreferences
import com.example.weatherapp.model.remote.RemoteData
import com.example.weatherapp.view.Communicator
import com.example.weatherapp.viewModel.HomeFactory
import com.example.weatherapp.viewModel.HomeViewModel
import com.example.weatherapp.viewModel.MainActivityViewModel
import com.example.weatherapp.viewModel.MainFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class HomeFragment : Fragment() {



    val communicator by lazy {
        requireActivity() as Communicator
    }

    lateinit var binding:FragmentHomeBinding
    lateinit var homeViewModel:HomeViewModel
    lateinit var factory: HomeFactory
    lateinit var mainFactory: MainFactory
    lateinit var mainViewModel:MainActivityViewModel
    lateinit var myAdapter: DailyForcastAdapter
    lateinit var hourAdabter: HourlyForcastAdabter

    var  settings = emptyMap<String,String>()

    private var latOld: Double = 0.0
    private var lonOld: Double = 0.0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        factory=HomeFactory(Repo.getInstance(RemoteData(), LocalData(requireContext()),
            SharedPreferences(requireContext())
        ))
        homeViewModel=ViewModelProvider(this,factory).get(HomeViewModel::class.java)

        mainFactory= MainFactory(Repo.getInstance(RemoteData(), LocalData(requireContext()),SharedPreferences(requireContext())))
        mainViewModel= ViewModelProvider(requireActivity(),mainFactory).get(MainActivityViewModel::class.java)

        binding=FragmentHomeBinding.inflate(inflater,container,false)


        Log.d("AmrFragment", "onCreateView: ")



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         settings= homeViewModel.getSettings()


        Log.d("testAmr", "${settings[SharedPreferencesKeys.Temprature_key]} ")




        Log.d("AmrFragment", "onViewCreated: ")

        showLoading(true)


        myAdapter= DailyForcastAdapter()
        hourAdabter= HourlyForcastAdabter()

        binding.forecastRecyclerView.layoutManager= LinearLayoutManager(context)
        binding.forecastRecyclerView.adapter=myAdapter

        binding.recycleHourView.layoutManager=LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        binding.recycleHourView.adapter=hourAdabter


       mainViewModel.locationLiveData.observe(viewLifecycleOwner, Observer {
               fetchWeatherData(it.latitude,it.longitude)

           binding.swipeRefreshLayout.isRefreshing = false

       })


        lifecycleScope.launch {
            homeViewModel.currentWeatherState.collect{ resource ->
                when (resource) {
                    is ApiState.Loading -> {
                        showLoading(true)
                    }
                    is ApiState.Success<CurrentWeatherResponse> -> {
                        showLoading(false)

                        createUi(resource.data)
                    }
                    is ApiState.Error -> {

                        showLoading(false)
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        homeViewModel.forecastWeather.observe(viewLifecycleOwner, Observer {

            showLoading(false)


            binding.locationText.text=it.city.name

        })

        homeViewModel.dailyForecast.observe(viewLifecycleOwner, Observer {

            showLoading(false)

           val dailyForecast = it.map {
                    daily ->
                daily.copy(
                    maxTemp = convertTemperature(daily.maxTemp.toDouble(), settings[SharedPreferencesKeys.Temprature_key]?:"C"),
                    minTemp = convertTemperature(daily.minTemp.toDouble(), settings[SharedPreferencesKeys.Temprature_key]?:"C")
                )
            }

            myAdapter.submitList(dailyForecast)

        })

        homeViewModel.hourlyForecast.observe(viewLifecycleOwner, Observer {

            Log.d("AmrDataTes", "${it.size}  ")

           val hourlyForecast = it.map {
                hourly ->
                hourly.copy(temp=convertTemperature(hourly.temp.toDouble(), settings[SharedPreferencesKeys.Temprature_key]?:"C"))
            }

            hourAdabter.submitList(hourlyForecast)
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
             when(mainViewModel.getSettingsPrefs()){
                 "gps"->communicator.getFreshLocation()
                 "map"->communicator.getMapLocation()
             }
        }

    }


    fun createUi(currentWeather:CurrentWeatherResponse)
    {

        binding.updatedTime.text=getString(R.string.timeupdate,convertTimestampToTime(currentWeather.dt))
        binding.dateText.text=convertTimestampToDate(currentWeather.dt)

        Log.d("AmrDataTest", "${currentWeather.weather[0].icon} ")

        Glide.with(requireContext())
            .load("https://openweathermap.org/img/wn/${currentWeather.weather[0].icon}@2x.png")
            .into(binding.weatherIcon)


        binding.weatherCondition.text=currentWeather.weather[0].description

        // binding.temperatureText.text=it.main.temp.toInt().toString()
        binding.temperatureText.text=convertTemperature(currentWeather.main.temp,settings[SharedPreferencesKeys.Temprature_key]?:"C")

        binding.humadityVal.text=currentWeather.main.humidity.toString()
        binding.windVal.text=convertWindSpeed(currentWeather.wind.speed,settings[SharedPreferencesKeys.Speed_key]?:"mps")
        binding.fellLikeVal.text=convertTemperature(currentWeather.main.feels_like,settings[SharedPreferencesKeys.Temprature_key]?:"C")


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

        if (!NetworkUtils.isInternetAvailable(requireContext())) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
            return  // Exit the flow if there's no connection
        }

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

    fun convertTemperature(tempInCelsius: Double, unit: String): String {
        return when (unit) {
            "K" -> getString(R.string.temprature,"${(tempInCelsius + 273.15).toInt()} ","K")  // Celsius to Kelvin
            "F" ->  getString(R.string.temprature,"${(tempInCelsius * 9/5 + 32).toInt()} ","F")   // Celsius to Fahrenheit
            else -> getString(R.string.temprature,"${tempInCelsius.toInt()} ","C")  // Default is Celsius
        }
    }

    fun convertWindSpeed(speed: Double, unit: String): String {
        return when (unit) {
            "mph" ->getString(R.string.windSpeed,(speed * 2.23694).toInt().toString(),"Mph") // Convert mps to mph
            "mps" -> getString(R.string.windSpeed,speed .toInt().toString(),"M/s")
            else -> getString(R.string.windSpeed,speed .toInt().toString(),"M/s")
        }

    }
}