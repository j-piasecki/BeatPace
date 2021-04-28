package io.beatpace.database

import androidx.room.*

@Dao
interface DAO {

    @Query("SELECT * FROM playlists")
    fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_content WHERE playlistId = (:playlistId)")
    fun getAllSongs(playlistId: Int): List<PlaylistContent>

    @Insert
    fun insertNewSong(content: PlaylistContent)

    @Insert
    fun createNewPlaylist(playlist: PlaylistEntity): Long

    @Delete
    fun deletePlaylist(playlist: PlaylistEntity)

    @Delete
    fun removeSong(content: PlaylistContent)
}