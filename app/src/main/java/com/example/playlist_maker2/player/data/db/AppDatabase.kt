package com.example.playlist_maker2.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlist_maker2.player.data.db.dao.PlaylistDao
import com.example.playlist_maker2.player.data.db.dao.PlaylistTracksDao
import com.example.playlist_maker2.player.data.db.dao.TrackDao
import com.example.playlist_maker2.player.data.db.entities.PlaylistEntity
import com.example.playlist_maker2.player.data.db.entities.PlaylistTrackEntity
import com.example.playlist_maker2.player.data.db.entities.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun playlistTracksDao(): PlaylistTracksDao
}