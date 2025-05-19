package com.example.playlist_maker2.lib.domain.models

data class Playlist(
    val playlistName: String,
    val playlistAbout: String,
    val playlistTracksCount: Int?,
    val playlistTracksDuration: Int?
)
