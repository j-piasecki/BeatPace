package io.beatpace.api

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult


class PaceTracker(private var fusedLocationClient: FusedLocationProviderClient) {
    private lateinit var locationCallback: LocationCallback
    private var previousLocation: Location? = null
    private lateinit var onUpdateListener: (Double) -> Unit

    fun startTracking(context: Context) {
        val request: LocationRequest = LocationRequest.create()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener {
                location : Location? ->
                previousLocation = location
        }
        locationCallback = createLocationCallback()
        
        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopTracking() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun calculatePace(startLocation: Location?, finalLocation: Location): Double {

        return 0.0
    }

    private fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val pace = calculatePace(previousLocation, locationResult.lastLocation)
                previousLocation = locationResult.lastLocation
                onUpdateListener(pace)
            }
        }
    }

    fun setOnUpdateListener(listener: (Double) -> Void) {}

}