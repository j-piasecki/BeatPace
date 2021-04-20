package io.beatpace.database

import io.beatpace.api.data.structures.Playlist

interface DAO {

    fun getPlaylists(): List<Playlist>
}