package io.beatpace.api

import io.beatpace.database.DAO
import io.beatpace.database.PlaylistContent
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.*


internal class PlaylistManagerTest {
    private lateinit var example2: PlaylistManager
    private lateinit var dao: DAO

    @Before
    fun setUp() {
        dao = mock(DAO::class.java)
        `when`(dao.getPlaylists()).thenReturn(listOf())
        example2 = PlaylistManager(dao)
    }

    @Test
    fun findExistingPlaylist_getPlaylistById_Received() {
        val Id = example2.createPlaylist("First")
        assertNotNull ( example2.getPlaylistById(Id))
    }

    @Test
    fun deleteExistingPlaylist_deletePlaylistById_Deleted() {
        val Id = example2.createPlaylist( "First")
        example2.deletePlaylistById(Id)
        assertNull( example2.getPlaylistById(Id))
    }

    @Test
    fun deletePlaylistById_DeletePlayListInDAO_MethodNotExecuted() {
        example2.deletePlaylistById(123)

        verify(dao, times(1)).deletePlaylist(123)
    }

    @Test
    fun deletePlaylistById_DeletePlayListInDAO_MethodExecuted() {
        val id = example2.createPlaylist("Playlist")
        example2.deletePlaylistById(id)

        verify(dao, times(1)).deletePlaylist(id)
    }

    @Test
    fun newPlaylist_createPlaylist_Created() {
        val Id = example2.createPlaylist("First")
        assertNotNull( example2.getPlaylistById(Id))
    }

    @Test
    fun createPlaylist_CreateNewPlayListInDAO_MethodExecuted() {
        val name = "Any name"
        example2.createPlaylist(name)

        verify(dao, times(1)).createNewPlaylist(name)
    }

    @Test
    fun addNewSong_addSongToPlaylist_Added() {
        val Id = example2.createPlaylist("First")
        val current = example2.getPlaylistById(Id)
        example2.addSongToPlaylist( Id, 100)
        assertEquals(100, current!!.getSong(0))
    }

    @Test
    fun addSongToPlaylist_addSongToPlaylistInDAO_MethodExecuted() {
        val id = example2.createPlaylist("Playlist")
        val songId = 200L
        example2.addSongToPlaylist(id, songId)

        verify(dao, times(1)).insertNewSong(PlaylistContent(id, songId))
    }


    @Test
    fun removeExistingSong_removeSongFromPlaylist_Removed() {
        val Id = example2.createPlaylist("First")
        val current = example2.getPlaylistById(Id)
        example2.addSongToPlaylist( Id, 100)
        example2.removeSongFromPlaylist(Id, 100)
        assertEquals( 0, current!!.getSize())
    }

    @Test
    fun removeSongFromPlaylist_removeSongInPlaylistInDAO_MethodNotExecuted() {
        val id = example2.createPlaylist("Playlist")
        val songId = 200L
        example2.removeSongFromPlaylist(id, songId)

        verify(dao, times(1)).removeSong(PlaylistContent(id, songId))
    }

    @Test
    fun removeSongFromPlaylist_removeSongInPlaylistInDAO_MethodExecuted() {
        val id = example2.createPlaylist("Playlist")
        val songId = 200L
        example2.addSongToPlaylist(id, songId)

        example2.removeSongFromPlaylist(id, songId)

        verify(dao, times(1)).removeSong(PlaylistContent(id, songId))
    }
}