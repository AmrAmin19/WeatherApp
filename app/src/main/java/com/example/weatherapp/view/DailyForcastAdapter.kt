package com.example.weatherapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.DailyForecastItemBinding
import com.example.weatherapp.model.DailyForecast

class DayForcast : DiffUtil.ItemCallback<DailyForecast>() {
    override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
             return  oldItem.date==newItem.date
    }

    override fun areContentsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
             return  oldItem == newItem
    }
}

class DailyForcastAdapter :ListAdapter<DailyForecast,DailyForcastAdapter.DailyViewHolder>(DayForcast())
{
    lateinit var binding: DailyForecastItemBinding
    class DailyViewHolder(val binding: DailyForecastItemBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {

        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        binding= DailyForecastItemBinding.inflate(inflater,parent,false)

        return DailyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
      var currentDay = getItem(position)
        holder.binding.dayName.text=currentDay.dayName
        holder.binding.dailycondition.text=currentDay.weatherDescription
        holder.binding.minTemp.text = holder.itemView.context.getString(
            R.string.minMaxTemp,
            currentDay.minTemp.toInt(),
            currentDay.maxTemp.toInt()
        )





        Glide.with(holder.itemView.context)
            .load("https://openweathermap.org/img/wn/${currentDay.icon}@2x.png")
            .into(binding.weatherIcon)
    }
}