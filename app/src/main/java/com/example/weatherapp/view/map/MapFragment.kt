package com.example.weatherapp.view.map

import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentMapBinding
import com.example.weatherapp.model.ApiState
import com.example.weatherapp.model.LOCATION_KEYS
import com.example.weatherapp.model.MapArgs
import com.example.weatherapp.model.Repo
import com.example.weatherapp.model.SharedPreferencesKeys
import com.example.weatherapp.model.local.LocalData
import com.example.weatherapp.model.local.SharedPreferences
import com.example.weatherapp.model.remote.RemoteData
import com.example.weatherapp.viewModel.MainActivityViewModel
import com.example.weatherapp.viewModel.MainFactory
import com.example.weatherapp.viewModel.MapFactory
import com.example.weatherapp.viewModel.MapViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

class MapFragment : Fragment() {

    lateinit var binding:FragmentMapBinding
    private var marker: Marker? = null
    lateinit var mainFactory:MainFactory
    lateinit var mainViewModel: MainActivityViewModel
    lateinit var viewmodel:MapViewModel
    lateinit var factory: MapFactory
    lateinit var adapter: RecyclerViewAdapter // Adapter for search results

    private var sourceFragment: String? = null



    var lat :Double=0.0
    var lon:Double=0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       sourceFragment = arguments?.getString(MapArgs.SourceFragment_Key)

        mainFactory=MainFactory(Repo.getInstance(RemoteData(), LocalData(requireContext()),SharedPreferences(requireContext())))
        mainViewModel= ViewModelProvider(requireActivity(),mainFactory).get(MainActivityViewModel::class.java)

        factory= MapFactory(Repo.getInstance(RemoteData(), LocalData(requireContext()),SharedPreferences(requireContext())))
        viewmodel=ViewModelProvider(this,factory).get(MapViewModel::class.java)

        binding= FragmentMapBinding.inflate(inflater,container,false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()

        adapter = RecyclerViewAdapter(ArrayList()) { selectedLocation ->
            getCoordinates(selectedLocation)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        setupSearch()

        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
       binding.map.setTileSource(TileSourceFactory.MAPNIK)
        binding.map.setMultiTouchControls(true)
        binding.map.controller.setZoom(15.0)
        val startPoint = GeoPoint(31.1843, 29.92) // Alexandria
        binding.map.controller.setCenter(startPoint)

       viewLifecycleOwner.lifecycleScope.launch {
           viewmodel.cityLocatio.collect{
             if (it != null)
             {

                     updateMarkerPosition(GeoPoint(it.lat,it.lon))
                     binding.map.controller.setCenter(GeoPoint(it.lat,it.lon))
                     binding.recyclerView.visibility = View.GONE

                 Log.d("FlowlocationResult ",  "${it.lat}  ${it.lon}")
             }
           }
       }

        viewmodel.filteredCities.observe(viewLifecycleOwner, Observer { filteredList ->
            adapter.setNames(ArrayList(filteredList))
            binding.recyclerView.visibility = if (filteredList.isEmpty()) View.GONE else View.VISIBLE
        })


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


        val mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let {
                    updateMarkerPosition(it)
                    lat=it.latitude
                    lon=it.longitude
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }

        val overlayEvents = MapEventsOverlay(mapEventsReceiver)
        binding.map.overlays.add(overlayEvents)




        when(sourceFragment)
        {
            MapArgs.FavFragment_Source-> binding.btnOk.setImageResource(R.drawable.baseline_favorite_24)
            MapArgs.SettingsFragment_Source-> binding.btnOk.setImageResource(R.drawable.baseline_add_24)
        }

        lifecycleScope.launch {
            viewmodel.forecastWeather.collect{
                when(it){
                    is ApiState.Error->{
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is ApiState.Loading->{
//                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }
                    is ApiState.Success->{
                        viewmodel.insretFavWeather(it.data)
                    }
                }
            }
        }


        binding.btnOk.setOnClickListener {


            when(sourceFragment)
            {
                MapArgs.FavFragment_Source-> {

                    // changed from lon > 0 --> lon !=0
                    if (lat != 0.0 && lon != 0.0) {

                        viewmodel.fetchDataFromApi(lat,lon)
                    } else {
                        Toast.makeText(requireContext(), "No location selected", Toast.LENGTH_SHORT).show()
                    }

                }
                MapArgs.SettingsFragment_Source->{
                    mainViewModel.updateLocation(Location("").apply {
                        latitude = lat
                        longitude = lon
                    })
                    viewmodel.AddLocationPrefs(LOCATION_KEYS.LATITUDE, lat)
                    viewmodel.AddLocationPrefs(LOCATION_KEYS.LONGITUDE, lon)

                    findNavController().popBackStack()

                }
            }


        }

    }

    private fun updateMarkerPosition(location: GeoPoint) {
        if (marker == null) {
            marker = Marker(binding.map)
            marker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker?.title = "Selected Location"
            binding.map.overlays.add(marker)
        }
        marker?.position = location
        lat=location.latitude
        lon=location.longitude
        binding.map.invalidate() // Refresh map
    }



    override fun onResume() {
        super.onResume()
        binding.map.onResume()
        Log.d("TAG", "onResume: ")

    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }






    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.show()
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText?.trim()?.lowercase() ?: ""
                if (query.isNotEmpty()) {
                    viewmodel.updateSearchQuery(newText ?: "")

                } else {
                    binding.recyclerView.visibility = View.GONE
                }
                return true
            }
        })

    }

    private fun getCoordinates(location: String) {
      viewmodel.getLocation(location)
    }


}

