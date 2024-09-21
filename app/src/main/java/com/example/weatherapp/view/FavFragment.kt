package com.example.weatherapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFavBinding

class FavFragment : Fragment() {

    lateinit var binding:FragmentFavBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentFavBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabOpenMap.setOnClickListener {
           findNavController().navigate(R.id.action_favFragment_to_mapFragment)
          //  openMapFragment()

        }
    }


    private fun openMapFragment() {
        // Replace HomeFragment with MapFragment using FragmentTransaction
        val mapFragment = MapFragment()
        parentFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, mapFragment) // Replace the container with MapFragment
            .addToBackStack(null) // Add this transaction to the back stack so the user can navigate back
            .commit()
    }


}
