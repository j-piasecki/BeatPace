package io.beatpace.api.data.structures

import org.junit.Assert.*
import org.junit.Test


internal class PlaylistTest {
    private val example = Playlist("Example", 1, emptyList())

    @Test
    fun addSongById_addSongId_Added() {
        example.addSongId(100)
        assertEquals(100, example.getSong(0))
    }

    @Test
    fun deleteSongById_deleteSongId_Deleted() {
        example.addSongId(100)
        example.deleteSongId(100)
        assertEquals(0, example.getSize())
    }

    @Test
    fun playlistIsEmpty_getSize_Received() {
        assertEquals(0, example.getSize())
    }

    @Test
    fun playlistIsNotEmpty_getSize_Received() {
        example.addSongId(100)
        example.addSongId(101)
        assertEquals(2, example.getSize())
    }

    @Test
    fun checkIfContains_containsSong_contains() {
        example.addSongId(100)
        assertEquals(true, example.containsSong(100))
    }

    @Test
    fun checkIfContains_containsSong_doesNotContain() {
        assertEquals(false, example.containsSong(100))
    }
}