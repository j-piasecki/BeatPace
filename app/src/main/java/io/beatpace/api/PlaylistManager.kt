package io.beatpace.api

import io.beatpace.api.data.structures.Playlist
import io.beatpace.database.DAO

class PlaylistManager(private val dao: DAO) {

    private lateinit var playlists: MutableMap<Int, Playlist>

    fun getAllPlaylists(): List<Playlist> {
        TODO("Not yet implemented")
    }

    fun getPlaylistById(playlistId: Int): Playlist {
        TODO("Not yet implemented")
    }

    fun deletePlaylistById(playlistId: Int) {}

    fun createPlaylist(playlistName: String): Int {
        TODO("Not yet implemented")
    }

    fun addSongToPlaylist(playlistId: Int, songId: Long) {}

    fun removeSongFromPlaylist(playlistId: Int, songId: Long) {}

}