package io.beatpace.api

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import io.beatpace.api.data.structures.Playlist
import java.util.*

class MusicController(private val exoPlayer: SimpleExoPlayer) {

    private var targetPace: Double = 0.0

    fun startPlaying(playlist: Playlist, pace: Double) {
        this.targetPace = pace
        val songs = getMediaSourceList(playlist)
        exoPlayer.addMediaSources(songs)

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
        /*
            Some operations that returns target volume
         */


    }

    private fun getMediaSourceList(playlist: Playlist) : List<MediaSource> {
        val list = LinkedList<Long>()
        playlist.getSize()
        return LinkedList()
    }

}