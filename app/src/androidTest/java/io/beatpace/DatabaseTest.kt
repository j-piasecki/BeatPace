package io.beatpace

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.beatpace.database.AppDatabase
import io.beatpace.database.DAO
import io.beatpace.database.PlaylistContent
import io.beatpace.database.PlaylistEntity
import org.junit.AfterClass

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    companion object {
        private lateinit var db: AppDatabase
        private lateinit var dao: DAO

        @BeforeClass
        @JvmStatic
        fun setupDatabase() {
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext

            db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()
            dao = db.dao()
        }

        @AfterClass
        @JvmStatic
        fun clearDatabase() {
            db.close()
        }
    }

    @Before
    fun resetDatabase() {
        db.clearAllTables()
    }

    @Test
    fun database_TestCreatePlaylist_Created() {
        dao.createNewPlaylist(title = "Best playlist")

        val allPlaylists = dao.getPlaylists()
        assertEquals("Best playlist", allPlaylists.first().title)
    }

    @Test
    fun database_TestDeletePlaylist_Deleted() {
        val id = dao.createNewPlaylist(title = "Best playlist")

        dao.deletePlaylist(id = id.toInt())

        val allPlaylists = dao.getPlaylists()
        assertEquals(0, allPlaylists.size)
    }

    @Test
    fun database_TestInsertSong_Inserted() {
        val id = dao.createNewPlaylist(title = "Best playlist")

        dao.insertNewSong(PlaylistContent(playlistId = id.toInt(), 0))

        val allSongs = dao.getAllSongs(id.toInt())
        assertEquals(0, allSongs.first().songId)
    }

    @Test
    fun database_TestDeleteSong_Deleted() {
        val id = dao.createNewPlaylist(title = "Best playlist")

        dao.insertNewSong(PlaylistContent(playlistId = id.toInt(), 0))
        dao.removeSong(PlaylistContent(playlistId = id.toInt(), 0))

        val allSongs = dao.getAllSongs(id.toInt())
        assertEquals(allSongs.size, 0)
    }

    @Test
    fun database_TestDeletePlaylistWithContent_Deleted() {
        val id = dao.createNewPlaylist(title = "Best playlist")

        dao.insertNewSong(PlaylistContent(playlistId = id.toInt(), 0))
        dao.insertNewSong(PlaylistContent(playlistId = id.toInt(), 1))

        dao.deletePlaylist(id = id.toInt())

        val allSongs = dao.getAllSongs(id.toInt())
        assertEquals(allSongs.size, 0)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun database_TestPlaylistPrimaryKeyViolation_ExceptionThrown() {
        dao.createNewPlaylist(PlaylistEntity(playlistId = 0, title = "Playlist 1"))
        dao.createNewPlaylist(PlaylistEntity(playlistId = 0, title = "Playlist 2"))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun database_TestPlaylistContentPrimaryKeyViolation_ExceptionThrown() {
        val id = dao.createNewPlaylist(title = "Playlist 1").toInt()

        dao.insertNewSong(PlaylistContent(playlistId = id, songId = 0))
        dao.insertNewSong(PlaylistContent(playlistId = id, songId = 0))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun database_TestAddSongPlaylistDoesNotExist_ExceptionThrown() {
        dao.insertNewSong(PlaylistContent(playlistId = 0, songId = 0))
    }
}