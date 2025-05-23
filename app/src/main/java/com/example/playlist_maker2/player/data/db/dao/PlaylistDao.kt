package com.example.playlist_maker2.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker2.player.data.db.entities.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("SELECT * FROM playlist_table ORDER by playlistName ASC")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("UPDATE playlist_table SET playlistTracksCount = :playlistTracksCount WHERE playlistName = :playlistName")
    suspend fun setPlaylistTracksCount(playlistName: String, playlistTracksCount: Int)
}