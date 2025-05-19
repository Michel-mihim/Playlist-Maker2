package com.example.playlist_maker2.lib.domain.models

import androidx.room.ColumnInfo

data class PlaylistTrack(
    val trackId: String,
    val trackDuration: Int,
    val playlistName: String
)
