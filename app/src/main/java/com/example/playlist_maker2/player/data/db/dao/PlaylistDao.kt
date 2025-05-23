package com.example.playlist_maker2.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.player.data.db.entities.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("SELECT * FROM playlist_table ORDER by playlistName ASC")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE playlistName = :playlistName")
    suspend fun readPlaylist(playlistName: String): PlaylistEntity

    @Query("DELETE FROM playlist_table WHERE playlistName = :playlistName")
    suspend fun deletePlaylist(playlistName: String)

    @Query("UPDATE playlist_table SET playlistTracksCount = :playlistTracksCount, playlistTracksDuration = :playlistTracksDuration WHERE playlistName = :playlistName")
    suspend fun setPlaylistTracksCalculation(playlistName: String, playlistTracksCount: Int, playlistTracksDuration: Int)

    @Query("UPDATE playlist_table SET playlistName = :newPlaylistName, playlistAbout = :newPlaylistAbout WHERE playlistName = :oldPlaylistName")
    suspend fun setPlaylistInformation(oldPlaylistName: String, newPlaylistName: String, newPlaylistAbout: String)

}