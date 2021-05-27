package io.beatpace.api

import io.beatpace.api.data.structures.Playlist
import io.beatpace.database.DAO
import io.beatpace.database.PlaylistContent
import io.beatpace.database.PlaylistEntity

/**
 *  Manages and stores all playlists while app is running
 */

class PlaylistManager(private val dao: DAO) {

    private val playlists: MutableMap<Int, Playlist> = HashMap()

    init{ // get all playlists and save them into our playlists map

        var entityList = dao.getPlaylists() // a temporary PlaylistEntityList

        for ( i in entityList.indices){ // creating and adding all our playlists into our map from the database

            val songList = ArrayList<Long>()// a list of all the songs in current playlist

            val contentList = dao.getAllSongs(entityList[i].playlistId!!)

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
        var newPlaylist = Playlist( playlistName, newPlaylistId, mutableListOf())
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