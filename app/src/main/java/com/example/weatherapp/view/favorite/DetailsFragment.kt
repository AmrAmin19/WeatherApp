package com.example.weatherapp.view.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentDetailsBinding
import com.example.weatherapp.model.FavWeather
import com.example.weatherapp.model.Repo
import com.example.weatherapp.model.local.LocalData
import com.example.weatherapp.model.remote.RemoteData
import com.example.weatherapp.view.home.DailyForcastAdapter
import com.example.weatherapp.view.home.HourlyForcastAdabter
import com.example.weatherapp.viewModel.DetailsFactory
import com.example.weatherapp.viewModel.DetailsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DetailsFragment : Fragment() {
    lateinit var binding: FragmentDetailsBinding
    lateinit var factory:DetailsFactory
    lateinit var viewModel: DetailsViewModel

    lateinit var dayAdapter: DailyForcastAdapter
    lateinit var hourAdabter: HourlyForcastAdabter
//    lateinit var   favWeather :FavWeather

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentDetailsBinding.inflate(inflater,container,false)
        factory=DetailsFactory(Repo.getInstance(RemoteData(), LocalData(requireContext())))
        viewModel=ViewModelProvider(this,factory).get(DetailsViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val args: DetailsFragmentArgs by navArgs()  // Retrieve the arguments
       val favWeather = args.favWeather

        viewModel.fetchCurrentWeather(favWeather.lat,favWeather.lon)
        viewModel.fetchForecastWeather(favWeather.lat,favWeather.lon)

        binding.locationText.text=favWeather.name


        dayAdapter= DailyForcastAdapter()
        hourAdabter= HourlyForcastAdabter()

        binding.forecastRecyclerView.layoutManager= LinearLayoutManager(context)
        binding.forecastRecyclerView.adapter=dayAdapter

        binding.recycleHourView.layoutManager= LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
        binding.recycleHourView.adapter=hourAdabter


        viewModel.currentWeather.observe(viewLifecycleOwner, Observer {

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

        viewModel.dailyForecast.observe(viewLifecycleOwner, Observer {

            showLoading(false)

            dayAdapter.submitList(it)

        })

        viewModel.hourlyForecast.observe(viewLifecycleOwner, Observer {

            Log.d("AmrDataTes", "${it.size}  ")

            hourAdabter.submitList(it)
        })

    }


    fun convertTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp * 1000) // Convert seconds to milliseconds
        val sdf = SimpleDateFormat("MMMM dd", Locale.getDefault()) // Format to Month name and day number
        sdf.timeZone= TimeZone.getDefault()
        return sdf.format(date)
    }

    fun convertTimestampToTime(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault()) // Format to hours and minutes only
//        val tz = TimeZone.getTimeZone("GMT${if (timeZone >= 0) "+" else ""}$timeZone")
//        sdf.timeZone = tz
        sdf.timeZone= TimeZone.getDefault()
        return sdf.format(date)
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


}