package io.beatpace.api.data.structures

data class Playlist(var name: String, var Id: Int, private val songList: MutableList<Long>) { // MutableList lista która można zmieniać

    fun addSongId(songId: Long) {
        if (!containsSong(songId)) // sprawdza czy piosenka nie jest już w playliscie
            songList.add(songId)

    }

    fun deleteSongId(songId: Long) {
        if (containsSong(songId))
            songList.remove(songId)
    }

    fun getSong(songPosition: Int): Long {
        return songList[songPosition]
    }

    fun getSize(): Int {
        return songList.size
    }

    fun containsSong(songId: Long): Boolean {
        return songList.contains(songId)
    }
}