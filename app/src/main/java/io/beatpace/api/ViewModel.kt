package io.beatpace.api

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import io.beatpace.api.data.DataConfig
import io.beatpace.database.AppDatabase

class ViewModel(application: Application) : AndroidViewModel(application) {

    private var songs = HashMap<Long, String>()

    private val database: AppDatabase = Room.databaseBuilder(application, AppDatabase::class.java, "database").build()

    private val dataConfig = DataConfig.loadSavedData(application)
    private val playlistManager = PlaylistManager(database.dao())

    fun getPlaylistManager(): PlaylistManager {
        return playlistManager
    }

    fun getDataConfig(): DataConfig {
        return dataConfig
    }

    fun loadSongs() {
        TODO("Nie ma wczytywania piosenek")
    }

    fun getSongs(): Map<Long, String> {
        return songs
    }
}