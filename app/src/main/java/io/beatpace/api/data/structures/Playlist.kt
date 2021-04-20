package io.beatpace.api.data.structures

data class Playlist(var name: String, var Id: Int, private val songList: List<Long>) {

    fun addSongId(songId: Long) {}

    fun deleteSongId(songId: Long) {}

    fun getSong(songPosition: Int): Long {
        TODO("Not yet implemented")
    }

    fun getSize(): Int {
        TODO("Not yet implemented")
    }
}