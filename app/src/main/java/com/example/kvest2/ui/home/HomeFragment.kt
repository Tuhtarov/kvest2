package com.example.kvest2.ui.home

import android.content.ContentValues
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.R
import com.example.kvest2.databinding.HomeFragmentBinding
import com.example.kvest2.ui.camera.CameraPreview
import java.io.IOException

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


        var mCamera: Camera? = null

        mCamera = viewModel.getCameraInstance()

        var mPreview : CameraPreview? = null

        mPreview = mCamera?.let {
            CameraPreview(requireContext(),it)
        }
        mPreview?.also {
            val preview: FrameLayout = binding.cameraPreview
            preview.addView(it)
        }


        return binding.root
    }
}