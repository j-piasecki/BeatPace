package io.beatpace.api

import android.content.ContentUris
import android.provider.MediaStore
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import io.beatpace.api.data.structures.Playlist
import java.util.*
import kotlin.math.pow

class MusicController(private val exoPlayer: SimpleExoPlayer) {

    private var targetPace: Double = 0.0
    private var canStartPlaying: Boolean = true

    fun startPlaying(playlist: Playlist, pace: Double) {
        if (canStartPlaying) {
            exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
            this.targetPace = pace
            val mediaItemList = getMediaItemList(playlist)
            exoPlayer.addMediaItems(mediaItemList)
            exoPlayer.prepare()
            exoPlayer.play()
            canStartPlaying = false
        }
    }

    fun stopPlaying() {
        exoPlayer.stop()
        exoPlayer.release()
    }

    fun attachToView(view: StyledPlayerView) {
        view.player = exoPlayer
    }

    fun onPaceUpdate(pace: Double) {
        exoPlayer.volume = (pace / targetPace).toFloat()
    }

    private fun getMediaItemList(playlist: Playlist): List<MediaItem> {
        val mediaItemList = ArrayList<MediaItem>()

        for (i in 0 until playlist.getSize()) {
            val id = playlist.getSong(i)
            val item = MediaItem.fromUri(
                ContentUris.appendId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.buildUpon(), id
                ).build()
            )

            mediaItemList.add(item)
        }

        return mediaItemList
    }

}