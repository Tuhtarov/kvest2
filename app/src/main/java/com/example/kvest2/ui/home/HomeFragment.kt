package com.example.kvest2.ui.home

import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.hardware.Camera
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.R
import com.example.kvest2.databinding.HomeFragmentBinding
import com.example.kvest2.ui.camera.CameraPreview
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    private lateinit var binding: HomeFragmentBinding
    private val CAMERA_REQUEST_CODE = 10001
    private lateinit var layout: View

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, HomeViewModelFactory(requireContext()))
            .get(HomeViewModel::class.java)

        binding = HomeFragmentBinding.inflate(inflater, container, false)

        startCamera()

        return binding.root
    }
    private  fun startCamera()
    {
        var mCamera: Camera? = null

        mCamera = viewModel.getCameraInstance()

        var mPreview: CameraPreview? = null

        mPreview = mCamera?.let {
            CameraPreview(requireContext(), it)
        }
        mPreview?.also {
            val preview: FrameLayout = binding.cameraPreview
            preview.addView(it)
        }
    }



}