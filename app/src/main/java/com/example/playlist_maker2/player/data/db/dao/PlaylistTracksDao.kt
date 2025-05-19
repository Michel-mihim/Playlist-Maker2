package com.example.playlist_maker2.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker2.player.data.db.entities.PlaylistTracksEntity
import com.example.playlist_maker2.player.data.db.entities.TrackEntity

@Dao
interface PlaylistTracksDao {

    @Insert(entity = PlaylistTracksEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrack(playlistTrack: PlaylistTracksEntity): Long

    @Query("SELECT * FROM playlist_tracks_table WHERE playlistName = :playlistName")
    suspend fun getEditTracks(playlistName: String): List<PlaylistTracksEntity>

    @Query("SELECT COUNT(trackId) FROM playlist_tracks_table WHERE playlistName = :playlistName")
    suspend fun getTracksCount(playlistName: String): Int

    @Query("SELECT SUM(trackDuration) FROM playlist_tracks_table WHERE playlistName = :playlistName")
    suspend fun getTracksDuration(playlistName: String): Int
}