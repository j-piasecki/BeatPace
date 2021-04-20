package io.beatpace.api

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.View
import io.beatpace.api.data.structures.Playlist

class MonitoringService : Service() {

    private lateinit var binder: IBinder
    private lateinit var musicController: MusicController

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        TODO("Not yet implemented")
        return super.onStartCommand(intent, flags, startId)
    }

    fun startMonitoring(playlist: Playlist, pace: Double) {}

    fun stopMonitoring() {}

    fun setPaceDisplayListener(listener: (Double) -> Unit) {}

    fun attachPlayerToView(view: View) {}

    private fun createNotificationChannel() {}

    private fun onPaceUpdate(pace: Double) {}

}