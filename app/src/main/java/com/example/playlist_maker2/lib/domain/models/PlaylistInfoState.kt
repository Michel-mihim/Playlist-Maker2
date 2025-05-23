package com.example.playlist_maker2.lib.domain.models

sealed interface PlaylistInfoState {

    data class Content(
        val playlistName: String,
        val playlistAbout: String
    ): PlaylistInfoState
}