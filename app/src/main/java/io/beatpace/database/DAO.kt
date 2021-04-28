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

    @Query("INSERT INTO playlists VALUES(NULL, :title)")
    fun createNewPlaylist(title: String): Long

    @Insert
    fun createNewPlaylist(entity: PlaylistEntity): Long

    @Query("DELETE FROM playlists WHERE playlistId=:id")
    fun deletePlaylist(id: Int)

    @Delete
    fun removeSong(content: PlaylistContent)
}