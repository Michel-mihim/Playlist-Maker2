package com.example.playlist_maker2.player.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "playlist_tracks_table", primaryKeys = ["playlistName", "trackId"])
data class PlaylistTracksEntity(
    @ColumnInfo(name = "trackId", typeAffinity = ColumnInfo.TEXT)
    val trackId: String,
    @ColumnInfo(name = "playlistName", typeAffinity = ColumnInfo.TEXT)
    val playlistName: String
)
