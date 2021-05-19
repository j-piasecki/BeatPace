package io.beatpace.api

import android.content.ContentUris
import android.provider.MediaStore
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import io.beatpace.api.data.structures.Playlist
import java.util.*

class MusicController(private val exoPlayer: SimpleExoPlayer) {

    private var targetPace: Double = 0.0

    fun startPlaying(playlist: Playlist, pace: Double) {
        this.targetPace = pace
        val mediaItemList = getMediaItemList(playlist)
        exoPlayer.addMediaItems(mediaItemList)

        exoPlayer.prepare()
        exoPlayer.play()
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

        //TODO remove
        val a =
            MediaItem.fromUri("file:///android_asset/Game of Thrones S8 Official Soundtrack A Song of Ice and Fire - Ramin Djawadi WaterTower.mp3");

        return LinkedList(listOf(a))
    }

    fun getCurrentSong(): MediaItem? {
        return exoPlayer.currentMediaItem
    }

    /* ----------- */


}