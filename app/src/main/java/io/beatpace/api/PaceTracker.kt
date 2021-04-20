package io.beatpace.api

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback

class PaceTracker {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var onUpdateListener: (Double) -> Unit

    fun startTracking() {}

    fun stopTracking() {}

    fun setOnUpdateListener(listener: (Double) -> Void) {}

    private fun calculatePace() {}

}