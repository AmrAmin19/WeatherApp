package com.example.weatherapp.view.favorite

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weatherapp.databinding.FavItemBinding
import com.example.weatherapp.model.FavWeather

class FavDiff:DiffUtil.ItemCallback<FavWeather>(){
    override fun areItemsTheSame(oldItem: FavWeather, newItem: FavWeather): Boolean {
       return oldItem.name==newItem.name
    }

    override fun areContentsTheSame(oldItem: FavWeather, newItem: FavWeather): Boolean {
      return oldItem==newItem
    }
}

class FavAdapter (
    var myListenner:(FavWeather)->Unit
) :ListAdapter<FavWeather,FavAdapter.FavViewHolder>(FavDiff())
{
    lateinit var binding:FavItemBinding
    class FavViewHolder(var binding: FavItemBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= FavItemBinding.inflate(inflater,parent,false)
        return FavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
       var currentWeather = getItem(position)
        holder.binding.cityName.text=currentWeather.name

        holder.binding.favItemCard.setOnClickListener {

            Log.d("AmrAdapter", "card clicked ")
        }

        holder.binding.deletIcon.setOnClickListener {
            myListenner.invoke(currentWeather)
        }

    }
}