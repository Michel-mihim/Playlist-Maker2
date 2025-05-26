package com.example.playlist_maker2.lib.domain.models

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.INTEGER
import androidx.room.ColumnInfo.Companion.TEXT

data class PlaylistTrack(
    val trackId: String,
    val trackDuration: Int,
    val playlistName: String,
    val trackName: String,
    //FOR ADAPTER===================================================================================
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
)
