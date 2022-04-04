package com.example.kvest2.ui.camera

import android.hardware.Camera
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    /** A safe way to get an instance of the Camera object. */
    fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }

}