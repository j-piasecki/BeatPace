package io.beatpace.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
        tableName = "playlist_content",
        primaryKeys = ["playlistId", "songId"],
        foreignKeys = [ForeignKey(entity = PlaylistEntity::class, parentColumns = ["playlistId"], childColumns = ["playlistId"], onDelete = ForeignKey.CASCADE)]
)
data class PlaylistContent(
    val playlistId: Int,
    val songId: Long
)
