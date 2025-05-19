package com.example.playlist_maker2.player.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.INTEGER
import androidx.room.ColumnInfo.Companion.TEXT
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey @ColumnInfo(name = "playlistName", typeAffinity = TEXT)
    val playlistName: String,
    @ColumnInfo(name = "playlistAbout", typeAffinity = TEXT)
    val playlistAbout: String,
    @ColumnInfo(name = "dateTime", typeAffinity = INTEGER)
    val dateTimeStamp: Long,
    @ColumnInfo(name = "playlistTracksCount", typeAffinity = INTEGER)
    val playlistTracksCount: Int?,
    @ColumnInfo(name = "playlistTracksDuration", typeAffinity = INTEGER)
    val playlistTracksDuration: Int?

)
