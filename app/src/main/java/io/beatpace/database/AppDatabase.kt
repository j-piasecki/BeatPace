package io.beatpace.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlaylistEntity::class, PlaylistContent::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): DAO
}