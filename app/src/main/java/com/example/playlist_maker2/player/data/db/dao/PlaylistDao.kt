package com.example.playlist_maker2.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.player.data.db.entities.PlaylistEntity
import com.example.playlist_maker2.player.data.db.entities.TrackEntity

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table ORDER by playlistName ASC")
    suspend fun getPlaylists(): List<PlaylistEntity>
}