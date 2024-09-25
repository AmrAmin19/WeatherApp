package com.example.weatherapp.view.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.example.weatherapp.model.Repo
import com.example.weatherapp.model.local.LocalData
import com.example.weatherapp.model.local.SharedPreferences
import com.example.weatherapp.model.remote.RemoteData
import com.example.weatherapp.viewModel.SettingsFactory
import com.example.weatherapp.viewModel.SettingsViewModel
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    lateinit var factory: SettingsFactory
    lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        factory=SettingsFactory(
            Repo.getInstance(RemoteData(), LocalData(requireContext()),SharedPreferences(requireContext())))

        viewModel= ViewModelProvider(this,factory).get(SettingsViewModel::class.java)

        binding= FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getSettings()

        lifecycleScope.launch{
            viewModel.sharedData.collect { data ->
                updateRadioGroups(data)
            }
        }

        binding.groupLocation.setOnCheckedChangeListener{ group , checkedId ->
            when(checkedId){
                binding.map.id -> viewModel.AddsettingsPrefs("Location","map")
                else -> viewModel.AddsettingsPrefs("Location","gps")
            }

        }

        binding.groupLanguage.setOnCheckedChangeListener{ _ , checkedId ->
            when(checkedId){
               binding.arabic.id -> viewModel.AddsettingsPrefs("Language","ar")
                else -> viewModel.AddsettingsPrefs("Language","en")
            }
        }

        binding.groupTemperature.setOnCheckedChangeListener{ _ , checkedId ->
            when(checkedId){
               binding.kelvin.id -> viewModel.AddsettingsPrefs("Temperature","K")
               binding.fahrenheit.id -> viewModel.AddsettingsPrefs("Temperature","F")
                else -> viewModel.AddsettingsPrefs("Temperature","C")
            }
        }

        binding.groupWindSpeed.setOnCheckedChangeListener{ _ , checkedId ->
            when(checkedId){
               binding.mileHour.id ->viewModel.AddsettingsPrefs("Speed","mph")
                else -> viewModel.AddsettingsPrefs("Speed","mps")
            }
        }

        binding.groupNotifications.setOnCheckedChangeListener{ _ , checkedId ->
            when(checkedId){
                binding.disable.id-> viewModel.AddsettingsPrefs("Notification","disable") //viewModel.saveNotification("disable")
                else -> viewModel.AddsettingsPrefs("Notification","enable")
            }
        }

    }


    private fun updateRadioGroups(settings: List<String>) {
        when (settings[0]) {
            "gps" -> {binding.groupLocation.check(binding.gps.id)
                Log.d("SharedPrefs", "gps: ")
            }
            "map" -> {binding.groupLocation.check(binding.map.id)

                Log.d("SharedPrefs", "map: ")}
        }
        when (settings[1]) {
            "en" -> {binding.groupLanguage.check(binding.english.id)
                Log.d("SharedPrefs", "en: ")
            }
            "ar" -> {binding.groupLanguage.check(binding.arabic.id)
                Log.d("SharedPrefs", "ar ")
            }
        }
        when (settings[2]) {
            "C" -> binding.groupTemperature.check(binding.celsius.id)
            "K" -> binding.groupTemperature.check(binding.kelvin.id)
            "F" -> binding.groupTemperature.check(binding.fahrenheit.id)
        }
        when (settings[3]) {
            "mps" -> binding.groupWindSpeed.check(binding.meterSec.id)
            "mph" -> binding.groupWindSpeed.check(binding.mileHour.id)
        }
        when (settings[4]) {
            "enable" -> binding.groupNotifications.check(binding.enable.id)
            "disable" -> binding.groupNotifications.check(binding.disable.id)
        }
    }


}