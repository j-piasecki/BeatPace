package io.beatpace.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
        @PrimaryKey(autoGenerate = true) val playlistId: Int? = null,
        val title: String
)
