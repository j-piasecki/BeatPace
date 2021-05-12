package io.beatpace.api

import android.app.Service
import android.content.Intent
import android.os.Binder
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.gms.location.LocationServices
import io.beatpace.api.data.structures.Playlist
import io.beatpace.exceptions.NegativePaceException


class MonitoringService(

) : Service() {

    lateinit var musicController: MusicController// = MusicController(SimpleExoPlayer.Builder(this).build())
    lateinit var paceTracker: PaceTracker
    private var binder = LocalBinder()

    override fun onBind(intent: Intent?): LocalBinder {
        paceTracker = PaceTracker(applicationContext)
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        TODO("Not yet implemented")
        return super.onStartCommand(intent, flags, startId)
    }

    fun startMonitoring(playlist: Playlist, pace: Double) {
        if (pace < 0)
            throw NegativePaceException("Pace cannot be negative!")
        musicController.startPlaying(playlist, pace)
        paceTracker.startTracking()
    }

    fun stopMonitoring() {
        musicController.stopPlaying()
        paceTracker.stopTracking()
    }

    fun setPaceDisplayListener(listener: (Double) -> Unit) {}

    fun attachPlayerToView(view: StyledPlayerView) {
        musicController.attachToView(view)
    }

    private fun createNotificationChannel() {}

    private fun onPaceUpdate(pace: Double) {}

    inner class LocalBinder : Binder() {
        fun getService() = this@MonitoringService
    }
}