package io.beatpace.api

import android.view.View
import com.google.android.exoplayer2.ExoPlayer
import io.beatpace.api.data.structures.Playlist

class MusicController {

    private lateinit var exoPlayer: ExoPlayer
    private var targetPace: Double = 0.0


    fun startPlaying(playlist: Playlist, pace: Double) {}

    fun stopPlaying() {}

    fun attachToView(view: View) {}

    fun onPaceUpdate(pace: Double) {}

}