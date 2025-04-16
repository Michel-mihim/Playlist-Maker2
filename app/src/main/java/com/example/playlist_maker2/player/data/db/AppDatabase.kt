package com.example.playlist_maker2.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlist_maker2.player.data.db.dao.TrackDao
import com.example.playlist_maker2.player.data.db.entities.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
}