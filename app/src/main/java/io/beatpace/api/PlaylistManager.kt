package io.beatpace.api

import io.beatpace.api.data.structures.Playlist
import io.beatpace.database.DAO
import io.beatpace.database.PlaylistContent
import io.beatpace.database.PlaylistEntity

class PlaylistManager(private val dao: DAO) {

    private lateinit var playlists: MutableMap<Int, Playlist>

    init{ // get all playlists and save them into our playlists map

        var entityList = dao.getPlaylists() // a temporary PlaylistEntityList

        for ( i in entityList.indices){ // creating and adding all our playlists into our map from the database

            var songList = ArrayList<Long>()// a list of all the songs in current playlist

            var contentList = dao.getAllSongs(entityList[i].playlistId!!)

            for (j in contentList.indices){ // convert playlistContent list into a list of songId's (Long)
                songList.add(contentList[j].songId)
            }

            var newPlaylist = Playlist( entityList[i].title, entityList[i].playlistId!!, songList) // Creating our playlist so we can add it into our map

            playlists.put(newPlaylist.Id, newPlaylist) // adding the playlist into the map
        }
    }

    fun getAllPlaylists(): List<Playlist> { // turn out playlists map into a list
        val playlistList: List<Playlist> = playlists.values.toList()
        return  playlistList
    }

    fun getPlaylistById(playlistId: Int): Playlist? {
        return playlists[playlistId]
    }

    fun deletePlaylistById(playlistId: Int) {
        playlists.remove(playlistId)
        dao.deletePlaylist(playlistId)
    }

    fun createPlaylist(playlistName: String): Int {
        var newPlaylistId = dao.createNewPlaylist(playlistName).toInt()
        var newPlaylist = Playlist( playlistName, newPlaylistId, mutableListOf()) // w ten sposób?
        playlists.put(newPlaylistId, newPlaylist)
        return newPlaylistId
    }

    fun addSongToPlaylist(playlistId: Int, songId: Long) {
        var current: Playlist? = playlists[playlistId]
        if (current == null)
            return
        current.addSongId(songId)
        var newContent = PlaylistContent(playlistId, songId)
        dao.insertNewSong(newContent)
    }

    fun removeSongFromPlaylist(playlistId: Int, songId: Long) {
        var current: Playlist? = playlists[playlistId]
        if (current == null)
            return
        current.deleteSongId(songId)
        var selectedContent = PlaylistContent(playlistId, songId)
        dao.removeSong(selectedContent)
    }

}