package com.example.weatherapp.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.HourlyForcastItemBinding
import com.example.weatherapp.model.HourlyForecast

class HourForcastDiff : DiffUtil.ItemCallback<HourlyForecast>() {
    override fun areItemsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
      return oldItem.time==newItem.time
    }

    override fun areContentsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
       return oldItem == newItem
    }
}

class HourlyForcastAdabter : ListAdapter<HourlyForecast, HourlyForcastAdabter.HourlyForcastViewHolder>(
    HourForcastDiff()
)
{
    lateinit var binding:HourlyForcastItemBinding
    class HourlyForcastViewHolder(var binding: HourlyForcastItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForcastViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= HourlyForcastItemBinding.inflate(inflater,parent,false)
        return HourlyForcastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyForcastViewHolder, position: Int) {
        val currentHour = getItem(position)
       holder.binding.textTime .text=currentHour.time
        holder.binding.textTemperature.text = currentHour.temp.toInt().toString()


        Glide.with(holder.itemView.context)
            .load("https://openweathermap.org/img/wn/${currentHour.icon}@2x.png")
            .into(holder.binding.imageWeatherIcon)
    }
}