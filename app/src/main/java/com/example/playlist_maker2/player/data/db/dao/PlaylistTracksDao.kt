package com.example.playlist_maker2.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker2.player.data.db.entities.PlaylistTracksEntity

@Dao
interface PlaylistTracksDao {

    @Insert(entity = PlaylistTracksEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrack(playlistTrack: PlaylistTracksEntity): Long

    @Query("SELECT COUNT(trackId) FROM playlist_tracks_table WHERE playlistName = :playlistName")
    suspend fun getTracksCount(playlistName: String): Long
}