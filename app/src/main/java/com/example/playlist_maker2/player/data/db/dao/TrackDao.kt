package com.example.playlist_maker2.player.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker2.player.data.db.entities.TrackEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM track_table WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: String): List<String>

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity)
}