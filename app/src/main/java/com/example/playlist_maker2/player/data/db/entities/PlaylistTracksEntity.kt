package com.example.playlist_maker2.player.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_tracks_table")
data class PlaylistTracksEntity(
    @PrimaryKey @ColumnInfo(name = "playlistName", typeAffinity = ColumnInfo.TEXT)
    val playlistName: String,
    @ColumnInfo(name = "trackId", typeAffinity = ColumnInfo.TEXT)
    val trackId: String
)
