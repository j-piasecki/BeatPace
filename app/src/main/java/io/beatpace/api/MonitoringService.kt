package io.beatpace.api

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback
import com.google.android.exoplayer2.ui.StyledPlayerView
import io.beatpace.MainActivity.Companion.CHANNEL_ID
import io.beatpace.R
import io.beatpace.api.data.structures.Playlist
import io.beatpace.exceptions.NegativePaceException
import io.beatpace.fragments.MonitoringFragment


class MonitoringService: Service() {

    companion object {
        const val NOTIFICATION_ID = 1
        var isRunning = false
    }

    lateinit var musicController: MusicController
    lateinit var paceTracker: PaceTracker
    private lateinit var binder: LocalBinder
    private var maxPace = 0.0
    private var listener: ((Double) -> Unit)? = null

    override fun onCreate() {
        super.onCreate()
        this.binder = LocalBinder()
        val exoPlayer = SimpleExoPlayer.Builder(this).build()
        this.musicController = MusicController(exoPlayer)
        this.paceTracker = PaceTracker(this)
    }

    override fun onBind(intent: Intent?): LocalBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, setUpNotification(paceTracker.getCurrentPace()))
        return START_REDELIVER_INTENT
    }

    fun startMonitoring(playlist: Playlist, pace: Double) {
        if (pace < 0)
            throw NegativePaceException("Pace cannot be negative!")

        musicController.attachToView(StyledPlayerView(this))
        paceTracker.setOnUpdateListener(this::onPaceUpdate)

        if (!isRunning) {
            isRunning = true
            maxPace = pace

            musicController.startPlaying(playlist, pace)
            paceTracker.startTracking()
        }
    }

    fun stopMonitoring() {
        musicController.stopPlaying()
        paceTracker.stopTracking()
        isRunning = false;
    }

    fun setPaceDisplayListener(listener: (Double) -> Unit) {
        this.listener = listener
    }

    fun attachPlayerToView(view: StyledPlayerView) {
        musicController.attachToView(view)
    }

    fun getRunDistance() = paceTracker.getRunDistance()

    fun getRunDuration() = paceTracker.getRunDuration()

    private fun setUpNotification(currPace: Double): Notification {
        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.monitoringFragment)
            .createPendingIntent()

        return NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle("BeatPace pace tracker")
            setContentText("Your current speed: $currPace, given pace: $maxPace")
            setSmallIcon(R.drawable.ic_android)
            setContentIntent(pendingIntent)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        }.build()
    }

    private fun notify(notification: Notification) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun onPaceUpdate(pace: Double) {
        notify(setUpNotification(pace))

        musicController.onPaceUpdate(pace)
        listener?.invoke(pace)
    }

    inner class LocalBinder : Binder() {
        fun getService() = this@MonitoringService
    }

}