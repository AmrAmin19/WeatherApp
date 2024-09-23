package com.example.weatherapp.view.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFavBinding
import com.example.weatherapp.model.Repo
import com.example.weatherapp.model.local.LocalData
import com.example.weatherapp.model.remote.RemoteData
import com.example.weatherapp.viewModel.FavFactory
import com.example.weatherapp.viewModel.FavViewModel
import com.example.weatherapp.viewModel.MainActivityViewModel


class FavFragment : Fragment() {

    lateinit var binding:FragmentFavBinding
    lateinit var factory: FavFactory
    lateinit var favViewModel: FavViewModel
    lateinit var mainViewModel: MainActivityViewModel

    lateinit var myAdapter: FavAdapter

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        factory= FavFactory(Repo.getInstance(RemoteData(), LocalData(requireContext())))
        favViewModel=ViewModelProvider(this,factory).get(FavViewModel::class.java)

        mainViewModel= ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

         swipeRefreshLayout=requireActivity().findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        binding=FragmentFavBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myAdapter= FavAdapter { favViewModel.delet(it) }
        binding.FavRecycleView.layoutManager= LinearLayoutManager(context)
        binding.FavRecycleView.adapter=myAdapter


        mainViewModel.favlocationLiveData.observe(viewLifecycleOwner, Observer {location ->
            val (lat, lon) = location
            favViewModel.fetchDataFromApi(lat, lon)
            swipeRefreshLayout.isEnabled = true

        })


        binding.fabOpenMap.setOnClickListener {

           findNavController().navigate(R.id.action_favFragment_to_mapFragment)

        }

        favViewModel.favWeather.observe(viewLifecycleOwner, Observer {
            myAdapter.submitList(it)
        })
    }



}
