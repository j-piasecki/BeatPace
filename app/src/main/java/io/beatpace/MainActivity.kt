package io.beatpace

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import io.beatpace.api.MonitoringService
import io.beatpace.api.ViewModel
import io.beatpace.fragments.HomeFragmentDirections


class MainActivity : AppCompatActivity() {

    private val viewModel: ViewModel by viewModels()
    companion object {
        const val CHANNEL_ID = "MonitoringServiceChannel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bool = savedInstanceState?.getBoolean("Running")
        createNotificationChannel()
        setContentView(R.layout.activity_main)

        viewModel.loadSongs()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("Running", true)
        super.onSaveInstanceState(outState)
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(CHANNEL_ID, "Monitoring service channel", NotificationManager.IMPORTANCE_DEFAULT)

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }



}