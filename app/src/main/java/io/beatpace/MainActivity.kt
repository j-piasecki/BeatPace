package io.beatpace

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import io.beatpace.api.MonitoringService
import io.beatpace.api.ViewModel
import io.beatpace.fragments.HomeFragmentDirections


class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "MonitoringServiceChannel"

        private const val REQUEST_CODE_STORAGE = 1
        private const val REQUEST_CODE_LOCATION = 2
    }

    private val viewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            viewModel.loadSongs()
        else
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_STORAGE)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_STORAGE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.loadSongs()
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_STORAGE)
                }
            }

            REQUEST_CODE_LOCATION -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
                }
            }
        }
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(CHANNEL_ID, "Monitoring service channel", NotificationManager.IMPORTANCE_DEFAULT)

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }



}