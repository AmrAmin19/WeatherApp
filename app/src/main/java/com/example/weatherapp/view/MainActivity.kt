package com.example.weatherapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewModel.MainActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
   private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        // Initialize Location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)




        setSupportActionBar(binding.customToolbar)

        val actionBar = supportActionBar

        actionBar?.apply {

            setHomeAsUpIndicator(R.drawable.baseline_menu_24_white)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val navController = Navigation.findNavController(this , R.id.navHostFragment)
        NavigationUI.setupWithNavController(binding.navgation,navController)




        navController.addOnDestinationChangedListener { _, destination, _ ->

            actionBar?.title = destination.label
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            startLocationUpdates()
        }

    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }



    private fun startLocationUpdates() {
        // Build the location request
        val locationRequest = LocationRequest.Builder(5000).apply {
            setMinUpdateIntervalMillis(2000)
            setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        }.build()

        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        // Check if permissions are granted
        if (permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            // Permissions granted, start location updates
            fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.lastLocation
                    if (location != null) {
                        Log.d("Amr", "onLocationResult: ${location.latitude}, ${location.longitude}")
                        viewModel.updateLocation(location)  // Pass location to ViewModel
                        fusedLocationClient.removeLocationUpdates(this)

                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }, Looper.myLooper())  // Use main looper
        } else {
            // Only request permissions if they haven't been requested before
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Request permissions if they are not already granted
                ActivityCompat.requestPermissions(this, permissions, 1)
            } else {
                // Handle the case where the user denied the permissions before
                Log.d("Amr", "Location permissions denied")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            // Check if all permissions are granted
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Start location updates when permissions are granted
                startLocationUpdates()
            } else {
                // Handle permission denial without requesting permissions again
                Log.d("Amr", "Location permissions denied")
            }
        }
    }





    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (binding.main.isDrawerOpen(GravityCompat.START)) {
                binding.main.closeDrawer(GravityCompat.START)
            } else {
                binding.main.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}