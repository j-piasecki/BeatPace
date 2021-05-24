package io.beatpace.api

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlin.math.round

class PaceTracker(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private var onUpdateListener: ((Double) -> Unit)? = null
    private var locationCallback = createLocationCallback()

    private var currentPace = 0.0
    private var previousLocation: Location? = null
    private var previousTime = 0L

    private var runDuration = 0L
    private var runDistance = 0.0

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

        currentPace = 0.0
        previousTime = System.currentTimeMillis()
    }

    fun stopTracking() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun getRunDistance() = runDistance

    fun getRunDuration() = runDuration

    fun getCurrentPace() = currentPace

    private fun calculatePace(location: Location): Double {
        val elapsedTime = System.currentTimeMillis() - previousTime
        val distance = if (previousLocation == null) 0.0 else location.distanceTo(previousLocation).toDouble()
        currentPace = round(distance * 1000 / elapsedTime * 100) / 100

        runDistance += distance
        runDuration += elapsedTime

        previousLocation = location
        previousTime = System.currentTimeMillis()

        return currentPace
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