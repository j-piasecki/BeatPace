package io.beatpace.api

import android.app.Application
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import io.beatpace.api.data.DataConfig
import io.beatpace.database.AppDatabase

/**
 * Eases access between classes, fragments and storage
 */

class ViewModel(private val app: Application) : AndroidViewModel(app) {

    private var songs = HashMap<Long, String>()

    private val database: AppDatabase = Room.databaseBuilder(app, AppDatabase::class.java, "database")
        .allowMainThreadQueries()
        .build()

    private val dataConfig = DataConfig.loadSavedData(app)
    private val playlistManager = PlaylistManager(database.dao())

    fun getPlaylistManager(): PlaylistManager {
        return playlistManager
    }

    fun getDataConfig(): DataConfig {
        return dataConfig
    }

    fun getSongs(): Map<Long, String> {
        return songs
    }

    /**
     * Loads media from system
     */
    fun loadSongs() {
        val cursor = app.contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE),
            MediaStore.Audio.Media.IS_MUSIC + "!= 0",
            null,
            MediaStore.Audio.Media.TITLE
        )

        songs.clear()

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                songs[cursor.getLong(0)] = cursor.getString(1)
            }
        }
    }
}