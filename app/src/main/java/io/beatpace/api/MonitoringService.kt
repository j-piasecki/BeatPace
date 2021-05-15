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


class MonitoringService(

) : Service() {

    companion object {
        const val NOTIFICATION_ID = 1
    }

    lateinit var musicController: MusicController
    lateinit var paceTracker: PaceTracker
    private lateinit var binder: LocalBinder

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
        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.monitoringFragment)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("BeatPace pace monitoring")
            .setContentText("Your current pace: 10")
            .setSmallIcon(R.drawable.ic_android)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(NOTIFICATION_ID, notification)
        //TODO another return value
        return START_NOT_STICKY
    }

    fun startMonitoring(playlist: Playlist, pace: Double) {
        if (pace < 0)
            throw NegativePaceException("Pace cannot be negative!")

        musicController.attachToView(StyledPlayerView(this))
        paceTracker.setOnUpdateListener(this::onPaceUpdate)

        musicController.startPlaying(playlist, pace)
        paceTracker.startTracking()

        notify(setUpNotification(0.0, pace))
    }

    fun stopMonitoring() {
        musicController.stopPlaying()
        paceTracker.stopTracking()
    }

    fun setPaceDisplayListener(listener: (Double) -> Unit) {}

    fun attachPlayerToView(view: StyledPlayerView) {
        musicController.attachToView(view)
    }

    private fun setUpNotification(currPace: Double, maxPace: Double): Notification {
        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.monitoringFragment)
            .createPendingIntent()


        val currentSong = musicController.getCurrentSong()

        return NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle("BeatPace pace tracker")
            setContentText("Your current speed: $currPace, given pace: $maxPace")
            setSmallIcon(R.drawable.ic_android)
            setContentIntent(pendingIntent)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            //color = ContextCompat.getColor(this@MonitoringService, R.color.common_google_signin_btn_text_light)
            addAction(
                NotificationCompat.Action(
                    R.drawable.ic_android,
                    getString(R.string.app_name),
                    PendingIntent.getBroadcast(
                        this@MonitoringService,
                        1, Intent(this@MonitoringService, MusicController::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
            )
        }.build()
    }

    private fun notify(notification: Notification) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun onPaceUpdate(pace: Double) {
        notify(setUpNotification(pace, 5.0))

        musicController.onPaceUpdate(pace)
    }

    inner class LocalBinder : Binder() {
        fun getService() = this@MonitoringService
    }

    inner class DescriptionAdapter : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): String {
            val window = player.currentWindowIndex
            return "Title"
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent {
            val window = player.currentWindowIndex
            val pendingIntent = NavDeepLinkBuilder(this@MonitoringService)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.monitoringFragment)
                .createPendingIntent()
            return pendingIntent
        }

        override fun getCurrentContentText(player: Player): String {
            val window = player.currentWindowIndex
            return "Description"
        }

        override fun getCurrentLargeIcon(player: Player, callback: BitmapCallback): Bitmap? {
            return null
        }

    }

}