package com.example.weatherapp.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentMapBinding
import com.example.weatherapp.model.Repo
import com.example.weatherapp.model.local.LocalData
import com.example.weatherapp.model.remote.RemoteData
import com.example.weatherapp.viewModel.MainActivityViewModel
import com.example.weatherapp.viewModel.MapFactory
import com.example.weatherapp.viewModel.MapViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker


class MapFragment : Fragment() {

    lateinit var binding:FragmentMapBinding
    private var marker: Marker? = null
    lateinit var mainViewModel: MainActivityViewModel
    lateinit var viewmodel:MapViewModel
    lateinit var factory: MapFactory
   // private lateinit var swipeRefreshLayout: SwipeRefreshLayout



    var lat :Double=0.0
    var lon:Double=0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //swipeRefreshLayout=requireActivity().findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)


        factory= MapFactory(Repo.getInstance(RemoteData(), LocalData(requireContext())))
        viewmodel=ViewModelProvider(this,factory).get(MapViewModel::class.java)

        binding= FragmentMapBinding.inflate(inflater,container,false)
        mainViewModel= ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



       // viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        // Initialize map

        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
       binding.map.setTileSource(TileSourceFactory.MAPNIK)
        binding.map.setMultiTouchControls(true)
        binding.map.controller.setZoom(15.0)
        val startPoint = GeoPoint(31.1843, 29.92) // Alexandria
        binding.map.controller.setCenter(startPoint)


        viewmodel.resultInsert.observe(viewLifecycleOwner, Observer {
           if (it>0)
           {
               Toast.makeText(requireContext(), "Added to Favorite", Toast.LENGTH_LONG).show()
               findNavController().popBackStack()
           }
            else
            {
                Toast.makeText(requireContext(), "did not insert", Toast.LENGTH_SHORT).show()
            }
        })

        // Add event listener for map taps
        val mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let {
                    updateMarkerPosition(it)
                    lat=it.latitude
                    lon=it.longitude
//                    mainViewModel.updateFavLocation(it)
                 //   viewModel.setSelectedLocation(it) // Update ViewModel
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }

        val overlayEvents = MapEventsOverlay(mapEventsReceiver)
        binding.map.overlays.add(overlayEvents)

        // Handle "OK" button click
        binding.btnOk.setOnClickListener {
           // val location = viewModel.getSelectedLatLon()
            // changed from lon > 0 --> lon !=0
            if (lat != 0.0 && lon != 0.0) {
               // val (lat, lon) = location
               // mainViewModel.updateFavLocation(lat,lon)
              //  showConfirmationDialog(lat,lon)

                viewmodel.fetchDataFromApi(lat,lon)

//                Toast.makeText(requireContext(), "Lat: $lat, Lon: $lon", Toast.LENGTH_LONG).show()

               // findNavController().navigate(R.id.action_mapFragment_to_favFragment)

//                findNavController().popBackStack()
                // Pass data to another fragment or use as needed
            } else {
                Toast.makeText(requireContext(), "No location selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateMarkerPosition(location: GeoPoint) {
        if (marker == null) {
            // Create a new marker if it doesn't exist
            marker = Marker(binding.map)
            marker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker?.title = "Selected Location"
            binding.map.overlays.add(marker)
        }
        // Update the marker's position
        marker?.position = location

        // Refresh map to show the updated marker
        binding.map.invalidate()
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
        Log.d("TAG", "onResume: ")
       // swipeRefreshLayout.isEnabled = false

    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
        //swipeRefreshLayout.isEnabled = true
    }



    private fun showConfirmationDialog( lat:Double,lon:Double) {
        AlertDialog.Builder(requireContext())
            .setTitle("Add to Favorites")
            .setMessage("Do you want to add to your favorites?")
            .setPositiveButton("OK") { _, _ ->  viewmodel.fetchDataFromApi(lat,lon) }
            .setNegativeButton("Cancel", null)
            .show()
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//
//
//    }
}