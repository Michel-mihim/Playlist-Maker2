package com.example.playlist_maker2.player.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker2.player.data.db.entities.PlaylistTrackEntity

@Dao
interface PlaylistTracksDao {

    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrack(playlistTrack: PlaylistTrackEntity): Long

    @Query("DELETE FROM playlist_tracks_table WHERE playlistName = :playlistName AND trackId = :trackId")
    suspend fun deletePlaylistTrack(trackId: String, playlistName: String)

    @Query("DELETE FROM playlist_tracks_table WHERE playlistName = :playlistName")
    suspend fun deletePlaylist(playlistName: String)

    @Query("SELECT * FROM playlist_tracks_table WHERE playlistName = :playlistName")
    suspend fun getEditTracks(playlistName: String): List<PlaylistTrackEntity>

    @Query("SELECT COUNT(trackId) FROM playlist_tracks_table WHERE playlistName = :playlistName")
    suspend fun getTracksCount(playlistName: String): Int

    @Query("SELECT SUM(trackDuration) FROM playlist_tracks_table WHERE playlistName = :playlistName")
    suspend fun getTracksDuration(playlistName: String): Int?
}