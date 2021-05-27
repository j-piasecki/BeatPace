package io.beatpace.api

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import io.beatpace.MainActivity.Companion.CHANNEL_ID
import io.beatpace.R
import io.beatpace.api.data.structures.Playlist
import io.beatpace.exceptions.NegativePaceException
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Starts monitoring and controls PaceTracker and MusicController
 */

class MonitoringService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1
        var isRunning = false
    }

    lateinit var musicController: MusicController
    lateinit var paceTracker: PaceTracker
    private lateinit var binder: LocalBinder
    private var maxPace = 0.0
    private var listener: ((Double, Double) -> Unit)? = null

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

    /**
     * Begins monitoring if not yet started
     */
    fun startMonitoring(playlist: Playlist, pace: Double) {
        if (pace < 0)
            throw NegativePaceException("Pace cannot be negative!")

        paceTracker.setOnUpdateListener(this::onPaceUpdate)
        
        if (!isRunning) {
            isRunning = true
            maxPace = BigDecimal.valueOf(pace).setScale(2, RoundingMode.HALF_UP).toDouble()

            musicController.startPlaying(playlist, pace)
            paceTracker.startTracking()
        }
    }

    fun stopMonitoring() {
        musicController.stopPlaying()
        paceTracker.stopTracking()
        isRunning = false;
    }

    fun setPaceDisplayListener(listener: (Double, Double) -> Unit) {
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
            setContentTitle(getString(R.string.pace_tracker))
            setContentText(getString(R.string.notification_text, currPace))
            setSmallIcon(R.drawable.ic_servicelogo)
            color = getColor(R.color.black)
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
        listener?.invoke(pace, maxPace)
    }

    inner class LocalBinder : Binder() {
        fun getService() = this@MonitoringService
    }

}