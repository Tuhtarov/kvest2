package com.example.kvest2.ui.camera

import android.content.ContentValues.TAG
import android.hardware.Camera
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.kvest2.R
import com.example.kvest2.databinding.CameraFragmentBinding
import com.example.kvest2.databinding.LoginFragmentBinding
import java.io.IOException

class CameraFragment : Fragment() {

    companion object {
        fun newInstance() = CameraFragment()
    }

    private lateinit var viewModel: CameraViewModel
    private var _binding: CameraFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = CameraFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(CameraViewModel::class.java)

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
        // TODO: Use the ViewModel
    }

}