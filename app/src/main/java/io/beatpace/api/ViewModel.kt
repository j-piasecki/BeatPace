package io.beatpace.api

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.beatpace.api.data.DataConfig

class ViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var songs: Map<Long, String>

    fun getPlaylistManager(): PlaylistManager {
        TODO("Not yet implemented")
    }

    fun getDataConfig(): DataConfig {
        TODO("Not yet implemented")
    }

    fun loadSongs() {}

    fun getSongs(): Map<Long, String> {
        TODO("Not yet implemented")
    }
}