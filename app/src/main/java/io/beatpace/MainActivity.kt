package io.beatpace

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import io.beatpace.api.MonitoringService
import io.beatpace.api.ViewModel
import javax.security.auth.callback.PasswordCallback


class MainActivity : AppCompatActivity() {

    private val viewModel: ViewModel by viewModels()
    companion object {
        const val CHANNEL_ID = "MonitoringServiceChannel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        setContentView(R.layout.activity_main)
        viewModel.loadSongs()
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(CHANNEL_ID, "Monitoring service channel", NotificationManager.IMPORTANCE_DEFAULT)

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

}