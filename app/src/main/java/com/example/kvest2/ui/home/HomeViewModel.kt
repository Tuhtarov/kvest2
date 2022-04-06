package com.example.kvest2.ui.home

import android.hardware.Camera
import android.location.LocationManager
import android.location.LocationRequest
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kvest2.data.model.AppUserSingleton
import com.example.kvest2.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.net.URI.create
import java.util.jar.Manifest

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    /** A safe way to get an instance of the Camera object. */
    lateinit var locationManager: LocationManager

    fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }



}