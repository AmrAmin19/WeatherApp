package com.example.weatherapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewModel.MainActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : AppCompatActivity(),Communicator {

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
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



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

//        binding.swipeRefreshLayout.setOnRefreshListener {
//            getFreshLocation()
//         //   startLocationUpdates()
//        }

    }

    override fun onStart() {
        super.onStart()
       if (checkPermission())
       {

           if (isLocationEnabled())
           {
               getFreshLocation()
           }
           else
           {
               enableLocationServices()
           }

       }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION),1)
        }
    }



//    private fun startLocationUpdates() {
//        // Build the location request
//        val locationRequest = LocationRequest.Builder(5000).apply {
//            setMinUpdateIntervalMillis(2000)
//            setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//        }.build()
//
//        val permissions = arrayOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//
//        // Check if permissions are granted
//        if (permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
//            // Permissions granted, start location updates
//            fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
//                override fun onLocationResult(locationResult: LocationResult) {
//                    val location = locationResult.lastLocation
//                    if (location != null) {
//                        Log.d("Amr", "onLocationResult: ${location.latitude}, ${location.longitude}")
//                        viewModel.updateLocation(location)  // Pass location to ViewModel
//                        fusedLocationClient.removeLocationUpdates(this)
//
//                        binding.swipeRefreshLayout.isRefreshing = false
//                    }
//                }
//            }, Looper.myLooper())  // Use main looper
//        } else {
//            // Only request permissions if they haven't been requested before
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                // Request permissions if they are not already granted
//                ActivityCompat.requestPermissions(this, permissions, 1)
//            } else {
//                // Handle the case where the user denied the permissions before
//                Log.d("Amr", "Location permissions denied")
//            }
//        }
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            // Check if all permissions are granted
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Start location updates when permissions are granted
               getFreshLocation()
            } else {
                // Handle permission denial without requesting permissions again
                Log.d("Amr", "Location permissions denied")
            }
        }
    }

    // testing location

    @SuppressLint("MissingPermission")
   override fun getFreshLocation()
    {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates( LocationRequest.Builder(5000).apply {
            setMinUpdateIntervalMillis(2000)
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        }.build(),
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.lastLocation
                    if (location != null) {
                        Log.d("Amr", "onLocationResult: ${location.latitude}, ${location.longitude}")
                        viewModel.updateLocation(location)  // Pass location to ViewModel
                        fusedLocationClient.removeLocationUpdates(this)




                      //  swipeRefreshLayout.isRefreshing = false
                    }
                }
            },
            Looper.myLooper()
        )
    }



    fun checkPermission():Boolean
    {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
    }

    fun enableLocationServices()
    {
        Toast.makeText(this,"Turn on Location " ,Toast.LENGTH_LONG).show()
        val intent=Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }
    fun isLocationEnabled():Boolean
    {
        val locationManger:LocationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManger.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
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