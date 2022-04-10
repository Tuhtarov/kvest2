package com.example.kvest2.ui.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.R
import com.example.kvest2.data.model.AppCurrentTasksSingleton
import com.example.kvest2.databinding.FragmentMapsBinding
import com.example.kvest2.databinding.HomeFragmentBinding
import com.example.kvest2.databinding.QuestFragmentBinding
import com.example.kvest2.ui.camera.CameraViewModel
import com.example.kvest2.ui.home.HomeViewModel
import com.example.kvest2.ui.home.HomeViewModelFactory
import com.example.kvest2.ui.quest.QuestSharedViewModel
import com.example.kvest2.ui.quest.QuestViewModelFactory

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    //private lateinit var mMap: GoogleMap
    private val callback = OnMapReadyCallback { googleMap ->
        //val haga = LatLng(53.7223716, 91.4403476,)
        //googleMap.addMarker(MarkerOptions().position(haga).title("Кто умнее всех на свете"))
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(haga))
        //TODO зум на первую точку или текущее местоположение
        var index = 0
        AppCurrentTasksSingleton.currentTasks.observe(viewLifecycleOwner) {
            it?.forEach {

                index++
                var taskLocation = LatLng(it.task.latitude.toDouble(), it.task.longitude.toDouble())
                googleMap.addMarker(MarkerOptions().position(taskLocation).title(index.toString()))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(taskLocation))
            }
        }
    }
    private lateinit var binding: FragmentMapsBinding

    private val viewModel: MapsViewModel by activityViewModels {
        MapsViewModelFactory(binding.root.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)

        AppCurrentTasksSingleton.currentTasks.observe(viewLifecycleOwner) {
            it?.forEach {
                Toast.makeText(
                    context,
                    "Точка ${it.task.latitude} ${it.task.longitude}", Toast.LENGTH_SHORT
                )
                    .show()
                //mMap.addMarker(MarkerOptions().position(LatLng(it.task.latitude.toDouble(),it.task.longitude.toDouble())).title(it.task.question))
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


}