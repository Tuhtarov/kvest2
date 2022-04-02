package com.example.kvest2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.R
import com.example.kvest2.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, HomeViewModelFactory(requireContext()))
            .get(HomeViewModel::class.java)

        binding = HomeFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }
}