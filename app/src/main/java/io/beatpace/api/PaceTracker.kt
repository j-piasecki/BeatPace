package io.beatpace.api

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*


class PaceTracker(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private var onUpdateListener: ((Double) -> Unit)? = null
    private var locationCallback = createLocationCallback()

    fun startTracking() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }

        val request: LocationRequest = LocationRequest.create()
        request.interval = 2000
        request.fastestInterval = 1500
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopTracking() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun calculatePace(location: Location): Double {
        return location.speed.toDouble()
    }

    private fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val pace = calculatePace(locationResult.lastLocation)
                onUpdateListener?.invoke(pace)
            }
        }
    }

    fun setOnUpdateListener(listener: (Double) -> Unit) {
        onUpdateListener = listener
    }
}