package io.beatpace.api.data

import android.content.Context

class DataConfig private constructor(private var selectedPlaylistId: Int, private var selectedPace: Double) {

    fun setPace(pace: Double) {}

    fun setPlaylistId(playlistId: Int) {}

    fun getSelectedPace(): Double {
        TODO("Not yet implemented")
    }

    fun getSelectedPlaylistId(): Int {
        TODO("Not yet implemented")
    }

    companion object {
        fun loadSavedData(context: Context): DataConfig {
            TODO("Not yet implemented")
        }
    }
}