package com.example.kvest2.ui.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.R
import com.example.kvest2.databinding.FragmentMapsBinding
import com.example.kvest2.databinding.HomeFragmentBinding
import com.example.kvest2.ui.camera.CameraViewModel
import com.example.kvest2.ui.home.HomeViewModel
import com.example.kvest2.ui.home.HomeViewModelFactory

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    private lateinit var mMap: GoogleMap
    private val callback = OnMapReadyCallback { googleMap ->
        val haga = LatLng(53.7247404, 91.4432434,)
        googleMap.addMarker(MarkerOptions().position(haga).title("Любимая шарага"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(haga))

    }
    private lateinit var binding: FragmentMapsBinding

    private lateinit var viewModel: MapsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =  ViewModelProvider(this).get(MapsViewModel::class.java)

        binding = FragmentMapsBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


}