package com.example.weatherapp.view.settings

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.example.weatherapp.model.MapArgs
import com.example.weatherapp.model.Repo
import com.example.weatherapp.model.SharedPreferencesKeys
import com.example.weatherapp.model.local.LocalData
import com.example.weatherapp.model.local.SharedPreferences
import com.example.weatherapp.model.remote.RemoteData
import com.example.weatherapp.viewModel.MainActivityViewModel
import com.example.weatherapp.viewModel.SettingsFactory
import com.example.weatherapp.viewModel.SettingsViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    lateinit var factory: SettingsFactory
    lateinit var viewModel: SettingsViewModel
    lateinit var mainViewModel:MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        factory=SettingsFactory(
            Repo.getInstance(RemoteData(), LocalData(requireContext()),SharedPreferences(requireContext())))

        viewModel= ViewModelProvider(this,factory).get(SettingsViewModel::class.java)
        
        mainViewModel=ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

        binding= FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getSettings()

        lifecycleScope.launch{
            viewModel.sharedData.collect { data ->
                updateRadioGroups(data)
                mainViewModel.updateSettingsData(data)
            }
        }

        binding.groupLocation.setOnCheckedChangeListener{ group , checkedId ->
            when(checkedId){
                binding.map.id ->{
                    viewModel.AddsettingsPrefs(SharedPreferencesKeys.Location_key,"map")
                    val action = SettingsFragmentDirections.actionSettingsFragmentToMapFragment(MapArgs.SettingsFragment_Source)
                    findNavController().navigate(action)
                }
                else -> viewModel.AddsettingsPrefs(SharedPreferencesKeys.Location_key,"gps")
            }

        }

        binding.groupLanguage.setOnCheckedChangeListener{ _ , checkedId ->
            when(checkedId){
               binding.arabic.id -> viewModel.AddsettingsPrefs(SharedPreferencesKeys.Language_key,"ar")
                else -> viewModel.AddsettingsPrefs(SharedPreferencesKeys.Language_key,"en")
            }
        }

        binding.groupTemperature.setOnCheckedChangeListener{ _ , checkedId ->
            when(checkedId){
               binding.kelvin.id -> viewModel.AddsettingsPrefs(SharedPreferencesKeys.Temprature_key,"K")
               binding.fahrenheit.id -> viewModel.AddsettingsPrefs(SharedPreferencesKeys.Temprature_key,"F")
                else -> viewModel.AddsettingsPrefs(SharedPreferencesKeys.Temprature_key,"C")
            }
        }

        binding.groupWindSpeed.setOnCheckedChangeListener{ _ , checkedId ->
            when(checkedId){
               binding.mileHour.id ->viewModel.AddsettingsPrefs(SharedPreferencesKeys.Speed_key,"mph")
                else -> viewModel.AddsettingsPrefs(SharedPreferencesKeys.Speed_key,"mps")
            }
        }

        binding.groupNotifications.setOnCheckedChangeListener{ _ , checkedId ->
            when(checkedId){
                binding.disable.id-> viewModel.AddsettingsPrefs(SharedPreferencesKeys.Notification_key,"disable") //viewModel.saveNotification("disable")
                else -> viewModel.AddsettingsPrefs(SharedPreferencesKeys.Notification_key,"enable")
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

    fun setLocale(languageCode: String, context: Context) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        val intent = (context as Activity).intent
        context.finish()
        context.startActivity(intent)
    }


}